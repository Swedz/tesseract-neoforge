package net.swedz.tesseract.neoforge.material.builtin.property;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.MaterialPartItemReferenceFormatter;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;

import java.util.Optional;

public interface MaterialProperties
{
	MaterialProperty<Float> BLAST_RESISTANCE = create("blast_resistance", 6f)
			.block((h, v) -> h.withProperties((p) -> p.explosionResistance(v)));
	
	MaterialProperty<Float> HARDNESS = create("hardness", 5f)
			.block((h, v) -> h.withProperties((p) -> p.destroyTime(v)));
	
	MaterialProperty<Boolean> REQUIRES_CORRECT_TOOL_FOR_DROPS = create("requires_correct_tool_for_drops", true)
			.block((h, v) ->
			{
				if(v)
				{
					h.withProperties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
				}
			});
	
	MaterialProperty<Optional<TagKey<Block>>> NEEDS_TOOL = create("needs_tool", Optional.of(BlockTags.NEEDS_STONE_TOOL))
			.block((h, v) -> v.ifPresent(h::tag));
	
	MaterialProperty<Optional<TagKey<Block>>> MINEABLE = create("mineable", Optional.of(BlockTags.MINEABLE_WITH_PICKAXE))
			.block((h, v) -> v.ifPresent(h::tag));
	
	MaterialProperty<MaterialPartItemReferenceFormatter> ITEM_REFERENCE = create("item_reference", (namespace, material, part) -> ResourceLocation.fromNamespaceAndPath(namespace, part.formatId(material)).toString());
	
	MaterialProperty<MaterialPart> MAIN_PART = create("main_part", MaterialParts.INGOT);
	
	MaterialProperty<Integer> MEAN_RGB = create("mean_rgb", 0);
	
	MaterialProperty<OrePartDrops> ORE_DROP_PART = create("ore_drop_part", new OrePartDrops(MaterialParts.RAW_METAL));
	
	static <T> MaterialProperty<T> create(String id, T defaultValue)
	{
		return new MaterialProperty<>(Tesseract.id(id), defaultValue);
	}
}
