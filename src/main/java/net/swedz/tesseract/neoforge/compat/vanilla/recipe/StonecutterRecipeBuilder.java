package net.swedz.tesseract.neoforge.compat.vanilla.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.helper.RecipeHelper;
import net.swedz.tesseract.neoforge.recipe.RecipeBuilder;

public class StonecutterRecipeBuilder extends RecipeBuilder
{
	protected Ingredient input;
	
	public StonecutterRecipeBuilder(HolderLookup.Provider registries)
	{
		super(registries);
	}
	
	public Ingredient input()
	{
		return input;
	}
	
	public StonecutterRecipeBuilder input(Ingredient input)
	{
		Assert.notNull(input);
		this.input = input;
		return this;
	}
	
	public StonecutterRecipeBuilder input(ItemLike... items)
	{
		return this.input(Ingredient.of(items));
	}
	
	public StonecutterRecipeBuilder input(TagKey<Item> tag)
	{
		return this.input(Ingredient.of(RecipeHelper.items(registries).getOrThrow(tag)));
	}
	
	public StonecutterRecipeBuilder input(Identifier... itemIds)
	{
		return this.input(RecipeHelper.ingredient(itemIds));
	}
	
	public StonecutterRecipeBuilder input(String maybeTag)
	{
		return this.input(RecipeHelper.ingredient(RecipeHelper.items(registries), maybeTag));
	}
	
	@Override
	public void validate()
	{
		if(input == null || input.isEmpty())
		{
			throw new IllegalArgumentException("No input ingredient was provided");
		}
	}
	
	@Override
	public Recipe<?> convert()
	{
		return new StonecutterRecipe(new Recipe.CommonInfo(false), input, result);
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
