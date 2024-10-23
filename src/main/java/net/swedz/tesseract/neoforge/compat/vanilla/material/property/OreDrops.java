package net.swedz.tesseract.neoforge.compat.vanilla.material.property;

import net.minecraft.util.valueproviders.UniformInt;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;

public record OreDrops(MaterialPart drop, UniformInt experience)
{
	public OreDrops(MaterialPart drop)
	{
		this(drop, UniformInt.of(0, 0));
	}
}
