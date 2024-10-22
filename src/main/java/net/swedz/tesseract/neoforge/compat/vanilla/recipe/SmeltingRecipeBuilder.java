package net.swedz.tesseract.neoforge.compat.vanilla.recipe;

import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.recipe.RecipeBuilder;

public class SmeltingRecipeBuilder extends RecipeBuilder
{
	protected Ingredient input;
	protected int        cookingTime;
	protected float      experience;
	protected boolean    blasting;
	
	public Ingredient input()
	{
		return input;
	}
	
	public SmeltingRecipeBuilder input(Ingredient input)
	{
		if(input == null || input == Ingredient.EMPTY)
		{
			throw new NullPointerException("Input ingredient cannot be null or empty");
		}
		this.input = input;
		return this;
	}
	
	public int cookingTime()
	{
		return cookingTime;
	}
	
	public SmeltingRecipeBuilder cookingTime(int cookingTime)
	{
		if(cookingTime <= 0)
		{
			throw new IllegalArgumentException("Cooking time must be positive");
		}
		this.cookingTime = cookingTime;
		return this;
	}
	
	public float experience()
	{
		return experience;
	}
	
	public SmeltingRecipeBuilder experience(float experience)
	{
		if(experience < 0)
		{
			throw new IllegalArgumentException("Experience must be positive or 0");
		}
		this.experience = experience;
		return this;
	}
	
	public boolean isBlasting()
	{
		return blasting;
	}
	
	public SmeltingRecipeBuilder blasting()
	{
		this.blasting = true;
		return this;
	}
	
	@Override
	public void validate()
	{
		if(input == null || input == Ingredient.EMPTY)
		{
			throw new IllegalArgumentException("No input ingredient was provided");
		}
		if(cookingTime == 0)
		{
			throw new IllegalArgumentException("No cooking time was provided");
		}
	}
	
	@Override
	public Recipe<?> convert()
	{
		AbstractCookingRecipe.Factory<?> factory = blasting ? BlastingRecipe::new : SmeltingRecipe::new;
		return factory.create(
				"",
				CookingBookCategory.MISC,
				input,
				result,
				experience,
				cookingTime
		);
	}
	
	//<editor-fold desc="Inherited...">
	@Override
	public SmeltingRecipeBuilder output(ItemLike result, int count)
	{
		super.output(result, count);
		return this;
	}
	
	@Override
	public SmeltingRecipeBuilder output(String result, int count)
	{
		super.output(result, count);
		return this;
	}
	//</editor-fold>
}
