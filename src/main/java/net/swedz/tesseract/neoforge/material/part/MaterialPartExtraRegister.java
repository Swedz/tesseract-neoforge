package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;

public interface MaterialPartExtraRegister<T>
{
	void apply(MaterialRegistry registry, Material material, T holder);
}
