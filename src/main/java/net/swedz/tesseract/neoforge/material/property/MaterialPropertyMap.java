package net.swedz.tesseract.neoforge.material.property;

import com.google.common.collect.Maps;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Map;
import java.util.Optional;

public final class MaterialPropertyMap implements MaterialPropertyHolder
{
	private final Map<MaterialProperty<?>, Object> map = Maps.newHashMap();
	
	public MaterialPropertyMap copy()
	{
		MaterialPropertyMap copy = new MaterialPropertyMap();
		copy.putAll(this);
		return copy;
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return map.containsKey(property);
	}
	
	@Override
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
	
	@Override
	public <T> MaterialPropertyMap setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		return this.set(property, Optional.ofNullable(value));
	}
	
	@Override
	public <T> T get(MaterialProperty<T> property)
	{
		return (T) map.getOrDefault(property, property.defaultValue());
	}
	
	public void putAll(MaterialPropertyMap other)
	{
		map.putAll(other.map);
	}
	
	public void apply(ItemHolder<?> holder)
	{
		for(MaterialProperty property : MaterialProperty.getProperties())
		{
			property.applyItem(holder, this.get(property));
		}
	}
	
	public void apply(BlockHolder<?> holder)
	{
		for(MaterialProperty property : MaterialProperty.getProperties())
		{
			property.applyBlock(holder, this.get(property));
		}
	}
}