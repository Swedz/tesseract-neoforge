package net.swedz.tesseract.neoforge.helper;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.stream.Stream;

public final class RecipeHelper
{
	public static HolderGetter<Item> items(HolderLookup.Provider registries)
	{
		return registries.lookupOrThrow(Registries.ITEM);
	}
	
	public static HolderGetter<Fluid> fluids(HolderLookup.Provider registries)
	{
		return registries.lookupOrThrow(Registries.FLUID);
	}
	
	public static Ingredient ingredient(HolderGetter<Item> itemGetter, String maybeTag)
	{
		return maybeTag.startsWith("#") ?
				Ingredient.of(itemGetter.getOrThrow(ItemTags.create(Identifier.parse(maybeTag.substring(1))))) :
				Ingredient.of(BuiltInRegistries.ITEM.getValue(Identifier.parse(maybeTag)));
	}
	
	public static Ingredient ingredient(HolderLookup.Provider registries, String maybeTag)
	{
		return ingredient(items(registries), maybeTag);
	}
	
	public static Ingredient ingredient(Identifier... itemIds)
	{
		return Ingredient.of(Stream.of(itemIds).map(BuiltInRegistries.ITEM::getValue).toArray(Item[]::new));
	}
}
