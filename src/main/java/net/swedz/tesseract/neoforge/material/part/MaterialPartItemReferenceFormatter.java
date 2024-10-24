package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;

public interface MaterialPartItemReferenceFormatter
{
	String format(String namespace, Material material, MaterialPart part);
	
	interface OnlyMaterial extends MaterialPartItemReferenceFormatter
	{
		String format(String namespace, Material material);
		
		@Override
		default String format(String namespace, Material material, MaterialPart part)
		{
			return this.format(namespace, material);
		}
	}
	
	static MaterialPartItemReferenceFormatter tag()
	{
		return (namespace, material, part) -> "#c:%ss/%s".formatted(part.id().getPath(), material.id().getPath());
	}
	
	static MaterialPartItemReferenceFormatter tag(String format)
	{
		return (namespace, material, part) -> format.contains("%s") ? "#c:%s".formatted(format.formatted(material.id().getPath())) : format;
	}
}
