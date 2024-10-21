package net.swedz.tesseract.neoforge.material.part;

public interface MaterialPartFormatter
{
	String format(String material, String part);
	
	interface OnlyMaterial extends MaterialPartFormatter
	{
		String format(String material);
		
		@Override
		default String format(String material, String part)
		{
			return this.format(material);
		}
	}
}
