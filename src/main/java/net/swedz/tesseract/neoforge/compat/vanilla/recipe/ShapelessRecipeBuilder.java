package net.swedz.tesseract.neoforge.compat.vanilla.recipe;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.helper.RecipeHelper;
import net.swedz.tesseract.neoforge.recipe.RecipeBuilder;

import java.util.Collections;
import java.util.List;

public class ShapelessRecipeBuilder extends RecipeBuilder
{
	protected final List<Ingredient> input = Lists.newArrayList();
	
	public List<Ingredient> input()
	{
		return Collections.unmodifiableList(input);
	}
	
	public ShapelessRecipeBuilder with(Ingredient ingredient)
	{
		if(ingredient == null || ingredient == Ingredient.EMPTY)
		{
			throw new NullPointerException("Input ingredient cannot be null or empty");
		}
		input.add(ingredient);
		return this;
	}
	
	public ShapelessRecipeBuilder with(ItemLike... items)
	{
		return this.with(Ingredient.of(items));
	}
	
	public ShapelessRecipeBuilder with(ItemStack... stacks)
	{
		return this.with(Ingredient.of(stacks));
	}
	
	public ShapelessRecipeBuilder with(TagKey<Item> tag)
	{
		return this.with(Ingredient.of(tag));
	}
	
	public ShapelessRecipeBuilder with(String maybeTag)
	{
		return this.with(RecipeHelper.ingredient(maybeTag));
	}
	
	@Override
	public void validate()
	{
		if(input.isEmpty() || input.size() > 9)
		{
			throw new IllegalArgumentException("Invalid length " + input.size());
		}
	}
	
	@Override
	public Recipe<?> convert()
	{
		return new ShapelessRecipe(
				"",
				CraftingBookCategory.MISC,
				result,
				NonNullList.copyOf(input)
		);
	}
	
	//<editor-fold desc="Inherited...">
	@Override
	public ShapelessRecipeBuilder output(ItemLike result, int count)
	{
		super.output(result, count);
		return this;
	}
	
	@Override
	public ShapelessRecipeBuilder output(String result, int count)
	{
		super.output(result, count);
		return this;
	}
	//</editor-fold>
}
