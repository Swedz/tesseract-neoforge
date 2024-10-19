package net.swedz.tesseract.neoforge.material;

import com.google.common.collect.Sets;
import net.minecraft.tags.TagKey;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyTag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public final class Material
{
	private final String id, englishName;
	
	private final MaterialPropertyMap properties = new MaterialPropertyMap();
	
	private final Set<MaterialPart> parts = Sets.newHashSet();
	
	private Material(String id, String englishName)
	{
		this.id = id;
		this.englishName = englishName;
	}
	
	public static Material create(String id, String englishName)
	{
		return new Material(id, englishName);
	}
	
	public String id()
	{
		return id;
	}
	
	public String englishName()
	{
		return englishName;
	}
	
	public MaterialPropertyMap properties()
	{
		return properties;
	}
	
	public <T> Material set(MaterialProperty<T> property, T value)
	{
		properties.set(property, value);
		return this;
	}
	
	public <T> Material set(MaterialPropertyTag<T> property, TagKey<T> value)
	{
		properties.set(property, Optional.of(value));
		return this;
	}
	
	public <T> T get(MaterialProperty<T> property)
	{
		return properties.get(property);
	}
	
	public Set<MaterialPart> parts()
	{
		return Collections.unmodifiableSet(parts);
	}
	
	public Material add(Collection<MaterialPart> parts)
	{
		for(MaterialPart part : parts)
		{
			if(this.parts.contains(part))
			{
				throw new IllegalStateException("Already added part '%s' to material '%s'".formatted(part.id(), id));
			}
		}
		this.parts.addAll(parts);
		return this;
	}
	
	public Material add(MaterialPart... parts)
	{
		return this.add(Arrays.asList(parts));
	}
	
	public boolean has(MaterialPart part)
	{
		return parts.contains(part);
	}
	
	public Material copy()
	{
		Material copy = new Material(id, englishName);
		copy.properties().putAll(properties);
		copy.add(parts);
		return copy;
	}
}
