package net.swedz.tesseract.neoforge.material.property;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.swedz.tesseract.neoforge.compat.vanilla.material.part.VanillaMaterialParts;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;

import java.util.Optional;

public interface MaterialProperties
{
	MaterialProperty<Float> BLAST_RESISTANCE = new MaterialProperty<>("blast_resistance", 6f)
			.block((h, v) -> h.withProperties((p) -> p.explosionResistance(v)));
	
	MaterialProperty<Float> HARDNESS = new MaterialProperty<>("hardness", 5f)
			.block((h, v) -> h.withProperties((p) -> p.destroyTime(v)));
	
	MaterialProperty<Boolean> REQUIRES_CORRECT_TOOL_FOR_DROPS = new MaterialProperty<>("requires_correct_tool_for_drops", true)
			.block((h, v) ->
			{
				if(v)
				{
					h.withProperties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
				}
			});
	
	MaterialProperty<Optional<TagKey<Block>>> NEEDS_TOOL = new MaterialProperty<>("needs_tool", Optional.of(BlockTags.NEEDS_STONE_TOOL))
			.block((h, v) -> v.ifPresent(h::tag));
	
	MaterialProperty<Optional<TagKey<Block>>> MINEABLE = new MaterialProperty<>("mineable", Optional.of(BlockTags.MINEABLE_WITH_PICKAXE))
			.block((h, v) -> v.ifPresent(h::tag));
	
	MaterialProperty<MaterialPart> MAIN_PART = new MaterialProperty<>("main_part", VanillaMaterialParts.INGOT);
}
