package net.swedz.tesseract.neoforge.material.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;

public interface MaterialRecipeContextFactory<C extends MaterialRecipeContext>
{
	C create(MaterialRegistry registry, Material material, RecipeOutput recipes);
}
