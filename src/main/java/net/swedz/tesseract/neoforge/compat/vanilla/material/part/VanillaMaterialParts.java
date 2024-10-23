package net.swedz.tesseract.neoforge.compat.vanilla.material.part;

import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;

import static net.swedz.tesseract.neoforge.material.part.CommonMaterialPartRegisters.*;
import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public interface VanillaMaterialParts
{
	MaterialPart INGOT = create("ingot", "Ingot")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("ingots"));
	
	MaterialPart NUGGET = create("nugget", "Nugget")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("nuggets"));
	
	MaterialPart GEM = create("gem", "Gem")
			.formattingMaterialOnly()
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("gems"));
	
	MaterialPart DUST = create("dust", "Dust")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("dusts"));
	
	MaterialPart BLOCK = create("block", "Block")
			.formatting("%s_%s"::formatted, (m, p) -> "Block of %s".formatted(m))
			.blockModel(CommonModelBuilders::blockCubeAll)
			.blockLoot(CommonLootTableBuilders::self)
			.item(itemTagCommon("storage_blocks"));
	
	MaterialPart RAW_METAL = create("raw_metal", "Raw Metal")
			.formattingMaterialOnly("raw_%s"::formatted, "Raw %s"::formatted)
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("raw_materials"));
	
	MaterialPart RAW_METAL_BLOCK = create("raw_metal_block", "Raw Metal Block")
			.formattingMaterialOnly("raw_%s_block"::formatted, "Block of Raw %s"::formatted)
			.set(BLAST_RESISTANCE, 6f)
			.set(HARDNESS, 5f)
			.blockModel(CommonModelBuilders::blockCubeAll)
			.blockLoot(CommonLootTableBuilders::self)
			.item((r, m, h) -> h.tag(TagHelper.itemCommonWithChild("storage_blocks", "raw_%s".formatted(m.id()))));
	
	MaterialPart SCRAP = create("scrap", "Scrap")
			.itemModel(CommonModelBuilders::generated);
	
	static MaterialPart create(String id, String englishName)
	{
		return new MaterialPart(ResourceLocation.withDefaultNamespace(id), englishName);
	}
}
