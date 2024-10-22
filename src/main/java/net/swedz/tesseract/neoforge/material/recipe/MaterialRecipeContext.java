package net.swedz.tesseract.neoforge.material.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.ShapelessRecipeBuilder;
import net.swedz.tesseract.neoforge.compat.vanilla.recipe.SmeltingRecipeBuilder;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;

import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public record MaterialRecipeContext(MaterialRegistry registry, Material material, RecipeOutput recipes)
{
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
	
	public void shapeless(MaterialPart input, int inputCount, MaterialPart result, int resultCount, boolean inverse)
	{
		if(this.has(input, result))
		{
			Item inputItem = material.get(input).asItem();
			Item resultItem = material.get(result).asItem();
			
			ShapelessRecipeBuilder inputToResult = new ShapelessRecipeBuilder();
			for(int i = 0; i < inputCount; i++)
			{
				inputToResult.with(inputItem);
			}
			inputToResult.output(resultItem, resultCount);
			inputToResult.offerTo(recipes, this.id("craft/%s_from_%s".formatted(result.id().getPath(), input.id().getPath())));
			
			if(inverse)
			{
				this.shapeless(result, resultCount, input, inputCount, false);
			}
		}
	}
	
	public void shapeless3x3(MaterialPart input, MaterialPart result, boolean inverse)
	{
		this.shapeless(input, 9, result, 1, inverse);
	}
	
	public void smelting(MaterialPart input, MaterialPart result, boolean blasting)
	{
		if(this.has(input, result))
		{
			new SmeltingRecipeBuilder()
					.input(Ingredient.of(material.get(result).asItem()))
					.output(material.get(input).asItem(), 1)
					.cookingTime(blasting ? 100 : 200)
					.experience(0.7f)
					.offerTo(recipes, this.id("smelting/%s_to_%s_%s".formatted(input.id().getPath(), result.id().getPath(), blasting ? "blasting" : "smelting")));
		}
	}
	
	public void smeltingAndBlasting(MaterialPart input, MaterialPart result)
	{
		smelting(input, result, true);
		smelting(input, result, false);
	}
}
