package net.swedz.tesseract.neoforge.compat.mi.material.recipe;

import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.MIItem;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.swedz.tesseract.neoforge.material.builtin.recipe.VanillaMaterialRecipeContext;
import net.swedz.tesseract.neoforge.material.builtin.recipe.VanillaMaterialRecipeGroups;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeGroup;

import static aztech.modern_industrialization.machines.init.MIMachineRecipeTypes.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.part.MIMaterialParts.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;

public interface MIMaterialRecipeGroups
{
	MaterialRecipeGroup<VanillaMaterialRecipeContext> STANDARD = VanillaMaterialRecipeGroups.STANDARD
			.add("tiny_dust_to_dust", (c) -> c.shapeless3x3(TINY_DUST, DUST, true))
			.add("deepslate_to_ore", (c) -> c.shapeless(ORE_DEEPSLATE, 1, ORE, 1, false))
			.add("netherrack_to_ore", (c) -> c.shapeless(ORE_NETHERRACK, 1, ORE, 1, false))
			.add("blade", (c) -> c.shaped(BLADE, 4, (r) -> r.add('P', CURVED_PLATE).add('R', ROD), "P", "P", "I"))
			.add("coil", (c) -> c.shaped(COIL, 1, (r) -> r.add('X', CABLE), "XXX", "X X", "XXX"))
			.add("large_plate", (c) -> c.shaped(LARGE_PLATE, 1, (r) -> r.add('X', PLATE), "XX", "XX"))
			.add("rotor", (c) -> c.shaped(ROTOR, 1, (r) -> r.add('b', BOLT).add('B', BLADE), "bBb", "BRB", "bBb"))
			.add("gear", (c) -> c.shaped(GEAR, 1, (r) -> r.add('b', BOLT).add('P', PLATE), "PbP", "bRb", "PbP"))
			.add("hammer", (c) -> c.shaped(HAMMER, 1, (r) -> r.add('p', LARGE_PLATE).add('s', Items.STICK), "ppp", "psp", " s "))
			.add("ring", (c) -> c.shaped(RING, 2, (r) -> r.add('b', BOLT).add('R', ROD), "bRb", "R R", "bRb"))
			.add("cable", (c) -> c.shaped(CABLE, 3, (r) -> r.add('r', MIItem.RUBBER_SHEET).add('w', WIRE), "rrr", "www", "rrr"))
			.add("barrel", (c) -> c.shaped(BARREL, 1, (r) -> r.add('#', PLATE).add('b', Tags.Items.BARRELS_WOODEN), "###", "#b#", "###"))
			.add("tank", (c) -> c.shaped(TANK, 1, (r) -> r.add('#', PLATE).add('g', Tags.Items.GLASS_BLOCKS), "###", "#g#", "###"))
			.add("drill_head", (c) -> c.shaped(DRILL_HEAD, 1, (r) -> r.add('G', GEAR).add('b', BOLT).add('c', CURVED_PLATE).add('R', ROD).add('p', PLATE), "bcp", "GRc", "bGb"));
	
	MaterialRecipeGroup<MIMachineMaterialRecipeContext> STANDARD_MACHINES = MaterialRecipeGroup.create(MIMachineMaterialRecipeContext::new)
			.add("nugget_to_ingot", (c) -> c.packAndUnpack(NUGGET, 9, INGOT, 1))
			.add("tiny_dust_to_dust", (c) -> c.packAndUnpack(TINY_DUST, 9, DUST, 1))
			.add("raw_metal_to_block", (c) -> c.packAndUnpack(RAW_METAL, 9, RAW_METAL_BLOCK, 1))
			.add("blade", (c) -> c.machine(PACKER, BLADE, 4, (b) -> b.addPartInput(CURVED_PLATE, 2).addPartInput(ROD, 1)))
			.add("coil", (c) -> c.machine(ASSEMBLER, CABLE, 8, COIL, 1))
			.add("large_plate", (c) -> c.machine(PACKER, PLATE, 4, LARGE_PLATE, 1))
			.add("cable_rubber", (c) -> c.machine(PACKER, CABLE, 3, (b) -> b.addItemInput(MIItem.RUBBER_SHEET, 6).addPartInput(WIRE, 3)))
			.add("barrel", (c) -> c.machine(ASSEMBLER, BARREL, 1, (b) -> b.addPartInput(PLATE, 8).addItemInput(Tags.Items.BARRELS_WOODEN, 1)))
			.add("tank", (c) -> c.machine(ASSEMBLER, TANK, 1, (b) -> b.addPartInput(PLATE, 8).addItemInput(Tags.Items.GLASS_BLOCKS, 1)))
			
			.add("recycle_double_ingot", (c) -> c.maceratorRecycling(DOUBLE_INGOT, 18))
			.add("recycle_plate", (c) -> c.maceratorRecycling(PLATE, 9))
			.add("recycle_curved_plate", (c) -> c.maceratorRecycling(CURVED_PLATE, 9))
			.add("recycle_nugget", (c) -> c.maceratorRecycling(NUGGET, 1))
			.add("recycle_large_plate", (c) -> c.maceratorRecycling(LARGE_PLATE, 36))
			.add("recycle_gear", (c) -> c.maceratorRecycling(GEAR, 18))
			.add("recycle_ring", (c) -> c.maceratorRecycling(RING, 4))
			.add("recycle_bolt", (c) -> c.maceratorRecycling(BOLT, 2))
			.add("recycle_rod", (c) -> c.maceratorRecycling(ROD, 4))
			.add("recycle_rotor", (c) -> c.maceratorRecycling(ROTOR, 27))
			.add("recycle_main_part", (c) ->
			{
				if(!c.mainPart().equals(DUST))
				{
					c.maceratorRecycling(c.mainPart(), 9);
				}
			})
			.add("recycle_blade", (c) -> c.maceratorRecycling(BLADE, 5))
			.add("recycle_drill_head", (c) -> c.maceratorRecycling(DRILL_HEAD, 7 * 9 + 4))
			.add("recycle_wire", (c) -> c.maceratorRecycling(WIRE, 4))
			
			.add("ore_to_crushed_dust", (c) -> c.machine("ore_to_crushed", MACERATOR, ORE, 1, CRUSHED_DUST, 3))
			.add("ore_to_raw_metal", (c) -> c.machine("ore_to_raw", MACERATOR, ORE, 1, RAW_METAL, 3))
			.add("crushed_dust_to_dust", (c) -> c.machine("crushed_dust", MACERATOR, DUST, 1, (b) -> b.addPartInput(CRUSHED_DUST, 1).addPartOutput(DUST, 1, 0.5f)))
			.add("raw_metal_to_dust", (c) -> c.machine("raw_metal", MACERATOR, DUST, 1, (b) -> b.addPartInput(RAW_METAL, 1).addPartOutput(DUST, 1, 0.5f)))
			
			.add("main_to_plate", (c) -> c.machine("main", COMPRESSOR, c.mainPart(), 1, PLATE, 1))
			.add("plate_to_curved_plate", (c) -> c.machine(COMPRESSOR, PLATE, 1, CURVED_PLATE, 1))
			.add("double_ingot_to_plate", (c) -> c.machine(COMPRESSOR, DOUBLE_INGOT, 1, PLATE, 2))
			.add("rod_to_ring", (c) -> c.machine(COMPRESSOR, ROD, 1, RING, 1))
			
			.add("main_to_rod", (c) -> c.cuttingMachine(c.mainPart(), 1, ROD, 2))
			.add("double_ingot_to_rod", (c) -> c.cuttingMachine(DOUBLE_INGOT, 1, ROD, 4))
			.add("rod_to_bolt", (c) -> c.cuttingMachine(ROD, 1, BOLT, 2))
			
			.add("main_to_block", (c) -> c.machine(PACKER, BLOCK, 1, (b) -> b.addPartInput(c.mainPart(), 9).addItemInput(MIItem.PACKER_BLOCK_TEMPLATE, 1, 0f)))
			.add("ingot_to_double_ingot", (c) -> c.machine(PACKER, DOUBLE_INGOT, 1, (b) -> b.addPartInput(INGOT, 2).addItemInput(MIItem.PACKER_DOUBLE_INGOT_TEMPLATE, 1, 0f)))
			
			.add("fuel_rod_double", (c) -> c.machine(PACKER, FUEL_ROD_DOUBLE, 1, (b) -> b.addPartInput(FUEL_ROD, 2).addItemInput("#c:plates/nuclear_alloy", 1)))
			.add("fuel_rod_quad", (c) -> c.machine(PACKER, FUEL_ROD_QUAD, 1, (b) -> b.addPartInput(FUEL_ROD_DOUBLE, 2).addItemInput("#c:plates/nuclear_alloy", 2)))
			
			.add("coil_to_cable", (c) -> c.machine(UNPACKER, COIL, 1, CABLE, 8))
			
			.add("plate_to_wire", (c) -> c.machine(WIREMILL, PLATE, 1, WIRE, 2))
			.add("wire_to_fine_wire", (c) -> c.machine(WIREMILL, WIRE, 1, FINE_WIRE, 4))
			
			.add("rotor", (c) -> c.machine(ASSEMBLER, ROTOR, 1, (b) -> b.addPartInput(BLADE, 4).addPartInput(RING, 1).addFluidInput(MIFluids.SOLDERING_ALLOY, 100)))
			.add("gear", (c) -> c.machine(ASSEMBLER, GEAR, 2, (b) -> b.addPartInput(PLATE, 4).addPartInput(RING, 1).addFluidInput(MIFluids.SOLDERING_ALLOY, 100)))
			.add("drill_head", (c) -> c.machine(ASSEMBLER, DRILL_HEAD, 1, (b) -> b.addPartInput(PLATE, 1).addPartInput(CURVED_PLATE, 2).addPartInput(ROD, 1).addPartInput(GEAR, 2).addFluidInput(MIFluids.SOLDERING_ALLOY, 75)))
			.add("cable_synthetic_rubber", (c) -> c.machine("cable_synthetic_rubber", ASSEMBLER, CABLE, 3, (b) -> b.addPartInput(WIRE, 3).addFluidInput(MIFluids.SYNTHETIC_RUBBER, 30)))
			.add("cable_styrene_rubber", (c) -> c.machine("cable_styrene_rubber", ASSEMBLER, CABLE, 3, (b) -> b.addPartInput(WIRE, 3).addFluidInput(MIFluids.STYRENE_BUTADIENE_RUBBER, 6)))
			
			.add("fuel_rod", (c) -> c.machine(ASSEMBLER, 16, 200, FUEL_ROD, 1, (b) -> b.addItemInput("modern_industrialization:blastproof_alloy_curved_plate", 2).addItemInput(MIItem.LARGE_MOTOR, 1).addItemInput(MIItem.ROBOT_ARM, 2).addPartInput(ROD, 18).addFluidInput(MIFluids.SOLDERING_ALLOY, 500).addFluidInput(MIFluids.HELIUM, 100)))
			
			.add("rod_to_magnetic", (c) -> c.machine(POLARIZER, 8, 200, ROD_MAGNETIC, 1, (b) -> b.addPartInput(ROD, 1)))
			.add("wire_to_magnetic", (c) -> c.machine(POLARIZER, 8, 200, WIRE_MAGNETIC, 1, (b) -> b.addPartInput(WIRE, 1)))
			
			.add("vacuum_freezer_cool_ingot", (c) -> c.machine("hot_ingot", VACUUM_FREEZER, 32, 250, INGOT, 1, (b) -> b.addPartInput(HOT_INGOT, 1)))
			.add("heat_exchanger_cool_ingot", (c) -> c.machine("hot_ingot", HEAT_EXCHANGER, 8, 10, INGOT, 1, (b) -> b.addPartInput(HOT_INGOT, 1).addFluidInput(MIFluids.CRYOFLUID, 100).addFluidOutput(MIFluids.ARGON, 65).addFluidOutput(MIFluids.HELIUM, 25)));
	
	MaterialRecipeGroup<MIMachineMaterialRecipeContext> DUST_TO_GEM = MaterialRecipeGroup.create(MIMachineMaterialRecipeContext::new)
			.add("dust_to_gem", (c) -> c.machine(c.material().id().getPath(), COMPRESSOR, DUST, 1, GEM, 1));
	
	MaterialRecipeGroup<VanillaMaterialRecipeContext> SMELTING = VanillaMaterialRecipeGroups.SMELTING
			.add("tiny_dust_to_nugget", (c) -> c.smeltingAndBlasting(TINY_DUST, NUGGET, 0.08f))
			.add("crushed_dust_to_ingot", (c) -> c.smeltingAndBlasting(CRUSHED_DUST, INGOT, 0.7f))
			.add("dust_to_ingot", (c) -> c.smeltingAndBlasting(DUST, INGOT, 0.7f))
			.add("rod_magnetic_to_rod", (c) -> c.smelting(ROD_MAGNETIC, ROD, 0f))
			.add("wire_magnetic_to_wire", (c) -> c.smelting(WIRE_MAGNETIC, WIRE, 0f));
	
	static MaterialRecipeGroup<MIMachineMaterialRecipeContext> blastFurnace(boolean hotIngot, int eu, int duration)
	{
		MaterialRecipeGroup<MIMachineMaterialRecipeContext> recipeGroup = MaterialRecipeGroup.create(MIMachineMaterialRecipeContext::new);
		if(hotIngot)
		{
			recipeGroup = recipeGroup.add("dust_to_hot_ingot", (c) -> c.machine("dust", BLAST_FURNACE, eu, duration, DUST, 1, HOT_INGOT, 1));
		}
		else
		{
			recipeGroup = recipeGroup
					.add("dust_to_ingot", (c) -> c.machine("dust", BLAST_FURNACE, eu, duration, DUST, 1, INGOT, 1))
					.add("tiny_dust_to_nugget", (c) -> c.machine("tiny_dust", BLAST_FURNACE, eu, duration / 10, TINY_DUST, 1, NUGGET, 1));
		}
		return recipeGroup;
	}
	
	static MaterialRecipeGroup<MIMachineMaterialRecipeContext> blastFurnace(boolean hotIngot, int eu)
	{
		return blastFurnace(hotIngot, eu, 200);
	}
	
	static MaterialRecipeGroup<MIMachineMaterialRecipeContext> blastFurnace(int eu)
	{
		return blastFurnace(false, eu);
	}
	
	static MaterialRecipeGroup<MIMachineMaterialRecipeContext> blastFurnace()
	{
		return blastFurnace(32);
	}
	
	MaterialRecipeGroup<MIForgeHammerMaterialRecipeContext> FORGE_HAMMER = MaterialRecipeGroup.create(MIForgeHammerMaterialRecipeContext::new)
			.add("ingot_to_dust", (c) -> c.hammer(INGOT, 1, DUST, 1))
			.add("nugget_to_tiny_dust", (c) -> c.hammer(NUGGET, 1, TINY_DUST, 1))
			.add("ingot_to_double_ingot", (c) -> c.hammer(INGOT, 2, DOUBLE_INGOT, 1))
			.add("ingot_to_plate_with_tool", (c) -> c.hammer(INGOT, 1, PLATE, 1, 20))
			.add("ingot_to_plate", (c) -> c.hammer(INGOT, 2, PLATE, 1))
			.add("ingot_to_curved_plate_with_tool", (c) -> c.hammer(INGOT, 1, CURVED_PLATE, 1, 40))
			.add("ingot_to_curved_plate", (c) -> c.hammer(INGOT, 2, CURVED_PLATE, 1))
			.add("ingot_to_rod_with_tool", (c) -> c.hammer(INGOT, 1, ROD, 2, 20))
			.add("ingot_to_rod", (c) -> c.hammer(INGOT, 1, ROD, 1))
			.add("ingot_to_ring_with_tool", (c) -> c.hammer(INGOT, 1, RING, 2, 60))
			.add("ingot_to_ring", (c) -> c.hammer(INGOT, 1, RING, 1))
			.add("ingot_to_bolt_with_tool", (c) -> c.hammer(INGOT, 1, BOLT, 4, 60))
			.add("ingot_to_bolt", (c) -> c.hammer(INGOT, 1, BOLT, 2))
			.add("double_ingot_to_plate_with_tool", (c) -> c.hammer(DOUBLE_INGOT, 1, PLATE, 2, 20))
			.add("double_ingot_to_plate", (c) -> c.hammer(DOUBLE_INGOT, 1, PLATE, 1))
			.add("double_ingot_to_curved_plate_with_tool", (c) -> c.hammer(DOUBLE_INGOT, 1, CURVED_PLATE, 2, 60))
			.add("double_ingot_to_curved_plate", (c) -> c.hammer(DOUBLE_INGOT, 1, CURVED_PLATE, 1))
			.add("double_ingot_to_rod_with_tool", (c) -> c.hammer(DOUBLE_INGOT, 1, ROD, 4, 20))
			.add("double_ingot_to_rod", (c) -> c.hammer(DOUBLE_INGOT, 1, ROD, 2))
			.add("double_ingot_to_ring_with_tool", (c) -> c.hammer(DOUBLE_INGOT, 1, RING, 4, 100))
			.add("double_ingot_to_ring", (c) -> c.hammer(DOUBLE_INGOT, 1, RING, 2))
			.add("double_ingot_to_bolt_with_tool", (c) -> c.hammer(DOUBLE_INGOT, 1, BOLT, 8, 100))
			.add("double_ingot_to_bolt", (c) -> c.hammer(DOUBLE_INGOT, 1, BOLT, 4))
			.add("plate_to_curved_plate_with_tool", (c) -> c.hammer(PLATE, 1, CURVED_PLATE, 1, 20))
			.add("rod_to_bolt_with_tool", (c) -> c.hammer(ROD, 1, BOLT, 2, 20))
			.add("rod_to_ring_with_tool", (c) -> c.hammer(ROD, 1, RING, 1, 20))
			.add("ore_to_crushed_dust_with_tool", (c) -> c.hammer(ORE, 1, CRUSHED_DUST, 3, 20))
			.add("ore_to_crushed_dust", (c) -> c.hammer(ORE, 1, CRUSHED_DUST, 2))
			.add("ore_to_raw_metal_with_tool", (c) -> c.hammer(ORE, 1, RAW_METAL, 3, 20))
			.add("ore_to_raw_metal", (c) -> c.hammer(ORE, 1, RAW_METAL, 2))
			.add("ore_to_dust_with_tool", (c) -> c.hammer(ORE, 1, DUST, 4, 50))
			.add("raw_metal_to_dust_with_tool", (c) -> c.hammer(RAW_METAL, 3, DUST, 4, 30))
			.add("crushed_dust_to_dust_with_tool", (c) -> c.hammer(CRUSHED_DUST, 3, DUST, 4, 30));
}
