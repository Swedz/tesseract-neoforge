package net.swedz.tesseract.neoforge.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.stream.Stream;

public final class RegistryHelper
{
	/**
	 * Get the {@link ResourceKey} for a given entry in a registry.
	 *
	 * @param registry the registry
	 * @param value    the entry
	 * @param <T>      the type the registry holds
	 * @return the {@link ResourceKey}
	 */
	public static <T> ResourceKey<T> resourceKey(Registry<T> registry, T value)
	{
		return registry.getResourceKey(value).orElseThrow();
	}
	
	/**
	 * Get the {@link Holder.Reference} for a given entry in a registry.
	 *
	 * @param registry the registry
	 * @param value    the entry
	 * @param <T>      the type the registry holds
	 * @return the {@link Holder.Reference}
	 */
	public static <T> Holder.Reference<T> holder(Registry<T> registry, T value)
	{
		return registry.getOrThrow(resourceKey(registry, value));
	}
	
	/**
	 * Get the registered {@link Holder}s that are tagged with the {@link TagKey}.
	 *
	 * @param registryAccess the registry access
	 * @param tag the tag
	 * @param <T> the type the registry holds
	 * @return a stream of {@link Holder}s that are tagged with the {@link TagKey}
	 */
	public static <T> Stream<Holder<T>> values(RegistryAccess registryAccess, TagKey<T> tag)
	{
		var registry = registryAccess.getOrThrow(tag.registry());
		return registry.value().getOrThrow(tag).stream();
	}
}
