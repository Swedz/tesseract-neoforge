package net.swedz.tesseract.neoforge.helper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.Stream;

public final class RecipeHelper
{
	public static Ingredient ingredient(String maybeTag)
	{
		return maybeTag.startsWith("#") ?
				Ingredient.of(ItemTags.create(ResourceLocation.parse(maybeTag.substring(1)))) :
				Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.parse(maybeTag)));
	}
	
	public static Ingredient ingredient(ResourceLocation... itemIds)
	{
		return Ingredient.of(Stream.of(itemIds).map(BuiltInRegistries.ITEM::get).toArray(Item[]::new));
	}
}
