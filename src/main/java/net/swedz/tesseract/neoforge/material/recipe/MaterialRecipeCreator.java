package net.swedz.tesseract.neoforge.material.recipe;

public interface MaterialRecipeCreator<C extends MaterialRecipeContext>
{
	void create(C context);
}
