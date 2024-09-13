package net.swedz.tesseract.neoforge.helper;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public final class TagHelper
{
	public static <F, T> TagKey<T> convert(TagKey<F> from, ResourceKey<? extends Registry<T>> to)
	{
		return TagKey.create(to, from.location());
	}
	
	public static <F, T> TagKey<T> convert(TagKey<F> from, Registry<T> to)
	{
		return convert(from, to.key());
	}
}
