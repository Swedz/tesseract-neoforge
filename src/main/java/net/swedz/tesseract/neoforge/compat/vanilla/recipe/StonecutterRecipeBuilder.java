package net.swedz.tesseract.neoforge.compat.vanilla.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.helper.RecipeHelper;
import net.swedz.tesseract.neoforge.recipe.RecipeBuilder;

public class StonecutterRecipeBuilder extends RecipeBuilder
{
	protected Ingredient input;
	
	public Ingredient input()
	{
		return input;
	}
	
	public StonecutterRecipeBuilder input(Ingredient input)
	{
		if(input == null || input == Ingredient.EMPTY)
		{
			throw new NullPointerException("Input ingredient cannot be null or empty");
		}
		this.input = input;
		return this;
	}
	
	public StonecutterRecipeBuilder input(ItemLike... items)
	{
		return this.input(Ingredient.of(items));
	}
	
	public StonecutterRecipeBuilder input(ItemStack... stacks)
	{
		return this.input(Ingredient.of(stacks));
	}
	
	public StonecutterRecipeBuilder input(TagKey<Item> tag)
	{
		return this.input(Ingredient.of(tag));
	}
	
	public StonecutterRecipeBuilder input(ResourceLocation... itemIds)
	{
		return this.input(RecipeHelper.ingredient(itemIds));
	}
	
	public StonecutterRecipeBuilder input(String maybeTag)
	{
		return this.input(RecipeHelper.ingredient(maybeTag));
	}
	
	@Override
	public void validate()
	{
		if(input == null || input == Ingredient.EMPTY)
		{
			throw new IllegalArgumentException("No input ingredient was provided");
		}
	}
	
	@Override
	public Recipe<?> convert()
	{
		return new StonecutterRecipe("", input, result);
	}
	
	//<editor-fold desc="Inherited...">
	@Override
	public StonecutterRecipeBuilder output(ItemStack result)
	{
		super.output(result);
		return this;
	}
	
	@Override
	public StonecutterRecipeBuilder output(ItemLike result, int count)
	{
		super.output(result, count);
		return this;
	}
	
	@Override
	public StonecutterRecipeBuilder output(String result, int count)
	{
		super.output(result, count);
		return this;
	}
	//</editor-fold>
}
