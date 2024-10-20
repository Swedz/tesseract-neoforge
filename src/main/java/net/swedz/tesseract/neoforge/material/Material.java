package net.swedz.tesseract.neoforge.material;

import com.google.common.collect.Maps;
import net.minecraft.tags.TagKey;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyTag;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Material
{
	private static final Map<String, Material> MATERIAL_IDS = Maps.newHashMap();
	
	protected final String id, englishName;
	
	protected final MaterialPropertyMap properties;
	
	protected final Map<MaterialPart, RegisteredMaterialPart> parts;
	
	protected Material(String id, String englishName)
	{
		this.id = id;
		this.englishName = englishName;
		
		this.properties = new MaterialPropertyMap();
		
		this.parts = Maps.newHashMap();
		
		if(MATERIAL_IDS.put(id, this) != null)
		{
			throw new IllegalArgumentException("There is already an existing material with the id '%s'".formatted(id));
		}
	}
	
	protected Material(Material other)
	{
		this.id = other.id;
		this.englishName = other.englishName;
		this.properties = other.properties;
		this.parts = other.parts;
	}
	
	public static Material create(String id, String englishName)
	{
		return new Material(id, englishName);
	}
	
	public static Material defer(Material other)
	{
		return new Material(other);
	}
	
	public final String id()
	{
		return id;
	}
	
	public final String englishName()
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
	
	public Map<MaterialPart, RegisteredMaterialPart> parts()
	{
		return Collections.unmodifiableMap(parts);
	}
	
	public Material add(MaterialPart part, RegisteredMaterialPart registered)
	{
		parts.put(part, registered);
		return this;
	}
	
	public Material add(String modId, Collection<MaterialPart> parts)
	{
		for(MaterialPart part : parts)
		{
			if(this.parts.containsKey(part))
			{
				throw new IllegalStateException("Already added part '%s' to material '%s'".formatted(part.id(), id));
			}
		}
		for(MaterialPart part : parts)
		{
			this.parts.put(part, RegisteredMaterialPart.of(modId, part.id(this)));
		}
		return this;
	}
	
	public Material add(String modId, MaterialPart... parts)
	{
		return this.add(modId, Arrays.asList(parts));
	}
	
	public boolean has(MaterialPart part)
	{
		return parts.containsKey(part);
	}
	
	public RegisteredMaterialPart get(MaterialPart part)
	{
		RegisteredMaterialPart registered = parts.get(part);
		if(registered == null)
		{
			throw new IllegalArgumentException("No '%s' part registered on the material '%s'".formatted(part.id(), id));
		}
		return registered;
	}
	
	@Override
	public int hashCode()
	{
		return id.hashCode();
	}
}
