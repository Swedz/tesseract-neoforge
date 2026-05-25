package net.swedz.tesseract.neoforge.compat.mi.material.part;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.MICommonProxy;
import aztech.modern_industrialization.MITags;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.blocks.storage.StorageBehaviour;
import aztech.modern_industrialization.blocks.storage.barrel.BarrelBlock;
import aztech.modern_industrialization.blocks.storage.barrel.BarrelBlockEntity;
import aztech.modern_industrialization.blocks.storage.barrel.BarrelItem;
import aztech.modern_industrialization.blocks.storage.tank.TankBlock;
import aztech.modern_industrialization.blocks.storage.tank.TankBlockEntity;
import aztech.modern_industrialization.blocks.storage.tank.TankItem;
import aztech.modern_industrialization.items.ForgeTool;
import aztech.modern_industrialization.items.PortableStorageUnit;
import aztech.modern_industrialization.materials.part.TankPart;
import aztech.modern_industrialization.nuclear.NeutronBehaviour;
import aztech.modern_industrialization.nuclear.NuclearConstant;
import aztech.modern_industrialization.nuclear.NuclearFuel;
import aztech.modern_industrialization.pipes.MIPipes;
import aztech.modern_industrialization.pipes.api.PipeNetworkType;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetwork;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetworkData;
import aztech.modern_industrialization.pipes.electricity.ElectricityNetworkNode;
import aztech.modern_industrialization.pipes.impl.PipeItem;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
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
	MaterialPart BATTERY = MIMaterialParts.create("battery", "Battery")
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
	
	MaterialPart BARREL = MIMaterialParts.create("barrel", "Barrel")
			.blockModel((b) -> (generators) ->
					generators.block().createTrivialBlock(b.get(), TexturedModel.COLUMN))
			.itemModelBuilder(CommonModelBuilders::itemBlockEntity)
			.itemTag(MITags.BARRELS)
			.withRegister((context) ->
			{
				AtomicReference<BlockEntityType<BarrelBlockEntity>> bet = new AtomicReference<>();
				
				var storageBehaviour = BarrelBlock.withStackCapacity(context.getOrThrow(BARREL_CAPACITY));
				
				EntityBlock factory = (pos, state) -> new BarrelBlockEntity(bet.get(), pos, state);
				BlockWithItemHolder<Block, BlockItem> blockHolder = new BlockWithItemHolder<>(
						context.id(), context.englishName(),
						context.registry().blockRegistry(), (p) -> new BarrelBlock(p, factory, storageBehaviour),
						context.registry().itemRegistry(), (b, p) -> new BarrelItem((BarrelBlock) b, p)
				);
				RegisteredMaterialPart registered = RegisteredMaterialPart.existingBlock(blockHolder);
				context.register(blockHolder);
				
				context.registry().blockEntityRegistry().register(
						context.id().getPath(), () ->
						{
							bet.set((BlockEntityType) new BlockEntityType<>(factory::newBlockEntity, blockHolder.get()));
							return bet.get();
						}
				);
				
				CapabilitiesListeners.register(
						context.registry().modId(), (event) ->
						{
							// TODO 26.1 event.registerBlockEntity(Capabilities.Item.BLOCK, bet.get(), (be, side) -> new SlotItemHandler(be));
						}
				);
				
				MICommonProxy.INSTANCE.registerPartBarrelClient(bet::get, context.get(MEAN_RGB));
				
				return registered;
			});
	
	MaterialPart BLADE = MIMaterialParts.create("blade", "Blade")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart BOLT = MIMaterialParts.create("bolt", "Bolt")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart CABLE = MIMaterialParts.create("cable", "Cable")
			.itemFactory((c, p) ->
			{
				CableTier tier = c.getOrThrow(CABLE_TIER);
				String cableId = "%s_cable".formatted(c.material().id());
				PipeNetworkType type = PipeNetworkType.register(
						c.registry().id(cableId),
						(id, data) -> new ElectricityNetwork(id, data, tier),
						ElectricityNetworkData.CODEC,
						ElectricityNetworkNode::new,
						c.get(MEAN_RGB) | 0xFF000000,
						false
				);
				return new PipeItem(p, type, new ElectricityNetworkData());
			})
			.itemModel((item) -> (generators) ->
					MIPipes.ITEM_MODEL_GENERATOR.accept(item.get(), generators.item()));
	
	MaterialPart COIL = MIMaterialParts.create("coil", "Coil")
			.blockModel(CommonModelBuilders::blockTopEnd)
			.blockLoot(CommonLootTableBuilders::self);
	
	MaterialPart CRUSHED_DUST = MIMaterialParts.create("crushed_dust", "Crushed Dust")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart CURVED_PLATE = MIMaterialParts.create("curved_plate", "Curved Plate")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart DOUBLE_INGOT = MIMaterialParts.create("double_ingot", "Double Ingot")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart DRILL_HEAD = MIMaterialParts.create("drill_head", "Drill Head")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart DRILL = MIMaterialParts.create("drill", "Drill")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart FINE_WIRE = MIMaterialParts.create("fine_wire", "Fine Wire")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart GEAR = MIMaterialParts.create("gear", "Gear")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("gears"));
	
	MaterialPart HAMMER = MIMaterialParts.create("hammer", "Hammer")
			.itemFactory((c, p) -> new ForgeTool(p, c.get(TOOL_DURABILITY), c.get(TOOL_ENCHANTABILITY)))
			.itemModelBuilder(CommonModelBuilders::generated)
			.itemTag(ForgeTool.TAG);
	
	MaterialPart HOT_INGOT = MIMaterialParts.create("hot_ingot", "Hot Ingot")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart LARGE_PLATE = MIMaterialParts.create("large_plate", "Large Plate")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart MACHINE_CASING = MIMaterialParts.create("machine_casing", "Machine Casing")
			.blockModel(CommonModelBuilders::blockCubeAll)
			.blockLoot(CommonLootTableBuilders::self);
	
	MaterialPart MACHINE_CASING_PIPE = MIMaterialParts.create("machine_casing_pipe", "Pipe Machine Casing")
			.blockModel(CommonModelBuilders::blockCubeAll)
			.blockLoot(CommonLootTableBuilders::self);
	
	MaterialPart MACHINE_CASING_SPECIAL = MIMaterialParts.create("machine_casing_special", "Special Casing")
			.blockModel(CommonModelBuilders::blockCubeAll)
			.blockLoot(CommonLootTableBuilders::self);
	
	MaterialPart PLATED_BRICKS = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("%s_plated_bricks"::formatted, "%s Plated Bricks"::formatted);
	
	MaterialPart CLEAN_MACHINE_CASING = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("clean_%s_machine_casing"::formatted, "Clean %s Machine Casing"::formatted);
	
	MaterialPart SOLID_MACHINE_CASING = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("solid_%s_machine_casing"::formatted, "Solid %s Machine Casing"::formatted);
	
	MaterialPart PLASMA_HANDLING_MACHINE_CASING = MACHINE_CASING_SPECIAL
			.formattingMaterialOnly("plasma_handling_%s_machine_casing"::formatted, "Plasma Handling %s Machine Casing"::formatted);
	
	MaterialPart PLATE = MIMaterialParts.create("plate", "Plate")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("plates"));
	
	MaterialPart RING = MIMaterialParts.create("ring", "Ring")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart ROD = MIMaterialParts.create("rod", "Rod")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("rods"));
	
	MaterialPart ROD_MAGNETIC = MIMaterialParts.create("rod_magnetic", "Magnetic Rod")
			.formatting("%s_%s"::formatted, (m, p) -> "Magnetic %s Rod".formatted(m))
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart ROTOR = MIMaterialParts.create("rotor", "Rotor")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart TANK = MIMaterialParts.create("tank", "Tank")
			.blockModel((block) -> (generators) ->
					TankPart.MODEL_GENERATOR.accept(block.get(), generators.block()))
			.itemModelBuilder(CommonModelBuilders::itemBlockEntity)
			.itemTag(MITags.TANKS)
			.withRegister((context) ->
			{
				AtomicReference<BlockEntityType<TankBlockEntity>> bet = new AtomicReference<>();
				
				StorageBehaviour<FluidResource> storageBehaviour = StorageBehaviour.uniformQuantity(FluidType.BUCKET_VOLUME * context.getOrThrow(TANK_CAPACITY));
				
				EntityBlock factory = (pos, state) -> new TankBlockEntity(bet.get(), pos, state);
				BlockWithItemHolder<Block, BlockItem> blockHolder = new BlockWithItemHolder<>(
						context.id(), context.englishName(),
						context.registry().blockRegistry(), (p) -> new TankBlock(p, factory, storageBehaviour),
						context.registry().itemRegistry(), (b, p) -> new TankItem((TankBlock) b, p)
				);
				RegisteredMaterialPart registered = RegisteredMaterialPart.existingBlock(blockHolder);
				context.register(blockHolder);
				
				context.registry().blockEntityRegistry().register(
						context.id().getPath(), () ->
						{
							bet.set((BlockEntityType) new BlockEntityType<>(factory::newBlockEntity, blockHolder.get()));
							return bet.get();
						}
				);
				
				CapabilitiesListeners.register(
						context.registry().modId(), (event) ->
						{
							event.registerBlockEntity(Capabilities.Fluid.BLOCK, bet.get(), (be, side) -> be.fluidHandler);
							
							// TODO 26.1
							//TankItem item = (TankItem) blockHolder.asItem();
							//event.registerItem(Capabilities.Fluid.ITEM, (stack, __) -> new ContainerItem.FluidHandler(stack, item), item);
						}
				);
				
				MICommonProxy.INSTANCE.registerPartTankClient(bet::get, context.get(MEAN_RGB));
				
				return registered;
			});
	
	MaterialPart TINY_DUST = MIMaterialParts.create("tiny_dust", "Tiny Dust")
			.set(ITEM_REFERENCE, MaterialPartItemReferenceFormatter.tag())
			.itemModelBuilder(CommonModelBuilders::generated)
			.item(itemTagCommon("tiny_dusts"));
	
	MaterialPart WIRE = MIMaterialParts.create("wire", "Wire")
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart WIRE_MAGNETIC = MIMaterialParts.create("wire_magnetic", "Magnetic Wire")
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
			NeutronBehaviour neutronBehaviour = NeutronBehaviour.of(NuclearConstant.ScatteringType.HEAVY, fuel, size);
			return new NuclearFuel(p.stacksTo(1), fuelParams, neutronBehaviour, c.registry().id("%s_fuel_rod_depleted".formatted(c.material().id().getPath())));
		};
	}
	
	MaterialPart FUEL_ROD_DEPLETED = MIMaterialParts.create("fuel_rod_depleted", "Depleted Fuel Rod")
			.formattingMaterialOnly("%s_fuel_rod_depleted"::formatted, "Depleted %s Fuel Rod"::formatted);
	
	MaterialPart FUEL_ROD = MIMaterialParts.create("fuel_rod", "Fuel Rod")
			.set(NUCLEAR_FUEL_SIZE, 1)
			.itemFactory(fuelRodFactory());
	
	MaterialPart FUEL_ROD_DOUBLE = MIMaterialParts.create("fuel_rod_double", "Double Fuel Rod")
			.set(NUCLEAR_FUEL_SIZE, 2)
			.itemFactory(fuelRodFactory());
	
	MaterialPart FUEL_ROD_QUAD = MIMaterialParts.create("fuel_rod_quad", "Quad Fuel Rod")
			.set(NUCLEAR_FUEL_SIZE, 4)
			.itemFactory(fuelRodFactory());
	
	MaterialPart[] ALL_FUEL_RODS = {FUEL_ROD, FUEL_ROD_DOUBLE, FUEL_ROD_QUAD, FUEL_ROD_DEPLETED};
	
	MaterialPart N_DOPED_PLATE = MIMaterialParts.create("n_doped_plate", "N-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "N-Doped %s Plate".formatted(m))
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart P_DOPED_PLATE = MIMaterialParts.create("p_doped_plate", "P-Doped Plate")
			.formatting("%s_%s"::formatted, (m, p) -> "P-Doped %s Plate".formatted(m))
			.itemModelBuilder(CommonModelBuilders::generated);
	
	MaterialPart[] ITEM_PURE_NON_METAL = {CRUSHED_DUST, DUST, TINY_DUST};
	MaterialPart[] ITEM_PURE_METAL     = {INGOT, NUGGET, DUST, TINY_DUST};
	
	static MaterialPart create(String id, String englishName)
	{
		return new MaterialPart(MI.id(id), englishName);
	}
}
