package net.swedz.tesseract.neoforge.material.part;

import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;

public interface MaterialPartItemReferenceFormatter
{
	String format(MaterialRegistry registry, Material material, MaterialPart part);
	
	interface OnlyMaterial extends MaterialPartItemReferenceFormatter
	{
		String format(MaterialRegistry registry, Material material);
		
		@Override
		default String format(MaterialRegistry registry, Material material, MaterialPart part)
		{
			return this.format(registry, material);
		}
	}
	
	static MaterialPartItemReferenceFormatter tag()
	{
		return (registry, material, part) -> "#c:%ss/%s".formatted(part.id().getPath(), material.id().getPath());
	}
	
	static MaterialPartItemReferenceFormatter tag(String format)
	{
		return (registry, material, part) -> format.contains("%s") ? "#c:%s".formatted(format.formatted(material.id().getPath())) : format;
	}
}
