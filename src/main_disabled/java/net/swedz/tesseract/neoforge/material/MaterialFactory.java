package net.swedz.tesseract.neoforge.material;

import net.minecraft.resources.Identifier;

public interface MaterialFactory
{
	Material create(Identifier id, String englishName);
}
