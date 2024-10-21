package net.swedz.tesseract.neoforge.material.part.recipe;

import com.google.common.collect.Maps;
import net.minecraft.data.recipes.RecipeOutput;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public final class MaterialRecipeGroup implements MaterialRecipeCreator
{
	private final Map<String, MaterialRecipeCreator> creators;
	
	private MaterialRecipeGroup(Map<String, MaterialRecipeCreator> creators)
	{
		this.creators = Maps.newHashMap(creators);
	}
	
	public MaterialRecipeGroup()
	{
		this(Maps.newHashMap());
	}
	
	public MaterialRecipeGroup add(String reference, MaterialRecipeCreator creator)
	{
		MaterialRecipeGroup copy = new MaterialRecipeGroup(creators);
		copy.creators.put(reference, creator);
		return copy;
	}
	
	public MaterialRecipeGroup filtered(Predicate<String> predicate)
	{
		MaterialRecipeGroup filtered = new MaterialRecipeGroup();
		creators.forEach((reference, creator) ->
		{
			if(predicate.test(reference))
			{
				filtered.add(reference, creator);
			}
		});
		return filtered;
	}
	
	public MaterialRecipeGroup only(Collection<String> references)
	{
		return this.filtered(references::contains);
	}
	
	public MaterialRecipeGroup only(String... references)
	{
		return this.only(Arrays.asList(references));
	}
	
	public MaterialRecipeGroup excluding(Collection<String> references)
	{
		return this.filtered((reference) -> !references.contains(reference));
	}
	
	public MaterialRecipeGroup excluding(String... references)
	{
		return this.excluding(Arrays.asList(references));
	}
	
	@Override
	public void create(Material material, MaterialPart part, RegisteredMaterialPart registeredPart, RecipeOutput recipes)
	{
		creators.forEach((reference, creator) -> creator.create(material, part, registeredPart, recipes));
	}
}
