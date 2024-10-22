package net.swedz.tesseract.neoforge.recipe;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public abstract class RecipeBuilder implements RecipeOfferable
{
	protected ItemStack result;
	
	public ItemStack result()
	{
		return result;
	}
	
	public RecipeBuilder output(ItemLike result, int count)
	{
		this.result = new ItemStack(result, count);
		return this;
	}
	
	public RecipeBuilder output(String result, int count)
	{
		return this.output(BuiltInRegistries.ITEM.get(ResourceLocation.parse(result)), count);
	}
	
	@Override
	public void offerTo(RecipeOutput recipeOutput, ResourceLocation location, AdvancementHolder advancement)
	{
		if(result == null)
		{
			throw new IllegalArgumentException("No result provided");
		}
		RecipeOfferable.super.offerTo(recipeOutput, location, advancement);
	}
}
