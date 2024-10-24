package net.swedz.tesseract.neoforge.material.builtin.recipe;

import com.google.common.collect.Lists;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapedRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapelessRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.SmeltingRecipeBuilder;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeContext;

import java.util.List;
import java.util.function.Consumer;

public class VanillaMaterialRecipeContext extends MaterialRecipeContext
{
	public VanillaMaterialRecipeContext(MaterialRegistry registry, Material material, RecipeOutput recipes)
	{
		super(registry, material, recipes);
	}
	
	public void shapeless(MaterialPart input, int inputCount, MaterialPart output, int outputCount, boolean inverse)
	{
		if(this.has(input, output))
		{
			Item inputItem = material.getOrThrow(input).asItem();
			Item outputItem = material.getOrThrow(output).asItem();
			
			ShapelessRecipeBuilder recipe = new ShapelessRecipeBuilder();
			for(int i = 0; i < inputCount; i++)
			{
				recipe.with(inputItem);
			}
			recipe.output(outputItem, outputCount);
			recipe.offerTo(recipes, this.id("craft/%s_from_%s".formatted(output.id().getPath(), input.id().getPath())));
			
			if(inverse)
			{
				this.shapeless(output, outputCount, input, inputCount, false);
			}
		}
	}
	
	public void shapeless3x3(MaterialPart input, MaterialPart output, boolean inverse)
	{
		this.shapeless(input, 9, output, 1, inverse);
	}
	
	public final class ShapedRecipeMap
	{
		private final String[]                            pattern;
		private final List<Consumer<ShapedRecipeBuilder>> actions       = Lists.newArrayList();
		private final List<MaterialPart>                  involvedParts = Lists.newArrayList();
		
		public ShapedRecipeMap(String... pattern)
		{
			this.pattern = pattern;
		}
		
		public ShapedRecipeMap add(char key, ItemLike item)
		{
			actions.add((r) -> r.define(key, item));
			return this;
		}
		
		public ShapedRecipeMap add(char key, MaterialPart part)
		{
			involvedParts.add(part);
			// TODO use a tag if the part has a "primary tag"
			return this.add(key, material.get(part).asItem());
		}
		
		private void apply(ShapedRecipeBuilder recipe)
		{
			for(String line : pattern)
			{
				recipe.pattern(line);
			}
			actions.forEach((action) -> action.accept(recipe));
		}
	}
	
	public void shaped(MaterialPart output, int outputCount, Consumer<ShapedRecipeMap> keyMapAction, String... pattern)
	{
		ShapedRecipeMap keyMap = new ShapedRecipeMap(pattern);
		keyMapAction.accept(keyMap);
		List<MaterialPart> parts = Lists.newArrayList(keyMap.involvedParts);
		parts.add(output);
		if(this.has(parts.toArray(new MaterialPart[0])))
		{
			Item outputItem = material.getOrThrow(output).asItem();
			
			String id = output.id().getPath();
			
			ShapedRecipeBuilder recipe = new ShapedRecipeBuilder()
					.output(outputItem, outputCount);
			keyMap.apply(recipe);
			recipe.offerTo(recipes, this.id("craft/%s".formatted(id)));
		}
	}
	
	public void smelting(MaterialPart input, MaterialPart output, boolean blasting, float experience)
	{
		if(this.has(input, output))
		{
			new SmeltingRecipeBuilder()
					.input(Ingredient.of(material.getOrThrow(output).asItem()))
					.output(material.getOrThrow(input).asItem(), 1)
					.cookingTime(blasting ? 100 : 200)
					.experience(experience)
					.offerTo(recipes, this.id("smelting/%s_to_%s_%s".formatted(input.id().getPath(), output.id().getPath(), blasting ? "blasting" : "smelting")));
		}
	}
	
	public void smelting(MaterialPart input, MaterialPart output, float experience)
	{
		this.smelting(input, output, false, experience);
	}
	
	public void blasting(MaterialPart input, MaterialPart output, float experience)
	{
		this.smelting(input, output, true, experience);
	}
	
	public void smeltingAndBlasting(MaterialPart input, MaterialPart output, float experience)
	{
		smelting(input, output, experience);
		blasting(input, output, experience);
	}
}
