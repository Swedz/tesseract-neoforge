package net.swedz.tesseract.neoforge.compat.mi.recipe;

import aztech.modern_industrialization.blocks.forgehammer.ForgeHammerRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.recipe.RecipeBuilder;

public class MIForgeHammerRecipeBuilder extends RecipeBuilder
{
	protected Ingredient input;
	protected int        inputCount;
	protected int        hammerDamage;
	
	public Ingredient input()
	{
		return input;
	}
	
	public int inputCount()
	{
		return inputCount;
	}
	
	public MIForgeHammerRecipeBuilder input(Ingredient input, int count)
	{
		if(input == null || input == Ingredient.EMPTY)
		{
			throw new NullPointerException("Input ingredient cannot be null");
		}
		if(count <= 0)
		{
			throw new IllegalArgumentException("Input count must be positive");
		}
		this.input = input;
		this.inputCount = count;
		return this;
	}
	
	public int hammerDamage()
	{
		return hammerDamage;
	}
	
	public MIForgeHammerRecipeBuilder hammerDamage(int hammerDamage)
	{
		if(hammerDamage < 0)
		{
			throw new IllegalArgumentException("Hammer damage must be positive or 0");
		}
		this.hammerDamage = hammerDamage;
		return this;
	}
	
	@Override
	public void validate()
	{
		if(input == null || input == Ingredient.EMPTY || inputCount <= 0)
		{
			throw new IllegalArgumentException("No input was provided");
		}
		if(hammerDamage < 0)
		{
			throw new IllegalArgumentException("Hammer damage must be positive or 0");
		}
	}
	
	@Override
	public Recipe<?> convert()
	{
		return new ForgeHammerRecipe(input, inputCount, result, hammerDamage);
	}
	
	//<editor-fold desc="Inherited...">
	@Override
	public MIForgeHammerRecipeBuilder output(ItemLike result, int count)
	{
		super.output(result, count);
		return this;
	}
	
	@Override
	public MIForgeHammerRecipeBuilder output(String result, int count)
	{
		super.output(result, count);
		return this;
	}
	//</editor-fold>
}
