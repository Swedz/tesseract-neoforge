package net.swedz.tesseract.neoforge.material;

import net.minecraft.resources.ResourceLocation;

public interface MaterialFactory
{
	Material create(ResourceLocation id, String englishName);
}
