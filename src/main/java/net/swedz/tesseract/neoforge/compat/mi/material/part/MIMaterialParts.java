package net.swedz.tesseract.neoforge.compat.mi.material.part;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.datagen.model.DelegatingModelBuilder;
import aztech.modern_industrialization.items.PortableStorageUnit;
import aztech.modern_industrialization.pipes.api.PipeNetworkType;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetwork;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetworkData;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetworkNode;
import aztech.modern_industrialization.pipes.impl.PipeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.MaterialPartItemReferenceFormatter;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;

import static net.swedz.tesseract.neoforge.compat.mi.material.property.MIMaterialProperties.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.CommonMaterialPartRegisters.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;
import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public interface MIMaterialParts
{
	MaterialPart BATTERY = create("battery", "Battery")
			.itemModelBuilder(CommonModelBuilders::generated)
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
	/*MaterialPart BARREL = create("barrel", "Barrel")
			.blockFactory((c, p) -> new BarrelBlock(p, BarrelBlock.withStackCapacity(c.getOrThrow(BARREL_CAPACITY))), (c, b, p) -> new BarrelItem((BarrelBlock) b, p))
			.blockModel((b) -> (provider) ->
			{
				Block block = b.get();
				String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
				ResourceLocation side = provider.modLoc("block/%s_side".formatted(id));
				ResourceLocation top = provider.modLoc("block/%s_top".formatted(id));
				provider.simpleBlock(block, provider.models().cubeColumn(id, side, top));
			});*/
	
	MaterialPart BLADE = create("blade", "Blade")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart BOLT = create("bolt", "Bolt")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart CABLE = create("cable", "Cable")
			.itemFactory((c, p) ->
			{
				CableTier tier = c.getOrThrow(CABLE_TIER);
				String cableId = "%s_cable".formatted(c.material().id());
				PipeNetworkType type = PipeNetworkType.register(
						c.registry().id(cableId),
						(id, data) -> new ElectricityNetwork(id, data, tier),
						ElectricityNetworkNode::new, c.get(MEAN_RGB) | 0xFF000000, false
				);
				return new PipeItem(p, type, new ElectricityNetworkData());
			})
			.itemModel((i) -> (provider) -> provider.getBuilder(BuiltInRegistries.ITEM.getKey(i.get()).getPath())
					.customLoader(DelegatingModelBuilder::new)
					.delegate(provider.getExistingFile(MI.id("block/pipe")))
					.end());
	
	MaterialPart COIL = create("coil", "Coil")
			.blockModel(CommonModelBuilders::blockTopEnd)
			.blockLoot(CommonLootTableBuilders::self);
	
	MaterialPart CRUSHED_DUST = create("crushed_dust", "Crushed Dust")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart CURVED_PLATE = create("curved_plate", "Curved Plate")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart DOUBLE_INGOT = create("double_ingot", "Double Ingot")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart DRILL_HEAD = create("drill_head", "Drill Head")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart DRILL = create("drill", "Drill")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart FINE_WIRE = create("fine_wire", "Fine Wire")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart GEAR = create("gear", "Gear")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("gears"));
	
	// TODO hammer
	/*MaterialPart HAMMER = create("hammer", "Hammer")
			.itemModelBuilder(CommonModelBuilders::generated);*/
	
	MaterialPart HOT_INGOT = create("hot_ingot", "Hot Ingot")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart LARGE_PLATE = create("large_plate", "Large Plate")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	// TODO machine casing
	
	// TODO machine casing pipe
	
	// TODO machine casing special
	
	MaterialPart PLATE = create("plate", "Plate")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("plates"));
	
	MaterialPart RING = create("ring", "Ring")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart ROD = create("rod", "Rod")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("rods"));
	
	MaterialPart ROD_MAGNETIC = create("rod_magnetic", "Magnetic Rod")
			.formatting("%s_%s"::formatted, (m, p) -> "Magnetic %s Rod".formatted(m))
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart ROTOR = create("rotor", "Rotor")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	// TODO tank
	
	MaterialPart TINY_DUST = create("tiny_dust", "Tiny Dust")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("tiny_dusts"));
	
	MaterialPart WIRE = create("wire", "Wire")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart WIRE_MAGNETIC = create("wire_magnetic", "Magnetic Wire")
			.formatting("%s_%s"::formatted, (m, p) -> "Magnetic %s Wire".formatted(m))
			.itemModelBuilder(CommonModelBuilders::generated);
	
	// TODO fuel rods
	
	MaterialPart N_DOPED_PLATE = create("n_doped_plate", "N-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "N-Doped %s Plate".formatted(m))
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart P_DOPED_PLATE = create("p_doped_plate", "P-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "P-Doped %s Plate".formatted(m))
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart[] ITEM_PURE_NON_METAL = {CRUSHED_DUST, DUST, TINY_DUST};
	MaterialPart[] ITEM_PURE_METAL     = {INGOT, NUGGET, DUST, TINY_DUST};
	
	static MaterialPart create(String id, String englishName)
	{
		return new MaterialPart(MI.id(id), englishName);
	}
}
