package net.swedz.tesseract.neoforge.material.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;

public interface MaterialRecipeCreator
{
	void create(Material material, MaterialPart part, RegisteredMaterialPart registeredPart, RecipeOutput recipes);
}
