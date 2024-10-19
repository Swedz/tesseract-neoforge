package net.swedz.tesseract.neoforge.compat.mi.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.part.CommonMaterialPartRegisters.*;
import static net.swedz.tesseract.neoforge.material.part.MaterialPart.*;
import static net.swedz.tesseract.neoforge.material.part.VanillaMaterialParts.*;

public interface MIMaterialParts
{
	// TODO battery
	
	// TODO barrel
	
	MaterialPart<ItemHolder<Item>> BLADE = item("blade", "Blade")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> BOLT = item("bolt", "Bolt")
			.with(itemModel(CommonModelBuilders::generated));
	
	// TODO cable
	
	MaterialPart<BlockWithItemHolder<Block, BlockItem>> COIL = block("coil", "Coil")
			.with(blockModel(CommonModelBuilders::blockTopEnd))
			.with(blockDropsSelf());
	
	MaterialPart<ItemHolder<Item>> CRUSHED_DUST = item("crushed_dust", "Crushed Dust")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> CURVED_PLATE = item("curved_plate", "Curved Plate")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> DOUBLE_INGOT = item("double_ingot", "Double Ingot")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> DRILL_HEAD = item("drill_head", "Drill Head")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> DRILL = item("drill", "Drill")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> FINE_WIRE = item("fine_wire", "Fine Wire")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> GEAR = item("gear", "Gear")
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("gears"));
	
	// TODO hammer
	/*MaterialPart<ItemHolder<Item>> HAMMER = item("hammer", "Hammer")
			.with(itemModel(CommonModelBuilders::generated));*/
	
	MaterialPart<ItemHolder<Item>> HOT_INGOT = item("hot_ingot", "Hot Ingot")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> LARGE_PLATE = item("large_plate", "Large Plate")
			.with(itemModel(CommonModelBuilders::generated));
	
	// TODO machine casing
	
	// TODO machine casing pipe
	
	// TODO machine casing special
	
	MaterialPart<ItemHolder<Item>> PLATE = item("plate", "Plate")
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("plates"));
	
	MaterialPart<ItemHolder<Item>> RING = item("ring", "Ring")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> ROD = item("rod", "Rod")
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("rods"));
	
	MaterialPart<ItemHolder<Item>> ROD_MAGNETIC = item("rod_magnetic", "Magnetic Rod", "%s_%s"::formatted, (m, p) -> "Magnetic %s Rod".formatted(m))
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> ROTOR = item("rotor", "Rotor")
			.with(itemModel(CommonModelBuilders::generated));
	
	// TODO tank
	
	MaterialPart<ItemHolder<Item>> TINY_DUST = item("tiny_dust", "Tiny Dust")
			.with(itemModel(CommonModelBuilders::generated))
			.with(itemTagCommon("tiny_dusts"));
	
	MaterialPart<ItemHolder<Item>> WIRE = item("wire", "Wire")
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> WIRE_MAGNETIC = item("wire_magnetic", "Magnetic Wire", "%s_%s"::formatted, (m, p) -> "Magnetic %s Wire".formatted(m))
			.with(itemModel(CommonModelBuilders::generated));
	
	// TODO fuel rods
	
	MaterialPart<ItemHolder<Item>> N_DOPED_PLATE = item("n_doped_plate", "N-Doped Plate", "%s_%s"::formatted, (m, p) -> "N-Doped %s Plate".formatted(m))
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> P_DOPED_PLATE = item("p_doped_plate", "P-Doped Plate", "%s_%s"::formatted, (m, p) -> "P-Doped %s Plate".formatted(m))
			.with(itemModel(CommonModelBuilders::generated));
	
	MaterialPart[] ITEM_PURE_NON_METAL = {CRUSHED_DUST, DUST, TINY_DUST};
	MaterialPart[] ITEM_PURE_METAL     = {INGOT, NUGGET, DUST, TINY_DUST};
}
