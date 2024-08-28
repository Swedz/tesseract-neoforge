package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformer;

import java.util.List;

public class SteamMultipliedCraftingMultiblockBlockEntity extends AbstractSteamMultipliedCraftingMultiblockBlockEntity
{
	protected final MachineRecipeType recipeType;
	protected final int               maxMultiplier;
	protected final EuCostTransformer euCostTransformer;
	
	public SteamMultipliedCraftingMultiblockBlockEntity(BEP bep, ResourceLocation id, ShapeTemplate[] shapeTemplates, List<OverclockComponent.Catalyst> overclockCatalysts, MachineRecipeType recipeType, int maxMultiplier, EuCostTransformer euCostTransformer)
	{
		super(bep, id, shapeTemplates, overclockCatalysts);
		
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
