package net.swedz.tesseract.neoforge.helper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;

public final class RecipeHelper
{
	public static Ingredient ingredient(String maybeTag)
	{
		return maybeTag.startsWith("#") ?
				Ingredient.of(ItemTags.create(ResourceLocation.parse(maybeTag.substring(1)))) :
				Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.parse(maybeTag)));
	}
}
