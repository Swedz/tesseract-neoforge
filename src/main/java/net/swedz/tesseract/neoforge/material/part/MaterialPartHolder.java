package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;

public interface MaterialPartHolder
{
	boolean has(MaterialPart part);
	
	RegisteredMaterialPart get(MaterialPart part);
	
	default RegisteredMaterialPart getOrThrow(MaterialPart part)
	{
		if(!this.has(part))
		{
			throw new IllegalArgumentException("Could not find part '%s'".formatted(part.id().toString()));
		}
		return this.get(part);
	}
	
	interface Mutable extends MaterialPartHolder
	{
		Material add(MaterialPart part, RegisteredMaterialPart registered);
	}
}
