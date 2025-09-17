package net.swedz.tesseract.neoforge.material.builtin.recipe;

import com.google.common.collect.Sets;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapedRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapelessRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.SmeltingRecipeBuilder;
import net.swedz.tesseract.neoforge.helper.RecipeHelper;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeContext;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class VanillaMaterialRecipeContext extends MaterialRecipeContext
{
	public VanillaMaterialRecipeContext(MaterialRegistry registry, Material material, RecipeOutput recipes)
	{
		super(registry, material, recipes);
	}
	
	public <B extends ShapelessRecipeBuilder> VanillaMaterialRecipeContext shapeless(Supplier<B> start, MaterialPart input, int inputCount, MaterialPart output, int outputCount, Consumer<B> builder)
	{
		if(this.has(input, output))
		{
			var inputItem = material.get(input).itemReference();
			var recipe = start.get();
			for(int __ = 0; __ < inputCount; __++)
			{
				recipe.with(RecipeHelper.ingredient(inputItem));
			}
			if(builder != null)
			{
				builder.accept(recipe);
			}
			recipe.output(material.get(output).asItem(), outputCount);
			recipe.offerTo(recipes, this.id("materials/%s/craft/%s".formatted(material.id().getPath(), output.id().getPath())));
		}
		return this;
	}
	
	public VanillaMaterialRecipeContext shapeless(MaterialPart input, int inputCount, MaterialPart output, int outputCount, Consumer<ShapelessRecipeBuilder> builder)
	{
		return this.shapeless(ShapelessRecipeBuilder::new, input, inputCount, output, outputCount, builder);
	}
	
	public VanillaMaterialRecipeContext shapeless(MaterialPart input, int inputCount, MaterialPart output, int outputCount)
	{
		return this.shapeless(input, inputCount, output, outputCount, null);
	}
	
	public VanillaMaterialRecipeContext shapeless(MaterialPart input, int inputCount, MaterialPart output, int outputCount, boolean inverse)
	{
		if(this.has(input, output))
		{
			Item inputItem = material.get(input).asItem();
			Item outputItem = material.get(output).asItem();
			
			var recipe = new ShapelessRecipeBuilder();
			for(int i = 0; i < inputCount; i++)
			{
				recipe.with(inputItem);
			}
			recipe.output(outputItem, outputCount);
			recipe.offerTo(recipes, this.id("materials/%s/craft/%s_from_%s".formatted(material.id().getPath(), output.id().getPath(), input.id().getPath())));
			
			if(inverse)
			{
				this.shapeless(output, outputCount, input, inputCount, false);
			}
		}
		return this;
	}
	
	public VanillaMaterialRecipeContext compacting(MaterialPart input, MaterialPart output, boolean inverse)
	{
		if(this.has(input, output))
		{
			this.shaped(
					output, 1,
					(r) -> r.add('#', material.get(input).asItem()),
					"###",
					"###",
					"###"
			);
			
			if(inverse)
			{
				this.shapeless(output, 1, input, 9, false);
			}
		}
		return this;
	}
	
	public <B extends ShapedRecipeBuilder> VanillaMaterialRecipeContext shaped(Supplier<B> start, MaterialPart output, int outputCount, Consumer<ShapedRecipeMap> keyMapAction, Consumer<B> builder, String... pattern)
	{
		ShapedRecipeMap keyMap = new ShapedRecipeMap(material, pattern);
		keyMapAction.accept(keyMap);
		Set<MaterialPart> parts = Sets.newHashSet(keyMap.getInvolvedParts());
		parts.add(output);
		if(this.has(parts.toArray(new MaterialPart[0])))
		{
			Item outputItem = material.get(output).asItem();
			
			String id = output.id().getPath();
			
			var recipe = start.get();
			recipe.output(outputItem, outputCount);
			if(builder != null)
			{
				builder.accept(recipe);
			}
			keyMap.apply(recipe);
			recipe.offerTo(recipes, this.id("materials/%s/craft/%s".formatted(material.id().getPath(), id)));
		}
		return this;
	}
	
	public VanillaMaterialRecipeContext shaped(MaterialPart output, int outputCount, Consumer<ShapedRecipeMap> keyMapAction, Consumer<ShapedRecipeBuilder> builder, String... pattern)
	{
		return this.shaped(ShapedRecipeBuilder::new, output, outputCount, keyMapAction, builder, pattern);
	}
	
	public VanillaMaterialRecipeContext shaped(MaterialPart output, int outputCount, Consumer<ShapedRecipeMap> keyMapAction, String... pattern)
	{
		return this.shaped(output, outputCount, keyMapAction, null, pattern);
	}
	
	public <B extends ShapedRecipeBuilder> VanillaMaterialRecipeContext shaped(Supplier<B> start, MaterialPart output, int outputCount, Consumer<ShapedRecipeMap> keyMapAction, String... pattern)
	{
		return this.shaped(start, output, outputCount, keyMapAction, null, pattern);
	}
	
	public VanillaMaterialRecipeContext smelting(MaterialPart input, MaterialPart output, boolean blasting, float experience)
	{
		if(this.has(input, output))
		{
			new SmeltingRecipeBuilder()
					.input(Ingredient.of(material.get(output).asItem()))
					.output(material.get(input).asItem(), 1)
					.cookingTime(blasting ? 100 : 200)
					.experience(experience)
					.offerTo(recipes, this.id("materials/%s/smelting/%s_to_%s_%s".formatted(material.id().getPath(), input.id().getPath(), output.id().getPath(), blasting ? "blasting" : "smelting")));
		}
		return this;
	}
	
	public VanillaMaterialRecipeContext smelting(MaterialPart input, MaterialPart output, float experience)
	{
		return this.smelting(input, output, false, experience);
	}
	
	public VanillaMaterialRecipeContext blasting(MaterialPart input, MaterialPart output, float experience)
	{
		return this.smelting(input, output, true, experience);
	}
	
	public VanillaMaterialRecipeContext smeltingAndBlasting(MaterialPart input, MaterialPart output, float experience)
	{
		smelting(input, output, experience);
		blasting(input, output, experience);
		return this;
	}
}
