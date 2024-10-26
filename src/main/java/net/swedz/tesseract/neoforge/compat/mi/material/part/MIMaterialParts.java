package net.swedz.tesseract.neoforge.compat.mi.material.part;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.MITags;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.blocks.storage.StorageBehaviour;
import aztech.modern_industrialization.blocks.storage.barrel.BarrelBlock;
import aztech.modern_industrialization.blocks.storage.barrel.BarrelBlockEntity;
import aztech.modern_industrialization.blocks.storage.barrel.BarrelItem;
import aztech.modern_industrialization.datagen.model.DelegatingModelBuilder;
import aztech.modern_industrialization.items.ForgeTool;
import aztech.modern_industrialization.items.PortableStorageUnit;
import aztech.modern_industrialization.nuclear.INeutronBehaviour;
import aztech.modern_industrialization.nuclear.NuclearConstant;
import aztech.modern_industrialization.nuclear.NuclearFuel;
import aztech.modern_industrialization.pipes.api.PipeNetworkType;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetwork;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetworkData;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetworkNode;
import aztech.modern_industrialization.pipes.impl.PipeItem;
import aztech.modern_industrialization.proxy.CommonProxy;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.bridge.SlotItemHandler;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.item.ItemVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;
import net.swedz.tesseract.neoforge.compat.mi.material.property.IsotopeFuel;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.MaterialPartItemFactory;
import net.swedz.tesseract.neoforge.material.part.MaterialPartItemReferenceFormatter;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;

import java.util.concurrent.atomic.AtomicReference;

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
	
	MaterialPart BARREL = create("barrel", "Barrel")
			.blockModel((b) -> (provider) ->
			{
				Block block = b.get();
				String id = BuiltInRegistries.BLOCK.getKey(block).getPath();
				provider.simpleBlock(block, provider.models().cubeColumn(id, provider.modLoc("block/%s_side".formatted(id)), provider.modLoc("block/%s_top".formatted(id))));
			})
			.itemModelBuilder(CommonModelBuilders::itemBlockEntity)
			.itemTag(MITags.BARRELS)
			.withRegister((context) ->
			{
				AtomicReference<BlockEntityType<BarrelBlockEntity>> bet = new AtomicReference<>();
				
				StorageBehaviour<ItemVariant> storageBehaviour = BarrelBlock.withStackCapacity(context.getOrThrow(BARREL_CAPACITY));
				
				EntityBlock factory = (pos, state) -> new BarrelBlockEntity(bet.get(), pos, state);
				BlockWithItemHolder<Block, BlockItem> blockHolder = new BlockWithItemHolder<>(
						context.id(), context.englishName(),
						context.registry().blockRegistry(), (p) -> new BarrelBlock(factory, storageBehaviour),
						context.registry().itemRegistry(), (b, p) -> new BarrelItem((BarrelBlock) b, p)
				);
				RegisteredMaterialPart registered = RegisteredMaterialPart.existingBlock(blockHolder);
				context.register(blockHolder);
				
				context.registry().blockEntityRegistry().register(context.id().getPath(), () ->
				{
					bet.set((BlockEntityType) BlockEntityType.Builder.of(factory::newBlockEntity, blockHolder.get()).build(null));
					return bet.get();
				});
				
				CapabilitiesListeners.register(context.registry().modId(), (event) ->
						event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, bet.get(), (be, side) -> new SlotItemHandler(be)));
				
				CommonProxy.INSTANCE.registerPartBarrelClient(bet::get, context.get(MEAN_RGB));
				
				return registered;
			});
	
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
	
	MaterialPart HAMMER = create("hammer", "Hammer")
			.itemFactory((c, p) -> new ForgeTool(c.get(TOOL_TIER), p))
			.itemModelBuilder(CommonModelBuilders::generated)
			.itemTag(ForgeTool.TAG);
	
	MaterialPart HOT_INGOT = create("hot_ingot", "Hot Ingot")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart LARGE_PLATE = create("large_plate", "Large Plate")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart MACHINE_CASING = create("machine_casing", "Machine Casing")
			.blockModel(CommonModelBuilders::blockCubeAll);
	
	MaterialPart MACHINE_CASING_PIPE = create("machine_casing_pipe", "Pipe Machine Casing")
			.blockModel(CommonModelBuilders::blockCubeAll);
	
	MaterialPart MACHINE_CASING_SPECIAL = create("machine_casing_special", "Special Casing")
			.blockModel(CommonModelBuilders::blockCubeAll);
	
	MaterialPart PLATED_BRICKS = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("%s_plated_bricks"::formatted, "%s Plated Bricks"::formatted);
	
	MaterialPart CLEAN_MACHINE_CASING = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("clean_%s_machine_casing"::formatted, "Clean %s Machine Casing"::formatted);
	
	MaterialPart SOLID_MACHINE_CASING = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("solid_%s_machine_casing"::formatted, "Solid %s Machine Casing"::formatted);
	
	MaterialPart PLASMA_HANDLING_MACHINE_CASING = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("plasma_handling_%s_machine_casing"::formatted, "Plasma Handling %s Machine Casing"::formatted);
	
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
	
	static MaterialPartItemFactory fuelRodFactory()
	{
		return (c, p) ->
		{
			int size = c.getOrThrow(NUCLEAR_FUEL_SIZE);
			IsotopeFuel fuel = c.getOrThrow(ISOTOPE);
			NuclearFuel.NuclearFuelParams fuelParams = new NuclearFuel.NuclearFuelParams(
					NuclearConstant.DESINTEGRATION_BY_ROD * size, fuel.maxTemp, fuel.tempLimitLow, fuel.tempLimitHigh, fuel.neutronsMultiplication, fuel.directEnergyFactor, size
			);
			INeutronBehaviour neutronBehaviour = INeutronBehaviour.of(NuclearConstant.ScatteringType.HEAVY, fuel, size);
			return new NuclearFuel(p.stacksTo(1), fuelParams, neutronBehaviour, c.registry().id("%s_fuel_rod_depleted".formatted(c.material().id().getPath())));
		};
	}
	
	MaterialPart FUEL_ROD_DEPLETED = create("fuel_rod_depleted", "Depleted Fuel Rod")
			.formattingMaterialOnly("%s_fuel_rod_depleted"::formatted, "Depleted %s Fuel Rod"::formatted);
	
	MaterialPart FUEL_ROD = create("fuel_rod", "Fuel Rod")
			.set(NUCLEAR_FUEL_SIZE, 1)
			.itemFactory(fuelRodFactory());
	
	MaterialPart FUEL_ROD_DOUBLE = create("fuel_rod_double", "Double Fuel Rod")
			.set(NUCLEAR_FUEL_SIZE, 2)
			.itemFactory(fuelRodFactory());
	
	MaterialPart FUEL_ROD_QUAD = create("fuel_rod_quad", "Quad Fuel Rod")
			.set(NUCLEAR_FUEL_SIZE, 4)
			.itemFactory(fuelRodFactory());
	
	MaterialPart[] ALL_FUEL_RODS = {FUEL_ROD, FUEL_ROD_DOUBLE, FUEL_ROD_QUAD, FUEL_ROD_DEPLETED};
	
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
