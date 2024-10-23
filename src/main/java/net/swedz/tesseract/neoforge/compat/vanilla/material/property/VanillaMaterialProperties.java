package net.swedz.tesseract.neoforge.compat.vanilla.material.property;

import net.swedz.tesseract.neoforge.compat.vanilla.material.part.VanillaMaterialParts;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;

public interface VanillaMaterialProperties
{
	MaterialProperty<OreDrops> ORE_DROP_PART = new MaterialProperty<>("ore_drop_part", new OreDrops(VanillaMaterialParts.RAW_METAL));
}
