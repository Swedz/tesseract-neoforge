package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;

public record MaterialPartRegisterContext(
		MaterialRegistry registry, Material material, MaterialPart part, MaterialPropertyMap properties
) implements MaterialPropertyHolder, MaterialPartHolder
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
	public <T> T get(MaterialProperty<T> property)
	{
		return properties.get(property);
	}
	
	@Override
	public boolean has(MaterialPart part)
	{
		return material.has(part);
	}
	
	@Override
	public RegisteredMaterialPart get(MaterialPart part)
	{
		return material.get(part);
	}
}
