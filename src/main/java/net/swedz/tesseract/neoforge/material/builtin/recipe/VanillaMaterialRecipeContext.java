package net.swedz.tesseract.neoforge.material.builtin.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapelessRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.SmeltingRecipeBuilder;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeContext;

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
			Item resultItem = material.getOrThrow(output).asItem();
			
			ShapelessRecipeBuilder inputToResult = new ShapelessRecipeBuilder();
			for(int i = 0; i < inputCount; i++)
			{
				inputToResult.with(inputItem);
			}
			inputToResult.output(resultItem, outputCount);
			inputToResult.offerTo(recipes, this.id("craft/%s_from_%s".formatted(output.id().getPath(), input.id().getPath())));
			
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
	
	public void smelting(MaterialPart input, MaterialPart output, boolean blasting)
	{
		if(this.has(input, output))
		{
			new SmeltingRecipeBuilder()
					.input(Ingredient.of(material.getOrThrow(output).asItem()))
					.output(material.getOrThrow(input).asItem(), 1)
					.cookingTime(blasting ? 100 : 200)
					.experience(0.7f)
					.offerTo(recipes, this.id("smelting/%s_to_%s_%s".formatted(input.id().getPath(), output.id().getPath(), blasting ? "blasting" : "smelting")));
		}
	}
	
	public void smeltingAndBlasting(MaterialPart input, MaterialPart output)
	{
		smelting(input, output, true);
		smelting(input, output, false);
	}
}
