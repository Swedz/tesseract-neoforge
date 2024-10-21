package net.swedz.tesseract.neoforge.material;

import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;

import java.util.Optional;

public class ImmutableMaterial extends Material
{
	public ImmutableMaterial(ResourceLocation id, String englishName)
	{
		super(id, englishName);
	}
	
	@Override
	public <T> Material set(MaterialProperty<T> property, T value)
	{
		return this.copy().set(property, value);
	}
	
	@Override
	public <T> Material setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		return this.copy().setOptional(property, value);
	}
	
	@Override
	public Material add(MaterialPart part, RegisteredMaterialPart registered)
	{
		return this.copy().add(part, registered);
	}
	
	@Override
	public Material addNative(MaterialPart part)
	{
		return this.copy().addNative(part);
	}
	
	@Override
	public Material addNative(MaterialPart... parts)
	{
		return this.copy().addNative(parts);
	}
}
