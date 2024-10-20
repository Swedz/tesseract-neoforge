package net.swedz.tesseract.neoforge.material;

final class VanillaMaterial extends WrappedMaterial<VanillaMaterial>
{
	private VanillaMaterial(String id, String englishName)
	{
		super(id, englishName);
	}
	
	private VanillaMaterial(Material other)
	{
		super(other);
	}
	
	public static VanillaMaterial create(String id, String englishName)
	{
		return new VanillaMaterial(id, englishName);
	}
	
	public static VanillaMaterial defer(Material other)
	{
		return new VanillaMaterial(other);
	}
	
	@Override
	protected String modId()
	{
		return "minecraft";
	}
}
