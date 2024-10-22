package net.swedz.tesseract.neoforge.material.recipe;

import com.google.common.collect.Maps;

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
		if(copy.creators.put(reference, creator) != null)
		{
			throw new IllegalArgumentException("There is already a recipe creator with the reference '%s' on this group".formatted(reference));
		}
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
	public void create(MaterialRecipeContext context)
	{
		creators.forEach((reference, creator) -> creator.create(context));
	}
}
