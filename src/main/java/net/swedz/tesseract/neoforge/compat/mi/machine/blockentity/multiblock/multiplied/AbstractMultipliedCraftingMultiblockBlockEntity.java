package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.api.machine.holder.CrafterComponentHolder;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.ReiSlotLocking;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformer;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.MultipliedCrafterComponent;
import net.swedz.tesseract.neoforge.compat.mi.helper.CommonGuiComponents;
import net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.BasicMultiblockMachineBlockEntity;

import java.util.UUID;

public abstract class AbstractMultipliedCraftingMultiblockBlockEntity extends BasicMultiblockMachineBlockEntity implements CrafterComponentHolder, ModularCrafterAccessBehavior
{
	protected final MultipliedCrafterComponent crafter;
	
	protected OperatingState operatingState = OperatingState.NOT_MATCHED;
	
	public AbstractMultipliedCraftingMultiblockBlockEntity(BEP bep, ResourceLocation id, ShapeTemplate[] shapeTemplates)
	{
		super(bep, new MachineGuiParameters.Builder(id, false).backgroundHeight(200).build(), shapeTemplates);
		
		this.crafter = new MultipliedCrafterComponent(
				this, inventory, this,
				this::getRecipeType, this::getMaxMultiplier, this::getEuCostTransformer
		);
		
		this.registerComponents(crafter);
		
		this.registerGuiComponent(new ReiSlotLocking.Server(crafter::lockRecipe, () -> operatingState != OperatingState.NOT_MATCHED));
		
		this.registerGuiComponent(CommonGuiComponents.standardMultiblockScreen(this, crafter, isActive));
	}
	
	public abstract MachineRecipeType getRecipeType();
	
	public abstract int getMaxMultiplier();
	
	public abstract EuCostTransformer getEuCostTransformer();
	
	protected long transformEuCost(long eu)
	{
		return this.getEuCostTransformer().transform(crafter, eu);
	}
	
	@Override
	public CrafterAccess getCrafterComponent()
	{
		return crafter;
	}
	
	@Override
	public Level getCrafterWorld()
	{
		return level;
	}
	
	@Override
	public UUID getOwnerUuid()
	{
		return placedBy.placerId;
	}
	
	@Override
	protected void onRematch()
	{
		operatingState = OperatingState.NOT_MATCHED;
	}
	
	@Override
	public void onMatchSuccessful()
	{
		super.onMatchSuccessful();
		
		operatingState = OperatingState.TRYING_TO_RESUME;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(!level.isClientSide)
		{
			boolean newActive = false;
			
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
					newActive = true;
				}
			}
			else
			{
				crafter.decreaseEfficiencyTicks();
			}
			
			this.updateActive(newActive);
		}
		
		this.tickExtra();
	}
	
	public void tickExtra()
	{
	}
}
