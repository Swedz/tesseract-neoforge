package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformer;

public class ElectricMultipliedCraftingMultiblockBlockEntity extends AbstractElectricMultipliedCraftingMultiblockBlockEntity
{
	protected final MachineRecipeType recipeType;
	protected final int               maxMultiplier;
	protected final EuCostTransformer euCostTransformer;
	
	public ElectricMultipliedCraftingMultiblockBlockEntity(BEP bep, ResourceLocation id, ShapeTemplate[] shapeTemplates,
														   MachineTier machineTier,
														   MachineRecipeType recipeType, int maxMultiplier, EuCostTransformer euCostTransformer)
	{
		super(bep, id, shapeTemplates, machineTier);
		
		this.recipeType = recipeType;
		this.maxMultiplier = maxMultiplier;
		this.euCostTransformer = euCostTransformer;
	}
	
	@Override
	public MachineRecipeType getRecipeType()
	{
		return recipeType;
	}
	
	@Override
	public int getMaxMultiplier()
	{
		return maxMultiplier;
	}
	
	@Override
	public EuCostTransformer getEuCostTransformer()
	{
		return euCostTransformer;
	}
}
