package net.swedz.tesseract.neoforge.material.builtin.recipe;

import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeGroup;

import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;

public interface VanillaMaterialRecipeGroups
{
	MaterialRecipeGroup<VanillaMaterialRecipeContext> STANDARD = MaterialRecipeGroup.create(VanillaMaterialRecipeContext::new)
			.add("nugget_to_ingot", (context) -> context.shapeless3x3(NUGGET, INGOT, true))
			.add("main_to_block", (context) -> context.shapeless3x3(context.mainPart(), BLOCK, true))
			.add("raw_metal_to_block", (context) -> context.shapeless3x3(RAW_METAL, RAW_METAL_BLOCK, true));
	
	MaterialRecipeGroup<VanillaMaterialRecipeContext> SMELTING = MaterialRecipeGroup.create(VanillaMaterialRecipeContext::new)
			.add("raw_metal_to_ingot", (context) -> context.smeltingAndBlasting(RAW_METAL, INGOT, 0.7f))
			.add("ore_to_primary", (context) -> context.smeltingAndBlasting(ORE, context.mainPart(), 0.7f))
			.add("ore_deepslate_to_primary", (context) -> context.smeltingAndBlasting(ORE_DEEPSLATE, context.mainPart(), 0.7f))
			.add("ore_netherrack_to_primary", (context) -> context.smeltingAndBlasting(ORE_NETHERRACK, context.mainPart(), 0.7f));
}
