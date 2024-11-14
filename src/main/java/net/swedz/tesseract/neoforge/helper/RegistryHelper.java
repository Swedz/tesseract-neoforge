package net.swedz.tesseract.neoforge.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

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
		return registry.getHolderOrThrow(resourceKey(registry, value));
	}
}
