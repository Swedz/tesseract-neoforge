package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;

public final class MachineRecipeHelper
{
	public static <T extends MachineProcessCondition> T getRecipeCondition(MachineRecipe recipe, Class<T> conditionType)
	{
		return recipe.conditions.stream()
				.filter((c) -> conditionType.isAssignableFrom(c.getClass()))
				.map((c) -> (T) c)
				.findFirst()
				.orElse(null);
	}
}
