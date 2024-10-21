package net.swedz.tesseract.neoforge.compat.mi.material.part;

import aztech.modern_industrialization.MI;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;

import static net.swedz.tesseract.neoforge.compat.vanilla.material.part.VanillaMaterialParts.*;
import static net.swedz.tesseract.neoforge.material.part.CommonMaterialPartRegisters.*;

public interface MIMaterialParts
{
	// TODO battery
	
	// TODO barrel
	
	MaterialPart BLADE = create("blade", "Blade")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart BOLT = create("bolt", "Bolt")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	// TODO cable
	
	MaterialPart COIL = create("coil", "Coil")
			.blockModel(CommonModelBuilders::blockTopEnd)
			.blockLoot(CommonLootTableBuilders::self)
			.immutable();
	
	MaterialPart CRUSHED_DUST = create("crushed_dust", "Crushed Dust")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart CURVED_PLATE = create("curved_plate", "Curved Plate")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart DOUBLE_INGOT = create("double_ingot", "Double Ingot")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart DRILL_HEAD = create("drill_head", "Drill Head")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart DRILL = create("drill", "Drill")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart FINE_WIRE = create("fine_wire", "Fine Wire")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart GEAR = create("gear", "Gear")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("gears"))
			.immutable();
	
	// TODO hammer
	/*MaterialPart HAMMER = create("hammer", "Hammer")
			.itemModel(CommonModelBuilders::generated)
			.immutable();*/
	
	MaterialPart HOT_INGOT = create("hot_ingot", "Hot Ingot")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart LARGE_PLATE = create("large_plate", "Large Plate")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	// TODO machine casing
	
	// TODO machine casing pipe
	
	// TODO machine casing special
	
	MaterialPart PLATE = create("plate", "Plate")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("plates"))
			.immutable();
	
	MaterialPart RING = create("ring", "Ring")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart ROD = create("rod", "Rod")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("rods"))
			.immutable();
	
	MaterialPart ROD_MAGNETIC = create("rod_magnetic", "Magnetic Rod")
			.formatting("%s_%s"::formatted, (m, p) -> "Magnetic %s Rod".formatted(m))
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart ROTOR = create("rotor", "Rotor")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	// TODO tank
	
	MaterialPart TINY_DUST = create("tiny_dust", "Tiny Dust")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("tiny_dusts"))
			.immutable();
	
	MaterialPart WIRE = create("wire", "Wire")
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart WIRE_MAGNETIC = create("wire_magnetic", "Magnetic Wire")
			.formatting("%s_%s"::formatted, (m, p) -> "Magnetic %s Wire".formatted(m))
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	// TODO fuel rods
	
	MaterialPart N_DOPED_PLATE = create("n_doped_plate", "N-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "N-Doped %s Plate".formatted(m))
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart P_DOPED_PLATE = create("p_doped_plate", "P-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "P-Doped %s Plate".formatted(m))
			.itemModel(CommonModelBuilders::generated)
			.immutable();
	
	MaterialPart[] ITEM_PURE_NON_METAL = {CRUSHED_DUST, DUST, TINY_DUST};
	MaterialPart[] ITEM_PURE_METAL     = {INGOT, NUGGET, DUST, TINY_DUST};
	
	static MaterialPart create(String id, String englishName)
	{
		return new MaterialPart(MI.id(id), englishName);
	}
}
