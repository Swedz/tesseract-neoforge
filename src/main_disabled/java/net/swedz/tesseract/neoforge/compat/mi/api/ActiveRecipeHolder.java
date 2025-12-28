package net.swedz.tesseract.neoforge.compat.mi.api;

public interface ActiveRecipeHolder<R>
{
	boolean hasActiveRecipe();
	
	R getActiveRecipe();
	
	long getRecipeEuCost(R recipe);
	
	long getRecipeTotalEuCost(R recipe);
	
	boolean doConditionsMatchForRecipe(R recipe);
}
