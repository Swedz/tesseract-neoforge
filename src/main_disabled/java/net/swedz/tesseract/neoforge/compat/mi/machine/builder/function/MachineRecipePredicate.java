package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import aztech.modern_industrialization.machines.recipe.MachineRecipe;

@FunctionalInterface
public interface MachineRecipePredicate
{
	/**
	 * Check if the recipe should be applicable to the recipe category.
	 *
	 * @param recipe the {@link MachineRecipe}
	 * @return true if the recipe should be applicable, false otherwise
	 */
	boolean test(MachineRecipe recipe);
}
