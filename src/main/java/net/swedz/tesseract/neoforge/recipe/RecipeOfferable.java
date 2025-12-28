package net.swedz.tesseract.neoforge.recipe;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public interface RecipeOfferable extends RecipeConvertible
{
	void validate();
	
	default void offerTo(RecipeOutput recipeOutput, Identifier location, AdvancementHolder advancement)
	{
		this.validate();
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, location), this.convert(), advancement);
	}
	
	default void offerTo(RecipeOutput recipeOutput, Identifier location)
	{
		this.offerTo(recipeOutput, location, null);
	}
}
