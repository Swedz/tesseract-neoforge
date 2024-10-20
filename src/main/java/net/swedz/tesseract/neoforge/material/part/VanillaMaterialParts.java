package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.part.CommonMaterialPartRegisters.*;
import static net.swedz.tesseract.neoforge.material.part.MaterialPart.*;
import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public interface VanillaMaterialParts
{
	MaterialPart<ItemHolder<Item>> INGOT = item("ingot", "Ingot")
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("ingots"));
	
	MaterialPart<ItemHolder<Item>> NUGGET = item("nugget", "Nugget")
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("nuggets"));
	
	MaterialPart<ItemHolder<Item>> GEM = item("gem", "Gem")
			.withoutSuffix()
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("gems"));
	
	MaterialPart<ItemHolder<Item>> DUST = item("dust", "Dust")
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("dusts"));
	
	MaterialPart<BlockWithItemHolder<Block, BlockItem>> BLOCK = block("block", "Block", "%s_%s"::formatted, (m, p) -> "Block of %s".formatted(m))
			.with(blockModel(CommonModelBuilders::blockCubeAll))
			.with(blockDropsSelf())
			.with(blockItemTagCommon("storage_blocks"));
	
	MaterialPart<ItemHolder<Item>> RAW_METAL = item("raw_metal", "Raw Metal", (m, p) -> "raw_%s".formatted(m), (m, p) -> "Raw %s".formatted(m))
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("raw_materials"));
	
	MaterialPart<BlockWithItemHolder<Block, BlockItem>> RAW_METAL_BLOCK = block("raw_metal_block", "Raw Metal Block", (m, p) -> "raw_%s_block".formatted(m), (m, p) -> "Block of Raw %s".formatted(m))
			.withProperty(BLAST_RESISTANCE, 6f)
			.withProperty(HARDNESS, 5f)
			.with(blockModel(CommonModelBuilders::blockCubeAll))
			.with(blockDropsSelf())
			.with((r, m, h) -> h.item().tag(TagHelper.itemCommonWithChild("storage_blocks", "raw_%s".formatted(m.id()))));
	
	MaterialPart<ItemHolder<Item>> SCRAP = item("scrap", "Scrap")
			.with(itemModel(CommonModelBuilders::generated));
}
