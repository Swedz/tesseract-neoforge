package net.swedz.tesseract.neoforge.material.builtin.property;

import net.minecraft.util.valueproviders.UniformInt;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;

public record OrePartDrops(MaterialPart drop, UniformInt drops, UniformInt experience)
{
	public static OrePartDrops of(MaterialPart drop, UniformInt drops, UniformInt experience)
	{
		return new OrePartDrops(drop, drops, experience);
	}
	
	public static OrePartDrops drops(MaterialPart drop, UniformInt drops)
	{
		return of(drop, drops, UniformInt.of(0, 0));
	}
	
	public static OrePartDrops experience(MaterialPart drop, UniformInt experience)
	{
		return of(drop, UniformInt.of(1, 1), experience);
	}
	
	public static OrePartDrops of(MaterialPart drop)
	{
		return drops(drop, UniformInt.of(1, 1));
	}
}
