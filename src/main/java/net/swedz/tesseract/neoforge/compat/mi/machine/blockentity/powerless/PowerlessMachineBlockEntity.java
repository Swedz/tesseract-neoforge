package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.powerless;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.api.machine.holder.CrafterComponentHolder;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.components.IsActiveComponent;
import aztech.modern_industrialization.machines.components.MachineInventoryComponent;
import aztech.modern_industrialization.machines.components.OrientationComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.AutoExtract;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.ReiSlotLocking;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import aztech.modern_industrialization.machines.models.MachineModelClientData;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.util.Simulation;
import aztech.modern_industrialization.util.Tickable;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class PowerlessMachineBlockEntity extends MachineBlockEntity implements Tickable, CrafterComponentHolder
{
	protected final MachineRecipeType recipeType;
	protected final int               baseRecipeEU;
	
	protected final MachineInventoryComponent inventory;
	protected final CrafterComponent          crafter;
	protected final IsActiveComponent         isActive;
	
	protected final RedstoneControlComponent redstoneControl;
	
	public PowerlessMachineBlockEntity(
			BEP bep, MachineGuiParameters guiParams, ProgressBar.Parameters progressBarParams,
			MachineInventoryComponent inventory,
			MachineRecipeType recipeType, int baseRecipeEU, boolean hasRedstoneControl
	)
	{
		super(
				bep, guiParams,
				new OrientationComponent.Params(
						true,
						inventory.itemOutputCount > 0,
						inventory.fluidOutputCount > 0
				)
		);
		
		this.recipeType = recipeType;
		this.baseRecipeEU = baseRecipeEU;
		
		this.inventory = inventory;
		this.crafter = new CrafterComponent(this, inventory, new Behavior());
		this.isActive = new IsActiveComponent();
		
		this.redstoneControl = hasRedstoneControl ? new RedstoneControlComponent() : null;
		
		this.registerComponents(inventory, crafter, isActive);
		
		if(hasRedstoneControl)
		{
			this.registerComponents(redstoneControl);
			this.registerGuiComponent(new SlotPanel.Server(this)
					.withRedstoneControl(redstoneControl));
		}
		
		this.registerGuiComponent(new AutoExtract.Server(orientation));
		this.registerGuiComponent(new ProgressBar.Server(progressBarParams, crafter::getProgress));
		this.registerGuiComponent(new ReiSlotLocking.Server(crafter::lockRecipe, () -> true));
	}
	
	@Override
	public MIInventory getInventory()
	{
		return inventory.inventory;
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
	public void tick()
	{
		if(level.isClientSide())
		{
			return;
		}
		
		boolean active = crafter.tickRecipe();
		isActive.updateActive(active, this);
		
		if(orientation.extractItems)
		{
			inventory.inventory.autoExtractItems(level, worldPosition, orientation.outputDirection);
		}
		if(orientation.extractFluids)
		{
			inventory.inventory.autoExtractFluids(level, worldPosition, orientation.outputDirection);
		}
		
		this.setChanged();
	}
	
	private class Behavior implements CrafterComponent.Behavior
	{
		@Override
		public boolean isEnabled()
		{
			return redstoneControl == null || redstoneControl.doAllowNormalOperation(PowerlessMachineBlockEntity.this);
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
}
