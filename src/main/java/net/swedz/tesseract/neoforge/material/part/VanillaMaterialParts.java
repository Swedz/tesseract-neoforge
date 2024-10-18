package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public interface VanillaMaterialParts
{
	MaterialPart<ItemHolder<Item>> INGOT = MaterialPart.item("ingot", "Ingot")
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("ingots", m.rawId())));
	
	MaterialPart<ItemHolder<Item>> NUGGET = MaterialPart.item("nugget", "Nugget")
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("nuggets", m.rawId())));
	
	MaterialPart<ItemHolder<Item>> GEM = MaterialPart.item()
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("gems", m.rawId())));
	
	MaterialPart<ItemHolder<Item>> DUST = MaterialPart.item("dust", "Dust")
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("dusts", m.rawId())));
	
	MaterialPart<BlockWithItemHolder<Block, BlockItem>> BLOCK = MaterialPart.block("block", "Block", "%s_%s"::formatted, (m, p) -> "Block of %s".formatted(m))
			.with((r, m, h) ->
			{
				h.withLootTable(CommonLootTableBuilders::self)
						.withModel(CommonModelBuilders::blockCubeAll);
				h.item().tag(TagHelper.itemCommonWithChild("storage_blocks", m.rawId()));
			});
	
	MaterialPart<ItemHolder<Item>> RAW_METAL = MaterialPart.item("raw_metal", "Raw Metal", (m, p) -> "raw_%s".formatted(m), (m, p) -> "Raw %s".formatted(m))
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("raw_materials", m.rawId())));
	
	MaterialPart<BlockWithItemHolder<Block, BlockItem>> RAW_METAL_BLOCK = MaterialPart.block("raw_metal_block", "Raw Metal Block", (m, p) -> "raw_%s_block".formatted(m), (m, p) -> "Block of Raw %s".formatted(m))
			.withProperty(BLAST_RESISTANCE, 6f)
			.withProperty(HARDNESS, 5f)
			.with((r, m, h) ->
			{
				h.withLootTable(CommonLootTableBuilders::self)
						.withModel(CommonModelBuilders::blockCubeAll);
				h.item().tag(TagHelper.itemCommonWithChild("storage_blocks", "raw_%s".formatted(m.rawId())));
			});
}
