package net.swedz.tesseract.neoforge.material.property;

import com.google.common.collect.Maps;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Map;

public final class MaterialPropertyMap
{
	private final Map<MaterialProperty<?>, Object> map = Maps.newHashMap();
	
	public MaterialPropertyMap copy()
	{
		MaterialPropertyMap copy = new MaterialPropertyMap();
		copy.putAll(this);
		return copy;
	}
	
	public <T> MaterialPropertyMap set(MaterialProperty<T> property, T value)
	{
		if(value == null || value.equals(property.defaultValue()))
		{
			map.remove(property);
		}
		else
		{
			map.put(property, value);
		}
		return this;
	}
	
	public <T> T get(MaterialProperty<T> property)
	{
		return (T) map.getOrDefault(property, property.defaultValue());
	}
	
	public void putAll(MaterialPropertyMap other)
	{
		map.putAll(other.map);
	}
	
	public void applyItemTags(ItemHolder<?> holder)
	{
		for(MaterialProperty<?> property : MaterialProperty.getProperties())
		{
			if(property instanceof MaterialPropertyTag.ItemOptional propertyTag)
			{
				this.get(propertyTag).ifPresent(holder::tag);
			}
		}
	}
	
	public void applyBlockTags(BlockHolder<?> holder)
	{
		for(MaterialProperty<?> property : MaterialProperty.getProperties())
		{
			if(property instanceof MaterialPropertyTag.BlockOptional propertyTag)
			{
				this.get(propertyTag).ifPresent(holder::tag);
			}
		}
	}
}
