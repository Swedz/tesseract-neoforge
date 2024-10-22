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
	
	public void full3x3(MaterialPart small, MaterialPart big, boolean inverse)
	{
		if(this.has(small, big))
		{
			Item smallItem = material.get(small).asItem();
			Item bigItem = material.get(big).asItem();
			
			ShapelessRecipeBuilder smallToBig = new ShapelessRecipeBuilder();
			for(int i = 0; i < 9; i++)
			{
				smallToBig.with(smallItem);
			}
			smallToBig.output(bigItem, 1);
			smallToBig.offerTo(recipes, this.id("craft/%s_from_%s".formatted(big.id().getPath(), small.id().getPath())));
			
			if(inverse)
			{
				new ShapelessRecipeBuilder()
						.with(bigItem)
						.output(smallItem, 9)
						.offerTo(recipes, this.id("craft/%s_from_%s".formatted(small.id().getPath(), big.id().getPath())));
			}
		}
	}
	
	public void smelting(MaterialPart inputPart, MaterialPart resultPart, boolean blasting)
	{
		if(this.has(inputPart, resultPart))
		{
			new SmeltingRecipeBuilder()
					.input(Ingredient.of(material.get(resultPart).asItem()))
					.output(material.get(inputPart).asItem(), 1)
					.cookingTime(blasting ? 100 : 200)
					.experience(0.7f)
					.offerTo(recipes, this.id("smelting/%s_to_%s_%s".formatted(inputPart.id().getPath(), resultPart.id().getPath(), blasting ? "blasting" : "smelting")));
		}
	}
	
	public void smeltingAndBlasting(MaterialPart inputPart, MaterialPart resultPart)
	{
		smelting(inputPart, resultPart, true);
		smelting(inputPart, resultPart, false);
	}
}
