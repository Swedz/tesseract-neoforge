package net.swedz.tesseract.neoforge.compat.vanilla.material.recipe;

import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeGroup;

import static net.swedz.tesseract.neoforge.compat.vanilla.material.part.VanillaMaterialParts.*;

public interface VanillaMaterialRecipeGroups
{
	MaterialRecipeGroup STANDARD = MaterialRecipeGroup.create(VanillaMaterialRecipeContext::new)
			.add("nugget_to_ingot", (context) -> context.shapeless3x3(NUGGET, INGOT, true))
			.add("main_to_block", (context) -> context.shapeless3x3(context.mainPart(), BLOCK, true))
			.add("raw_metal_to_block", (context) -> context.shapeless3x3(RAW_METAL, RAW_METAL_BLOCK, true));
	
	MaterialRecipeGroup SMELTING = MaterialRecipeGroup.create(VanillaMaterialRecipeContext::new)
			.add("raw_metal_to_primary", (context) -> context.smeltingAndBlasting(RAW_METAL, context.mainPart()));
	// TODO ores
}
