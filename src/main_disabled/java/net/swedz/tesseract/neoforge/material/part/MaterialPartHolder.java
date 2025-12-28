package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;

import java.util.Collection;
import java.util.List;

public interface MaterialPartHolder
{
	boolean has(MaterialPart part);
	
	default boolean hasAny(Collection<MaterialPart> parts)
	{
		return parts.stream().anyMatch(this::has);
	}
	
	default boolean hasAny(MaterialPart... parts)
	{
		return this.hasAny(List.of(parts));
	}
	
	RegisteredMaterialPart get(MaterialPart part);
	
	interface Mutable extends MaterialPartHolder
	{
		Material add(MaterialPart part, RegisteredMaterialPart registered);
	}
}
