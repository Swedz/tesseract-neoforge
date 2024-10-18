package net.swedz.tesseract.neoforge.material.property;

import net.minecraft.tags.BlockTags;

public interface MaterialProperties
{
	MaterialProperty<Float> BLAST_RESISTANCE = new MaterialProperty<>("blast_resistance", 6f);
	
	MaterialProperty<Float> HARDNESS = new MaterialProperty<>("hardness", 5f);
	
	MaterialProperty<Boolean> REQUIRES_CORRECT_TOOL_FOR_DROPS = new MaterialProperty<>("requires_correct_tool_for_drops", true);
	
	MaterialPropertyTag.BlockOptional NEEDS_TOOL = new MaterialPropertyTag.BlockOptional("needs_tool", BlockTags.NEEDS_STONE_TOOL);
	
	MaterialPropertyTag.BlockOptional MINEABLE = new MaterialPropertyTag.BlockOptional("mineable", BlockTags.MINEABLE_WITH_PICKAXE);
}
