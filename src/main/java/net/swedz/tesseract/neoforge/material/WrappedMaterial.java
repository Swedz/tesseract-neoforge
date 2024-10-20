package net.swedz.tesseract.neoforge.material;

import net.minecraft.tags.TagKey;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyTag;

import java.util.Arrays;
import java.util.Collection;

public abstract class WrappedMaterial<M extends WrappedMaterial> extends Material
{
	protected WrappedMaterial(String id, String englishName)
	{
		super(id, englishName);
	}
	
	protected WrappedMaterial(Material other)
	{
		super(other);
	}
	
	protected abstract String modId();
	
	@Override
	public <T> M set(MaterialProperty<T> property, T value)
	{
		super.set(property, value);
		return (M) this;
	}
	
	@Override
	public <T> M set(MaterialPropertyTag<T> property, TagKey<T> value)
	{
		super.set(property, value);
		return (M) this;
	}
	
	@Override
	public M add(String modId, Collection<MaterialPart> parts)
	{
		super.add(modId, parts);
		return (M) this;
	}
	
	public M add(Collection<MaterialPart> parts)
	{
		return this.add(this.modId(), parts);
	}
	
	@Override
	public M add(String modId, MaterialPart... parts)
	{
		super.add(modId, parts);
		return (M) this;
	}
	
	public M add(MaterialPart... parts)
	{
		return this.add(Arrays.asList(parts));
	}
}
