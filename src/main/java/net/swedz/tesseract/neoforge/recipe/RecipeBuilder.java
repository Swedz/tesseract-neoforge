package net.swedz.tesseract.neoforge.recipe;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

public abstract class RecipeBuilder implements RecipeOfferable
{
	protected final HolderGetter<Item> itemGetter;
	
	protected ItemStackTemplate result;
	
	public RecipeBuilder(HolderGetter<Item> itemGetter)
	{
		this.itemGetter = itemGetter;
	}
	
	public ItemStackTemplate result()
	{
		return result;
	}
	
	public RecipeBuilder output(ItemStackTemplate result)
	{
		this.result = result;
		return this;
	}
	
	public RecipeBuilder output(ItemStack result)
	{
		return this.output(ItemStackTemplate.fromNonEmptyStack(result));
	}
	
	public RecipeBuilder output(ItemLike result, int count)
	{
		return this.output(new ItemStackTemplate(result.asItem(), count));
	}
	
	public RecipeBuilder output(Identifier result, int count)
	{
		return this.output(BuiltInRegistries.ITEM.getValue(result), count);
	}
	
	public RecipeBuilder output(String result, int count)
	{
		return this.output(Identifier.parse(result), count);
	}
	
	@Override
	public void offerTo(RecipeOutput recipeOutput, Identifier location, AdvancementHolder advancement)
	{
		if(result == null)
		{
			throw new IllegalArgumentException("No result provided");
		}
		RecipeOfferable.super.offerTo(recipeOutput, location, advancement);
	}
}
