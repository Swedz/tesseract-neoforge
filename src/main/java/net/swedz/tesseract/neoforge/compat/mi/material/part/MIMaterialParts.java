package net.swedz.tesseract.neoforge.compat.mi.material.part;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.items.PortableStorageUnit;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;

import static net.swedz.tesseract.neoforge.compat.mi.material.property.MIMaterialProperties.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.CommonMaterialPartRegisters.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;

public interface MIMaterialParts
{
	MaterialPart BATTERY = create("battery", "Battery")
			.itemModel(CommonModelBuilders::generated)
			.item((context, holder) ->
			{
				long batteryCapacity = context.getOrThrow(BATTERY_CAPACITY);
				if(batteryCapacity <= 0)
				{
					throw new IllegalArgumentException("Battery capacity must be > 0");
				}
				holder.withRegistrationListener((item) -> PortableStorageUnit.CAPACITY_PER_BATTERY.put(item, batteryCapacity));
			});
	
	// TODO barrel
	
	MaterialPart BLADE = create("blade", "Blade")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart BOLT = create("bolt", "Bolt")
			.itemModel(CommonModelBuilders::generated);
	
	// TODO cable
	
	MaterialPart COIL = create("coil", "Coil")
			.blockModel(CommonModelBuilders::blockTopEnd)
			.blockLoot(CommonLootTableBuilders::self);
	
	MaterialPart CRUSHED_DUST = create("crushed_dust", "Crushed Dust")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart CURVED_PLATE = create("curved_plate", "Curved Plate")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart DOUBLE_INGOT = create("double_ingot", "Double Ingot")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart DRILL_HEAD = create("drill_head", "Drill Head")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart DRILL = create("drill", "Drill")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart FINE_WIRE = create("fine_wire", "Fine Wire")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart GEAR = create("gear", "Gear")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("gears"));
	
	// TODO hammer
	/*MaterialPart HAMMER = create("hammer", "Hammer")
			.itemModel(CommonModelBuilders::generated);*/
	
	MaterialPart HOT_INGOT = create("hot_ingot", "Hot Ingot")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart LARGE_PLATE = create("large_plate", "Large Plate")
			.itemModel(CommonModelBuilders::generated);
	
	// TODO machine casing
	
	// TODO machine casing pipe
	
	// TODO machine casing special
	
	MaterialPart PLATE = create("plate", "Plate")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("plates"));
	
	MaterialPart RING = create("ring", "Ring")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart ROD = create("rod", "Rod")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("rods"));
	
	MaterialPart ROD_MAGNETIC = create("rod_magnetic", "Magnetic Rod")
			.formatting("%s_%s"::formatted, (m, p) -> "Magnetic %s Rod".formatted(m))
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart ROTOR = create("rotor", "Rotor")
			.itemModel(CommonModelBuilders::generated);
	
	// TODO tank
	
	MaterialPart TINY_DUST = create("tiny_dust", "Tiny Dust")
			.itemModel(CommonModelBuilders::generated)
			.item(itemTagCommon("tiny_dusts"));
	
	MaterialPart WIRE = create("wire", "Wire")
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart WIRE_MAGNETIC = create("wire_magnetic", "Magnetic Wire")
			.formatting("%s_%s"::formatted, (m, p) -> "Magnetic %s Wire".formatted(m))
			.itemModel(CommonModelBuilders::generated);
	
	// TODO fuel rods
	
	MaterialPart N_DOPED_PLATE = create("n_doped_plate", "N-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "N-Doped %s Plate".formatted(m))
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart P_DOPED_PLATE = create("p_doped_plate", "P-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "P-Doped %s Plate".formatted(m))
			.itemModel(CommonModelBuilders::generated);
	
	MaterialPart[] ITEM_PURE_NON_METAL = {CRUSHED_DUST, DUST, TINY_DUST};
	MaterialPart[] ITEM_PURE_METAL     = {INGOT, NUGGET, DUST, TINY_DUST};
	
	static MaterialPart create(String id, String englishName)
	{
		return new MaterialPart(MI.id(id), englishName);
	}
}
