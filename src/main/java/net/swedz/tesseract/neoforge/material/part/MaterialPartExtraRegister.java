package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;

public interface MaterialPartExtraRegister<T>
{
	void apply(MaterialRegistry registry, Material material, MaterialPropertyMap properties, T holder);
}
