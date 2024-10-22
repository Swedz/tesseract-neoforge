package net.swedz.tesseract.neoforge.material.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;

import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public class MaterialRecipeContext
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
	
	public RecipeOutput recipes()
	{
		return recipes;
	}
	
	public ResourceLocation id(String path)
	{
		return registry.id(path);
	}
	
	public RegisteredMaterialPart getPart(MaterialPart part)
	{
		return material.get(part);
	}
	
	public MaterialPart mainPart()
	{
		return material.get(MAIN_PART);
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
