package net.swedz.tesseract.neoforge.recipe;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public interface RecipeOfferable
{
	void validate();
	
	Recipe<?> convert();
	
	default void offerTo(RecipeOutput recipeOutput, ResourceLocation location, AdvancementHolder advancement)
	{
		this.validate();
		recipeOutput.accept(location, this.convert(), advancement);
	}
	
	default void offerTo(RecipeOutput recipeOutput, ResourceLocation location)
	{
		this.offerTo(recipeOutput, location, null);
	}
}
