package net.swedz.tesseract.neoforge.compat.vanilla.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.helper.RecipeHelper;
import net.swedz.tesseract.neoforge.recipe.RecipeBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ShapedRecipeBuilder extends RecipeBuilder
{
	private final Map<Character, Ingredient> key     = Maps.newHashMap();
	private final List<String>               pattern = Lists.newArrayList();
	
	public Map<Character, Ingredient> key()
	{
		return Collections.unmodifiableMap(key);
	}
	
	public List<String> pattern()
	{
		return Collections.unmodifiableList(pattern);
	}
	
	public ShapedRecipeBuilder define(char key, Ingredient ingredient)
	{
		if(ingredient == null || ingredient == Ingredient.EMPTY)
		{
			throw new NullPointerException("Input ingredient cannot be null or empty");
		}
		if(this.key.put(key, ingredient) != null)
		{
			throw new IllegalStateException("Key mapping is already registered: " + key);
		}
		return this;
	}
	
	public ShapedRecipeBuilder define(char key, ItemLike... items)
	{
		return this.define(key, Ingredient.of(items));
	}
	
	public ShapedRecipeBuilder define(char key, ItemStack... stacks)
	{
		return this.define(key, Ingredient.of(stacks));
	}
	
	public ShapedRecipeBuilder define(char key, TagKey<Item> tag)
	{
		return this.define(key, Ingredient.of(tag));
	}
	
	public ShapedRecipeBuilder define(char key, String maybeTag)
	{
		return this.define(key, RecipeHelper.ingredient(maybeTag));
	}
	
	public ShapedRecipeBuilder pattern(String line)
	{
		if(line == null)
		{
			throw new NullPointerException("Pattern cannot be null");
		}
		pattern.add(line);
		return this;
	}
	
	@Override
	public void validate()
	{
		if(pattern.isEmpty() || pattern.size() > 3)
		{
			throw new IllegalArgumentException("Invalid length " + pattern.size());
		}
		for(String string : pattern)
		{
			if(string.length() != pattern.getFirst().length())
			{
				throw new IllegalArgumentException("Pattern length mismatch: " + string.length() + ", expected " + pattern.getFirst().length());
			}
		}
		for(String string : pattern)
		{
			for(int i = 0; i < string.length(); ++i)
			{
				if(string.charAt(i) != ' ' && !key.containsKey(string.charAt(i)))
				{
					throw new IllegalArgumentException("Key " + string.charAt(i) + " is missing a mapping.");
				}
			}
		}
		for(char c : key.keySet())
		{
			boolean ok = false;
			for(String string : pattern)
			{
				for(int i = 0; i < string.length(); ++i)
				{
					if(string.charAt(i) == c)
					{
						ok = true;
						break;
					}
				}
			}
			if(!ok)
			{
				throw new IllegalArgumentException("Key mapping '" + c + "' is not used in the pattern.");
			}
		}
	}
	
	@Override
	public Recipe<?> convert()
	{
		return new ShapedRecipe(
				"",
				CraftingBookCategory.MISC,
				ShapedRecipePattern.of(key, pattern),
				result
		);
	}
	
	//<editor-fold desc="Inherited...">
	@Override
	public ShapedRecipeBuilder output(ItemLike result, int count)
	{
		super.output(result, count);
		return this;
	}
	
	@Override
	public ShapedRecipeBuilder output(String result, int count)
	{
		super.output(result, count);
		return this;
	}
	//</editor-fold>
}
