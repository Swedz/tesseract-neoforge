package net.swedz.tesseract.neoforge.compat.mi.tooltip;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.compat.mi.TesseractMIText;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied.EuCostTransformer;
import net.swedz.tesseract.neoforge.tooltip.Parser;

public interface MIParser
{
	Parser<CableTier> CABLE_TIER_SHORT = CableTier::shortEnglishName;
	Parser<CableTier> CABLE_TIER_LONG  = CableTier::longEnglishName;
	
	Parser<EuCostTransformer> EU_COST_TRANSFORMER_PARSER = EuCostTransformer::text;
	
	Parser<TesseractMIText.TieredMachineRecipeType> MACHINE_RECIPE_TYPE_PARSER = (value) ->
	{
		var electric = value.electric();
		var recipeType = value.recipeType();
		String tierString = electric ? "electric" : "bronze";
		String key = "rei_categories.%s.%s_%s".formatted(MI.ID, tierString, recipeType.getPath());
		if(!Language.getInstance().has(key))
		{
			key = "rei_categories.%s.%s".formatted(MI.ID, recipeType.getPath());
		}
		return Component.translatable(key);
	};
}
