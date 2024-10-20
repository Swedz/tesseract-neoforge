package net.swedz.tesseract.neoforge.compat.mi.material;

import aztech.modern_industrialization.MI;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.WrappedMaterial;

final class MIMaterial extends WrappedMaterial<MIMaterial>
{
	private MIMaterial(String id, String englishName)
	{
		super(id, englishName);
	}
	
	private MIMaterial(Material other)
	{
		super(other);
	}
	
	public static MIMaterial create(String id, String englishName)
	{
		return new MIMaterial(id, englishName);
	}
	
	public static MIMaterial defer(Material other)
	{
		return new MIMaterial(other);
	}
	
	@Override
	protected String modId()
	{
		return MI.ID;
	}
}
