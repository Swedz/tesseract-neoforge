package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.powerless;

import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.api.machine.holder.CrafterComponentHolder;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.ReiSlotLocking;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import aztech.modern_industrialization.machines.models.MachineModelClientData;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.util.Simulation;
import net.minecraft.server.level.ServerLevel;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGui;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.BasicMultiblockMachineBlockEntity;

import java.util.UUID;

import static net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine.*;

public class PowerlessMultiblockMachineBlockEntity extends BasicMultiblockMachineBlockEntity implements CrafterComponentHolder
{
	protected final MachineRecipeType recipeType;
	protected final int               baseRecipeEU;
	
	protected final CrafterComponent crafter;
	
	protected final RedstoneControlComponent redstoneControl;
	
	protected OperatingState operatingState = OperatingState.NOT_MATCHED;
	
	public PowerlessMultiblockMachineBlockEntity(
			BEP bep, MachineGuiParameters guiParams, ShapeTemplate shape,
			MachineRecipeType recipeType, int baseRecipeEU, boolean hasRedstoneControl
	)
	{
		super(bep, guiParams, new ShapeTemplate[]{shape});
		
		this.recipeType = recipeType;
		this.baseRecipeEU = baseRecipeEU;
		
		this.crafter = new CrafterComponent(this, inventory, new Behavior());
		
		this.redstoneControl = hasRedstoneControl ? new RedstoneControlComponent() : null;
		
		this.registerComponents(crafter, isActive);
		
		if(hasRedstoneControl)
		{
			this.registerComponents(redstoneControl);
			this.registerGuiComponent(new SlotPanel.Server(this)
					.withRedstoneControl(redstoneControl));
		}
		
		this.registerGuiComponent(new ReiSlotLocking.Server(crafter::lockRecipe, () -> operatingState != OperatingState.NOT_MATCHED));
		this.registerGuiComponent(new ModularMultiblockGui.Server(ModularMultiblockGui.HEIGHT, (content) ->
		{
			boolean shapeValid = this.isShapeValid();
			boolean active = isActive.isActive;
			
			content.add((shapeValid ? MIText.MultiblockShapeValid : MIText.MultiblockShapeInvalid).text(), shapeValid ? WHITE : RED);
			if(shapeValid)
			{
				content.add(MIText.MultiblockStatusActive.text());
				
				if(crafter != null && crafter.hasActiveRecipe())
				{
					content.add(MIText.Progress.text(String.format("%.1f", crafter.getProgress() * 100) + " %"));
				}
			}
		}));
	}
	
	@Override
	protected MachineModelClientData getMachineModelData()
	{
		MachineModelClientData data = new MachineModelClientData();
		orientation.writeModelData(data);
		data.isActive = isActive.isActive;
		return data;
	}
	
	@Override
	public CrafterAccess getCrafterComponent()
	{
		return crafter;
	}
	
	@Override
	protected void onRematch(ShapeMatcher shapeMatcher)
	{
		super.onRematch(shapeMatcher);
		
		operatingState = OperatingState.NOT_MATCHED;
		if(shapeMatcher.isMatchSuccessful())
		{
			operatingState = OperatingState.TRYING_TO_RESUME;
		}
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(level.isClientSide())
		{
			return;
		}
		
		boolean active = false;
		if(operatingState == OperatingState.TRYING_TO_RESUME)
		{
			if(crafter.tryContinueRecipe())
			{
				operatingState = OperatingState.NORMAL_OPERATION;
			}
		}
		if(operatingState == OperatingState.NORMAL_OPERATION)
		{
			if(crafter.tickRecipe())
			{
				active = true;
			}
		}
		else
		{
			crafter.decreaseEfficiencyTicks();
		}
		this.updateActive(active);
	}
	
	private class Behavior implements CrafterComponent.Behavior
	{
		@Override
		public boolean isEnabled()
		{
			return redstoneControl == null || redstoneControl.doAllowNormalOperation(PowerlessMultiblockMachineBlockEntity.this);
		}
		
		@Override
		public long consumeEu(long max, Simulation simulation)
		{
			return max;
		}
		
		@Override
		public MachineRecipeType recipeType()
		{
			return recipeType;
		}
		
		@Override
		public long getBaseRecipeEu()
		{
			return baseRecipeEU;
		}
		
		@Override
		public long getMaxRecipeEu()
		{
			return this.getBaseRecipeEu();
		}
		
		@Override
		public ServerLevel getCrafterWorld()
		{
			return (ServerLevel) level;
		}
		
		@Override
		public UUID getOwnerUuid()
		{
			return placedBy.placerId;
		}
	}
	
	public enum OperatingState
	{
		/**
		 * Shape is not matched, don't do anything.
		 */
		NOT_MATCHED,
		/**
		 * Trying to resume a recipe but the output might not fit anymore.
		 * We wait until the output fits again before resuming normal operation.
		 */
		TRYING_TO_RESUME,
		/**
		 * Normal operation.
		 */
		NORMAL_OPERATION
	}
}
