package net.swedz.tesseract.neoforge.compat.mi.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.part.VanillaMaterialParts.*;

public interface MIMaterialParts
{
	// TODO battery
	
	// TODO barrel
	
	MaterialPart<ItemHolder<Item>> BLADE = MaterialPart.item("blade", "Blade")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> BOLT = MaterialPart.item("bolt", "Bolt")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	// TODO cable
	
	MaterialPart<BlockWithItemHolder<Block, BlockItem>> COIL = MaterialPart.block("coil", "Coil")
			.with((r, m, h) -> h
					.withLootTable(CommonLootTableBuilders::self)
					.withModel(CommonModelBuilders::blockTopEnd));
	
	MaterialPart<ItemHolder<Item>> CRUSHED_DUST = MaterialPart.item("crushed_dust", "Crushed Dust")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> CURVED_PLATE = MaterialPart.item("curved_plate", "Curved Plate")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> DOUBLE_INGOT = MaterialPart.item("double_ingot", "Double Ingot")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> DRILL_HEAD = MaterialPart.item("drill_head", "Drill Head")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> DRILL = MaterialPart.item("drill", "Drill")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> FINE_WIRE = MaterialPart.item("fine_wire", "Fine Wire")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> GEAR = MaterialPart.item("gear", "Gear")
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("gears", m.rawId())));
	
	// TODO hammer
	/*MaterialPart<ItemHolder<Item>> HAMMER = MaterialPart.item("hammer", "Hammer")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));*/
	
	MaterialPart<ItemHolder<Item>> HOT_INGOT = MaterialPart.item("hot_ingot", "Hot Ingot")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> LARGE_PLATE = MaterialPart.item("large_plate", "Large Plate")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	// TODO machine casing
	
	// TODO machine casing pipe
	
	// TODO machine casing special
	
	MaterialPart<ItemHolder<Item>> PLATE = MaterialPart.item("plate", "Plate")
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("plates", m.rawId())));
	
	MaterialPart<ItemHolder<Item>> RING = MaterialPart.item("ring", "Ring")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> ROD = MaterialPart.item("rod", "Rod")
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("rods", m.rawId())));
	
	MaterialPart<ItemHolder<Item>> ROD_MAGNETIC = MaterialPart.item("rod_magnetic", "Magnetic Rod", "%s_%s"::formatted, (m, p) -> "Magnetic %s Rod".formatted(m))
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> ROTOR = MaterialPart.item("rotor", "Rotor")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	// TODO tank
	
	MaterialPart<ItemHolder<Item>> TINY_DUST = MaterialPart.item("tiny_dust", "Tiny Dust")
			.with((r, m, h) -> h
					.withModel(CommonModelBuilders::generated)
					.tag(TagHelper.itemCommonWithChild("tiny_dusts", m.rawId())));
	
	MaterialPart<ItemHolder<Item>> WIRE = MaterialPart.item("wire", "Wire")
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> WIRE_MAGNETIC = MaterialPart.item("wire_magnetic", "Magnetic Wire", "%s_%s"::formatted, (m, p) -> "Magnetic %s Wire".formatted(m))
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	// TODO fuel rods
	
	MaterialPart<ItemHolder<Item>> N_DOPED_PLATE = MaterialPart.item("n_doped_plate", "N-Doped Plate", "%s_%s"::formatted, (m, p) -> "N-Doped %s Plate".formatted(m))
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart<ItemHolder<Item>> P_DOPED_PLATE = MaterialPart.item("p_doped_plate", "P-Doped Plate", "%s_%s"::formatted, (m, p) -> "P-Doped %s Plate".formatted(m))
			.with((r, m, h) ->
					h.withModel(CommonModelBuilders::generated));
	
	MaterialPart[] ITEM_PURE_NON_METAL = {CRUSHED_DUST, DUST, TINY_DUST};
	MaterialPart[] ITEM_PURE_METAL     = {INGOT, NUGGET, DUST, TINY_DUST};
}
