package net.swedz.tesseract.neoforge.material.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.MaterialPartHolder;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;

import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public class MaterialRecipeContext implements MaterialPropertyHolder, MaterialPartHolder
{
	protected final MaterialRegistry registry;
	protected final Material         material;
	protected final RecipeOutput     recipes;
	
	public MaterialRecipeContext(MaterialRegistry registry, Material material, RecipeOutput recipes)
	{
		this.registry = registry;
		this.material = material;
		this.recipes = recipes;
	}
	
	public MaterialRegistry registry()
	{
		return registry;
	}
	
	public Material material()
	{
		return material;
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return material.has(property);
	}
	
	@Override
	public <T> T get(MaterialProperty<T> property)
	{
		return material.get(property);
	}
	
	public MaterialPart mainPart()
	{
		return this.get(MAIN_PART);
	}
	
	@Override
	public boolean has(MaterialPart part)
	{
		return material.has(part);
	}
	
	@Override
	public RegisteredMaterialPart get(MaterialPart part)
	{
		return material.get(part);
	}
	
	public RecipeOutput recipes()
	{
		return recipes;
	}
	
	public ResourceLocation id(String path)
	{
		return registry.id(path);
	}
	
	public boolean has(MaterialPart... parts)
	{
		boolean has = false;
		for(MaterialPart part : parts)
		{
			if(!material.has(part))
			{
				return false;
			}
			if(!has && registry.includes(material, part))
			{
				has = true;
			}
		}
		return has;
	}
}
