package net.swedz.tesseract.neoforge.material.builtin.property;

import net.minecraft.util.valueproviders.UniformInt;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;

public record OrePartDrops(MaterialPart drop, UniformInt experience)
{
	public OrePartDrops(MaterialPart drop)
	{
		this(drop, UniformInt.of(0, 0));
	}
}
