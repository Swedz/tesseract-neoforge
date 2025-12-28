package net.swedz.tesseract.neoforge.compat.mi.material.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.swedz.tesseract.neoforge.compat.mi.recipe.MIForgeHammerRecipeBuilder;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeContext;

public class MIForgeHammerMaterialRecipeContext extends MaterialRecipeContext
{
	public MIForgeHammerMaterialRecipeContext(MaterialRegistry registry, Material material, RecipeOutput recipes)
	{
		super(registry, material, recipes);
	}
	
	public MIForgeHammerMaterialRecipeContext hammer(MaterialPart input, int inputCount, MaterialPart output, int outputCount, int cost)
	{
		if(this.has(input, output))
		{
			Item inputItem = material.get(input).asItem();
			Item outputItem = material.get(output).asItem();
			
			new MIForgeHammerRecipeBuilder()
					.input(Ingredient.of(inputItem), inputCount)
					.output(outputItem, outputCount)
					.hammerDamage(cost)
					.offerTo(recipes, this.id("forge_hammer/%s_to_%s".formatted(input.id().getPath(), output.id().getPath()) + (cost == 0 ? "" : "_with_tool")));
		}
		return this;
	}
	
	public MIForgeHammerMaterialRecipeContext hammer(MaterialPart input, int inputCount, MaterialPart output, int outputCount)
	{
		return this.hammer(input, inputCount, output, outputCount, 0);
	}
}
