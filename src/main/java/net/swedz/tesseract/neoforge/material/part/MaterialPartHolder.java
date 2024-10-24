package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;

public interface MaterialPartHolder
{
	boolean has(MaterialPart part);
	
	RegisteredMaterialPart get(MaterialPart part);
	
	interface Mutable extends MaterialPartHolder
	{
		Material add(MaterialPart part, RegisteredMaterialPart registered);
	}
}
