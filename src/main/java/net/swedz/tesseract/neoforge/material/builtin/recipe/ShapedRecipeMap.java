package net.swedz.tesseract.neoforge.material.builtin.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapedRecipeBuilder;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class ShapedRecipeMap
{
	private final Material material;
	
	private final String[]                            pattern;
	private final List<Consumer<ShapedRecipeBuilder>> actions       = Lists.newArrayList();
	private final Set<MaterialPart>                   involvedParts = Sets.newHashSet();
	
	public ShapedRecipeMap(Material material, String... pattern)
	{
		this.material = material;
		this.pattern = pattern;
	}
	
	public String[] getPattern()
	{
		return Arrays.copyOf(pattern, pattern.length);
	}
	
	public Set<MaterialPart> getInvolvedParts()
	{
		return Collections.unmodifiableSet(involvedParts);
	}
	
	public ShapedRecipeMap add(char key, Ingredient ingredient)
	{
		actions.add((r) -> r.define(key, ingredient));
		return this;
	}
	
	public ShapedRecipeMap add(char key, String maybeTag)
	{
		actions.add((r) -> r.define(key, maybeTag));
		return this;
	}
	
	public ShapedRecipeMap add(char key, ItemLike item)
	{
		actions.add((r) -> r.define(key, item));
		return this;
	}
	
	public ShapedRecipeMap add(char key, TagKey<Item> tag)
	{
		actions.add((r) -> r.define(key, tag));
		return this;
	}
	
	public ShapedRecipeMap add(char key, MaterialPart part)
	{
		involvedParts.add(part);
		MaterialPropertyMap properties = material.properties(part);
		actions.add((r) -> r.define(key, material.get(part).itemReference()));
		return this;
	}
	
	public void apply(ShapedRecipeBuilder recipe)
	{
		for(String line : pattern)
		{
			recipe.pattern(line);
		}
		actions.forEach((action) -> action.accept(recipe));
	}
}
