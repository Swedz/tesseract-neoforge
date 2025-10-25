package net.swedz.tesseract.neoforge.compat.mi;

import aztech.modern_industrialization.MI;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformer;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import static aztech.modern_industrialization.MITooltips.*;

public final class MIParser
{
	public static final Parser<EuCostTransformer> EU_COST_TRANSFORMER_PARSER = (euCostTransformer) ->
			euCostTransformer.text().withStyle(NUMBER_TEXT);
	
	public static final Parser<TesseractMIText.TieredMachineRecipeType> MACHINE_RECIPE_TYPE_PARSER = (value) ->
	{
		var electric = value.electric();
		var recipeType = value.recipeType();
		String tierString = electric ? "electric" : "bronze";
		String key = "rei_categories.%s.%s_%s".formatted(MI.ID, tierString, recipeType.getPath());
		if(!Language.getInstance().has(key))
		{
			key = "rei_categories.%s.%s".formatted(MI.ID, recipeType.getPath());
		}
		return Component.translatable(key).withStyle(NUMBER_TEXT);
	};
}
