package net.swedz.tesseract.neoforge.material.property;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public final class MaterialProperty<T>
{
	private static final Map<ResourceLocation, MaterialProperty<?>> PROPERTY_IDS = Maps.newHashMap();
	private static final Collection<MaterialProperty<?>>            PROPERTIES   = Collections.unmodifiableCollection(PROPERTY_IDS.values());
	
	public static Collection<MaterialProperty<?>> getProperties()
	{
		return PROPERTIES;
	}
	
	private final ResourceLocation key;
	private final T                defaultValue;
	
	private final Set<BiConsumer<ItemHolder<?>, T>>  itemActions  = Sets.newHashSet();
	private final Set<BiConsumer<BlockHolder<?>, T>> blockActions = Sets.newHashSet();
	
	public MaterialProperty(ResourceLocation key, T defaultValue)
	{
		this.key = key;
		this.defaultValue = defaultValue;
		
		if(PROPERTY_IDS.put(key, this) != null)
		{
			throw new IllegalArgumentException("Duplicate material property key '%s'".formatted(key));
		}
	}
	
	public ResourceLocation key()
	{
		return key;
	}
	
	public T defaultValue()
	{
		return defaultValue;
	}
	
	public MaterialProperty<T> item(BiConsumer<ItemHolder<?>, T> action)
	{
		itemActions.add(action);
		return this;
	}
	
	public MaterialProperty<T> block(BiConsumer<BlockHolder<?>, T> action)
	{
		blockActions.add(action);
		return this;
	}
	
	public void applyItem(ItemHolder<?> holder, T value)
	{
		itemActions.forEach((a) -> a.accept(holder, value));
	}
	
	public void applyBlock(BlockHolder<?> holder, T value)
	{
		blockActions.forEach((a) -> a.accept(holder, value));
	}
}
