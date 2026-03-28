package net.swedz.tesseract.neoforge.material.recipe;

import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class MaterialRecipeGroup<C extends MaterialRecipeContext>
{
	protected final MaterialRecipeGroup<?>                parent;
	protected final MaterialRecipeContextFactory<C>       contextFactory;
	protected final Map<String, MaterialRecipeCreator<C>> creators;
	
	protected MaterialRecipeGroup(MaterialRecipeGroup<?> parent, MaterialRecipeContextFactory<C> contextFactory, Map<String, MaterialRecipeCreator<C>> creators)
	{
		this.parent = parent;
		this.contextFactory = contextFactory;
		this.creators = Maps.newHashMap(creators);
	}
	
	public static <C extends MaterialRecipeContext> MaterialRecipeGroup<C> create(MaterialRecipeContextFactory<C> contextFactory)
	{
		return new MaterialRecipeGroup<>(null, contextFactory, Maps.newHashMap());
	}
	
	public static MaterialRecipeGroup<MaterialRecipeContext> create()
	{
		return create(MaterialRecipeContext::new);
	}
	
	public MaterialRecipeGroup<C> copy()
	{
		return new MaterialRecipeGroup<>(parent, contextFactory, creators);
	}
	
	public MaterialRecipeGroup<C> add(String reference, MaterialRecipeCreator<C> creator)
	{
		MaterialRecipeGroup<C> copy = this.copy();
		if(copy.creators.put(reference, creator) != null)
		{
			throw new IllegalArgumentException("There is already a recipe creator with the reference '%s' on this group".formatted(reference));
		}
		return copy;
	}
	
	public MaterialRecipeGroup<C> filtered(Predicate<String> predicate)
	{
		MaterialRecipeGroup<C> filtered = new MaterialRecipeGroup<>(parent == null ? null : parent.filtered(predicate), contextFactory, Maps.newHashMap());
		for(var entry : creators.entrySet())
		{
			var reference = entry.getKey();
			var creator = entry.getValue();
			if(predicate.test(reference))
			{
				filtered = filtered.add(reference, creator);
			}
		}
		return filtered;
	}
	
	public MaterialRecipeGroup<C> only(Collection<String> references)
	{
		return this.filtered(references::contains);
	}
	
	public MaterialRecipeGroup<C> only(String... references)
	{
		return this.only(Arrays.asList(references));
	}
	
	public MaterialRecipeGroup<C> without(Collection<String> references)
	{
		return this.filtered((reference) -> !references.contains(reference));
	}
	
	public MaterialRecipeGroup<C> without(String... references)
	{
		return this.without(Arrays.asList(references));
	}
	
	public void create(MaterialRegistry registry, Material material, HolderLookup.Provider registries, RecipeOutput recipes)
	{
		if(parent != null)
		{
			parent.create(registry, material, registries, recipes);
		}
		C context = contextFactory.create(registry, material, registries, recipes);
		creators.forEach((reference, creator) -> creator.create(context));
	}
	
	public <T extends MaterialRecipeContext> MaterialRecipeGroup<T> then(MaterialRecipeContextFactory<T> contextFactory)
	{
		return new MaterialRecipeGroup<>(this, contextFactory, Maps.newHashMap());
	}
}
