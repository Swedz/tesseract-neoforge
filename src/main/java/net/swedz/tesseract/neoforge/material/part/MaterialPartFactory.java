package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.resources.ResourceLocation;

public interface MaterialPartFactory
{
	MaterialPart create(ResourceLocation id, String englishName);
}
