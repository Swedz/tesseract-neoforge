package net.swedz.tesseract.neoforge.helper;

import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public final class TagHelper
{
	public static TagKey<Item> item(ResourceLocation location)
	{
		return TagKey.create(BuiltInRegistries.ITEM.key(), location);
	}
	
	public static TagKey<Item> itemCommon(String path)
	{
		return item(ResourceLocation.fromNamespaceAndPath("c", path));
	}
	
	public static List<TagKey<Item>> itemCommonWithChild(String path, String child)
	{
		return Lists.newArrayList(itemCommon(path), itemCommon("%s/%s".formatted(path, child)));
	}
	
	public static TagKey<Block> block(ResourceLocation location)
	{
		return TagKey.create(BuiltInRegistries.BLOCK.key(), location);
	}
	
	public static TagKey<Block> blockCommon(String path)
	{
		return block(ResourceLocation.fromNamespaceAndPath("c", path));
	}
	
	public static List<TagKey<Block>> blockCommonWithChild(String path, String child)
	{
		return Lists.newArrayList(blockCommon(path), blockCommon("%s/%s".formatted(path, child)));
	}
	
	public static <F, T> TagKey<T> convert(TagKey<F> from, ResourceKey<? extends Registry<T>> to)
	{
		return TagKey.create(to, from.location());
	}
	
	public static <F, T> TagKey<T> convert(TagKey<F> from, Registry<T> to)
	{
		return convert(from, to.key());
	}
}
