package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.resources.Identifier;

public interface MaterialPartFactory
{
	MaterialPart create(Identifier id, String englishName);
}
