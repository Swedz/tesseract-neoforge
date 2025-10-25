package net.swedz.tesseract.neoforge.compat.mi;

import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.network.chat.MutableComponent;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformer;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;
import net.swedz.tesseract.neoforge.lang.annotation.WithStyle;

public interface TesseractMIText
{
	record TieredMachineRecipeType(boolean electric, MachineRecipeType recipeType)
	{
	}
	
	@LangKey(
			key = "mi.machine_batcher.recipe_type",
			text = "Can run %s recipes in batches."
	)
	@WithStyle("tooltip")
	@Deprecated
	MutableComponent machineBatcherRecipe(TieredMachineRecipeType recipeType);
	
	default MutableComponent machineBatcherRecipe(boolean electric, MachineRecipeType recipeType)
	{
		return this.machineBatcherRecipe(new TieredMachineRecipeType(electric, recipeType));
	}
	
	@LangKey(
			key = "mi.machine_batcher.size_and_cost",
			text = "Runs in batches of up to %d at %s the EU cost."
	)
	@WithStyle("tooltip")
	MutableComponent machineBatcherSizeAndCost(int maxMultiplier, EuCostTransformer euCostTransformer);
}
