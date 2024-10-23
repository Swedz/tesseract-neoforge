package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;

import java.util.Optional;

public record MaterialPartRegisterContext(
		MaterialRegistry registry, Material material, MaterialPart part, MaterialPropertyMap properties
) implements MaterialPropertyHolder
{
	public MaterialPartRegisterContext(MaterialRegistry registry, Material material, MaterialPart part)
	{
		this(registry, material, part, material.properties(part));
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return properties.has(property);
	}
	
	@Override
	public <T> MaterialPropertyHolder set(MaterialProperty<T> property, T value)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T> MaterialPropertyHolder setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T> T get(MaterialProperty<T> property)
	{
		return properties.get(property);
	}
}
