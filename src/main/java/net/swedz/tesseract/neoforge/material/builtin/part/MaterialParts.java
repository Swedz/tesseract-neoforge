package net.swedz.tesseract.neoforge.material.builtin.part;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.material.builtin.part.block.OrePartBlock;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.MaterialPartItemReferenceFormatter;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;

import static net.swedz.tesseract.neoforge.material.builtin.part.CommonMaterialPartRegisters.*;
import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public interface MaterialParts
{
	MaterialPart INGOT = create("ingot", "Ingot")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("ingots"));
	
	MaterialPart NUGGET = create("nugget", "Nugget")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("nuggets"));
	
	MaterialPart GEM = create("gem", "Gem")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.formattingMaterialOnly()
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("gems"));
	
	MaterialPart DUST = create("dust", "Dust")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("dusts"));
	
	MaterialPart BLOCK = create("block", "Block")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag("storage_blocks/%s"))
			.formatting("%s_%s"::formatted, (m, p) -> "Block of %s".formatted(m))
			.blockModel(CommonModelBuilders::blockCubeAll)
			.blockLoot(CommonLootTableBuilders::self)
			.item(itemTagCommon("storage_blocks"));
	
	MaterialPart RAW_METAL = create("raw_metal", "Raw Metal")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag("raw_materials/%s"))
			.formattingMaterialOnly("raw_%s"::formatted, "Raw %s"::formatted)
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("raw_materials"));
	
	MaterialPart RAW_METAL_BLOCK = create("raw_metal_block", "Raw Metal Block")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag("storage_blocks/raw_%s"))
			.formattingMaterialOnly("raw_%s_block"::formatted, "Block of Raw %s"::formatted)
			.set(BLAST_RESISTANCE, 6f)
			.set(HARDNESS, 5f)
			.blockModel(CommonModelBuilders::blockCubeAll)
			.blockLoot(CommonLootTableBuilders::self)
			.item((c, h) -> h.tag(TagHelper.itemCommonWithChild("storage_blocks", "raw_%s".formatted(c.material().id()))));
	
	MaterialPart ORE = create("ore", "Ore")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag("ores/%s"))
			.blockFactory(OrePartBlock.factory())
			.set(BLAST_RESISTANCE, 3f)
			.set(HARDNESS, 3f)
			.blockModel(CommonModelBuilders::blockCubeAll)
			.itemAndBlockTag(Tags.Items.ORES_IN_GROUND_STONE)
			.item(CommonMaterialPartRegisters.itemTagCommon("ores"))
			.block(CommonMaterialPartRegisters.blockItemTagCommon("ores"))
			.block(CommonMaterialPartRegisters.oreDrop());
	
	MaterialPart ORE_DEEPSLATE = create("ore_deepslate", "Deepslate Ore")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag("ores/%s"))
			.blockFactory(OrePartBlock.factory())
			.set(BLAST_RESISTANCE, 3f)
			.set(HARDNESS, 4.5f)
			.formattingMaterialOnly("deepslate_%s_ore"::formatted, "Deepslate %s Ore"::formatted)
			.blockModel(CommonModelBuilders::blockCubeAll)
			.itemAndBlockTag(Tags.Items.ORES_IN_GROUND_DEEPSLATE)
			.item(CommonMaterialPartRegisters.itemTagCommon("ores"))
			.block(CommonMaterialPartRegisters.blockItemTagCommon("ores"))
			.block(CommonMaterialPartRegisters.oreDrop());
	
	MaterialPart ORE_NETHERRACK = create("ore_netherrack", "Netherrack Ore")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag("ores/%s"))
			.blockFactory(OrePartBlock.factory())
			.set(BLAST_RESISTANCE, 3f)
			.set(HARDNESS, 3f)
			.formattingMaterialOnly("nether_%s_ore"::formatted, "Nether %s Ore"::formatted)
			.blockModel(CommonModelBuilders::blockCubeAll)
			.itemAndBlockTag(Tags.Items.ORES_IN_GROUND_NETHERRACK)
			.item(CommonMaterialPartRegisters.itemTagCommon("ores"))
			.block(CommonMaterialPartRegisters.blockItemTagCommon("ores"))
			.block(CommonMaterialPartRegisters.oreDrop());
	
	MaterialPart SCRAP = create("scrap", "Scrap")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	static MaterialPart create(String id, String englishName)
	{
		return new MaterialPart(ResourceLocation.withDefaultNamespace(id), englishName);
	}
}
