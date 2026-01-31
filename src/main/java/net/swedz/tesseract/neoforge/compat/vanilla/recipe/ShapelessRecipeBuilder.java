package net.swedz.tesseract.neoforge.compat.vanilla.recipe;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.helper.RecipeHelper;
import net.swedz.tesseract.neoforge.recipe.RecipeBuilder;

import java.util.Collections;
import java.util.List;

public class ShapelessRecipeBuilder extends RecipeBuilder
{
	protected final List<Ingredient> input = Lists.newArrayList();
	
	public ShapelessRecipeBuilder(HolderGetter<Item> itemGetter)
	{
		super(itemGetter);
	}
	
	public List<Ingredient> input()
	{
		return Collections.unmodifiableList(input);
	}
	
	public ShapelessRecipeBuilder with(Ingredient ingredient)
	{
		Assert.notNull(ingredient);
		input.add(ingredient);
		return this;
	}
	
	public ShapelessRecipeBuilder with(ItemLike... items)
	{
		return this.with(Ingredient.of(items));
	}
	
	public ShapelessRecipeBuilder with(TagKey<Item> tag)
	{
		return this.with(Ingredient.of(itemGetter.getOrThrow(tag)));
	}
	
	public ShapelessRecipeBuilder with(Identifier... itemIds)
	{
		return this.with(RecipeHelper.ingredient(itemIds));
	}
	
	public ShapelessRecipeBuilder with(String maybeTag)
	{
		return this.with(RecipeHelper.ingredient(itemGetter, maybeTag));
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
	public ShapelessRecipeBuilder output(ItemStack result)
	{
		super.output(result);
		return this;
	}
	
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
