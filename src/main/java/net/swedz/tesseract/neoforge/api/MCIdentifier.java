package net.swedz.tesseract.neoforge.api;

import net.minecraft.resources.Identifier;

public record MCIdentifier(Identifier location, String englishName)
{
	public String modId()
	{
		return location.getNamespace();
	}
	
	public String id()
	{
		return location.getPath();
	}
}
