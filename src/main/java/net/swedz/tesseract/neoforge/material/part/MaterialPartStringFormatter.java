package net.swedz.tesseract.neoforge.material.part;

public interface MaterialPartStringFormatter
{
	String format(String material, String part);
	
	interface OnlyMaterial extends MaterialPartStringFormatter
	{
		String format(String material);
		
		@Override
		default String format(String material, String part)
		{
			return this.format(material);
		}
	}
}
