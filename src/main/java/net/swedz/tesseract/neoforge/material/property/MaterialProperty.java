package net.swedz.tesseract.neoforge.material.property;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class MaterialProperty<T>
{
	private static final Map<String, MaterialProperty<?>> PROPERTY_IDS = Maps.newHashMap();
	private static final Collection<MaterialProperty<?>>  PROPERTIES   = Collections.unmodifiableCollection(PROPERTY_IDS.values());
	
	public static Collection<MaterialProperty<?>> getProperties()
	{
		return PROPERTIES;
	}
	
	private final T defaultValue;
	
	public MaterialProperty(String key, T defaultValue)
	{
		this.defaultValue = defaultValue;
		
		if(PROPERTY_IDS.put(key, this) != null)
		{
			throw new IllegalArgumentException("Duplicate material property key '%s'".formatted(key));
		}
	}
	
	public T defaultValue()
	{
		return defaultValue;
	}
}
