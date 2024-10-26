package net.swedz.tesseract.neoforge.compat.mi.material;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import net.minecraft.util.valueproviders.UniformInt;
import net.swedz.tesseract.neoforge.compat.mi.material.property.IsotopeFuel;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.builtin.Materials;
import net.swedz.tesseract.neoforge.material.builtin.property.OrePartDrops;

import java.util.Optional;

import static net.swedz.tesseract.neoforge.compat.mi.material.part.MIMaterialParts.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.property.MIMaterialProperties.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.property.TimeFactor.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.recipe.MIMaterialRecipeGroups.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;
import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public interface MIMaterials
{
	Material GOLD = Materials.GOLD.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, SOFT)
			.addNative(BOLT, RING, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, PLATE, TINY_DUST)
			.addNative(DRILL_HEAD, DRILL)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING, FORGE_HAMMER);
	
	Material IRON = Materials.IRON.as(MI.ID).clearRecipes()
			.addNative(BOLT, RING, GEAR, ROD, DOUBLE_INGOT, DUST, LARGE_PLATE, PLATE, TINY_DUST)
			.addNative(HAMMER)
			.addNative(BARREL.set(BARREL_CAPACITY, 1L))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING, FORGE_HAMMER);
	
	Material COPPER = Materials.COPPER.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, SOFT)
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE, FINE_WIRE)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(CABLE.set(CABLE_TIER, CableTier.LV))
			.recipes(STANDARD, STANDARD_MACHINES.without("ore_to_raw_metal"), SMELTING, FORGE_HAMMER.without("ore_to_raw_metal", "ore_to_raw_metal_with_tool", "ore_to_dust_with_tool"));
	
	Material COAL = Materials.COAL.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, SOFT)
			.addNative(ITEM_PURE_NON_METAL)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING, DUST_TO_GEM);
	
	Material DIAMOND = Materials.DIAMOND.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, VERY_HARD)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(PLATE, LARGE_PLATE)
			.addNative(HAMMER)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material EMERALD = Materials.EMERALD.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, VERY_HARD)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(PLATE)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material LAPIS = Materials.LAPIS.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, SOFT)
			.addNative(ITEM_PURE_NON_METAL)
			.recipes(STANDARD, STANDARD_MACHINES.without("ore_to_crushed_dust"), SMELTING, DUST_TO_GEM);
	
	Material REDSTONE = Materials.REDSTONE.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, SOFT)
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.LV))
			.addNative(TINY_DUST, CRUSHED_DUST)
			.addNative(BATTERY)
			.recipes(STANDARD, STANDARD_MACHINES.without("ore_to_crushed_dust"), SMELTING);
	
	Material QUARTZ = Materials.QUARTZ.as(MI.ID).clearRecipes()
			.set(TIME_FACTOR, SOFT)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(ORE)
			.recipes(STANDARD, STANDARD_MACHINES.without("ore_to_crushed_dust"), SMELTING, DUST_TO_GEM);
	
	Material BRICK = create("brick", "Brick")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.set(TIME_FACTOR, SOFT)
			.addNative("minecraft", INGOT.formattingMaterialOnly())
			.addNative(DUST, TINY_DUST)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material FIRE_CLAY = create("fire_clay", "Fire Clay")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.set(TIME_FACTOR, SOFT)
			.addNative(INGOT.formattingMaterialOnly("%s_brick"::formatted, "%s Brick"::formatted))
			.addNative(DUST)
			.recipes(SMELTING);
	
	Material COKE = create("coke", "Coke")
			.set(MAIN_PART, GEM)
			.set(TIME_FACTOR, SOFT)
			.addNative(GEM, DUST, BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES, DUST_TO_GEM);
	
	Material BRONZE = create("bronze", "Bronze")
			.set(TIME_FACTOR, SOFT)
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(MACHINE_CASING, MACHINE_CASING_PIPE, PLATED_BRICKS)
			.addNative(BARREL.set(BARREL_CAPACITY, 32L))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING, FORGE_HAMMER);
	
	Material TIN = create("tin", "Tin")
			.set(TIME_FACTOR, SOFT)
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.addNative(CABLE.set(CABLE_TIER, CableTier.LV))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING, FORGE_HAMMER);
	
	Material STEEL = create("steel", "Steel")
			.addNative(BOLT, RING, ROD, GEAR, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(ROD_MAGNETIC)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(HAMMER)
			.addNative(MACHINE_CASING, MACHINE_CASING_PIPE)
			.addNative(BARREL.set(BARREL_CAPACITY, 128L))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING, FORGE_HAMMER);
	
	Material LIGNITE_COAL = create("lignite_coal", "Lignite Coal")
			.set(MAIN_PART, GEM)
			.set(TIME_FACTOR, SOFT)
			.set(ORE_DROP_PART, OrePartDrops.experience(GEM, UniformInt.of(0, 2)))
			.addNative(GEM, BLOCK)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES.without("crushed_dust_to_dust"), SMELTING, FORGE_HAMMER, DUST_TO_GEM);
	
	Material ALUMINUM = create("aluminum", "Aluminum")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(CABLE.set(CABLE_TIER, CableTier.HV))
			.addNative(MACHINE_CASING.formattingRaw("advanced_machine_casing", "Advanced Machine Casing"))
			.addNative(MACHINE_CASING_SPECIAL.formattingRaw("frostproof_machine_casing", "Frostproof Machine Casing"))
			.addNative(BARREL.set(BARREL_CAPACITY, 512L))
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace());
	
	Material BAUXITE = create("bauxite", "Bauxite")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.set(ORE_DROP_PART, OrePartDrops.experience(DUST, UniformInt.of(1, 4)))
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material LEAD = create("lead", "Lead")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES.without("raw_metal_to_dust"), SMELTING);
	
	Material BATTERY_ALLOY = create("battery_alloy", "Battery Alloy")
			.set(TIME_FACTOR, SOFT)
			.addNative(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, CURVED_PLATE, NUGGET)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material INVAR = create("invar", "Invar")
			.addNative(TINY_DUST, DUST, INGOT, ROD, DOUBLE_INGOT, RING, BOLT, PLATE, NUGGET, GEAR)
			.addNative(LARGE_PLATE)
			.addNative(BLOCK)
			.addNative(MACHINE_CASING_SPECIAL.formattingRaw("heatproof_machine_casing", "Heatproof Machine Casing"))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material CUPRONICKEL = create("cupronickel", "Cupronickel")
			.set(TIME_FACTOR, SOFT)
			.addNative(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, WIRE, NUGGET, WIRE_MAGNETIC)
			.addNative(COIL)
			.addNative(BLOCK)
			.addNative(CABLE.set(CABLE_TIER, CableTier.MV))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material ANTIMONY = create("antimony", "Antimony")
			.set(TIME_FACTOR, SOFT)
			.addNative(ITEM_PURE_METAL)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material NICKEL = create("nickel", "Nickel")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material SILVER = create("silver", "Silver")
			.set(TIME_FACTOR, SOFT)
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.addNative(CABLE.set(CABLE_TIER, CableTier.LV))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material SODIUM = create("sodium", "Sodium")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.HV))
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.addNative(BATTERY)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material SALT = create("salt", "Salt")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.set(ORE_DROP_PART, OrePartDrops.experience(DUST, UniformInt.of(1, 3)))
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material TITANIUM = create("titanium", "Titanium")
			.set(TIME_FACTOR, HARD)
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(HOT_INGOT)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(ORE)
			.addNative(MACHINE_CASING.formattingRaw("highly_advanced_machine_casing", "Highly Advanced Machine Casing"))
			.addNative(MACHINE_CASING_PIPE, SOLID_MACHINE_CASING)
			.addNative(BARREL.set(BARREL_CAPACITY, 8192L))
			.recipes(STANDARD, STANDARD_MACHINES.without("raw_metal_to_dust"), blastFurnace(true, 128, 400));
	
	Material ELECTRUM = create("electrum", "Electrum")
			.set(TIME_FACTOR, SOFT)
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(WIRE, FINE_WIRE)
			.addNative(CABLE.set(CABLE_TIER, CableTier.MV))
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material SILICON = create("silicon", "Silicon")
			.set(TIME_FACTOR, SOFT)
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.MV))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(N_DOPED_PLATE, P_DOPED_PLATE)
			.addNative(PLATE, DOUBLE_INGOT)
			.addNative(BATTERY)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material STAINLESS_STEEL = create("stainless_steel", "Stainless Steel")
			.set(TIME_FACTOR, HARD)
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(HOT_INGOT)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(ROD_MAGNETIC)
			.addNative(MACHINE_CASING.formattingRaw("turbo_machine_casing", "Turbo Machine Casing"))
			.addNative(MACHINE_CASING_PIPE, CLEAN_MACHINE_CASING)
			.addNative(BARREL.set(BARREL_CAPACITY, 2048L))
			.recipes(STANDARD, STANDARD_MACHINES.without("rod_to_magnetic"), blastFurnace(true, 32, 400));
	
	Material RUBY = create("ruby", "Ruby")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, HARD)
			.addNative(DUST, TINY_DUST)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material CARBON = create("carbon", "Carbon")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.addNative(DUST, TINY_DUST)
			.addNative(PLATE, LARGE_PLATE)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material CHROMIUM = create("chromium", "Chromium")
			.addNative(ITEM_PURE_METAL)
			.addNative(HOT_INGOT)
			.addNative(CRUSHED_DUST)
			.addNative(BLOCK)
			.addNative(PLATE, DOUBLE_INGOT)
			.recipes(STANDARD, STANDARD_MACHINES.without("crushed_dust_to_dust"), blastFurnace(true, 32, 400));
	
	Material MANGANESE = create("manganese", "Manganese")
			.set(MAIN_PART, DUST)
			.addNative(ITEM_PURE_NON_METAL)
			.recipes(STANDARD, STANDARD_MACHINES.without("crushed_dust_to_dust"));
	
	Material BERYLLIUM = create("beryllium", "Beryllium")
			.set(TIME_FACTOR, HARD)
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material ANNEALED_COPPER = create("annealed_copper", "Annealed Copper")
			.set(TIME_FACTOR, SOFT)
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(BLOCK)
			.addNative(CABLE.set(CABLE_TIER, CableTier.EV))
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(64));
	
	Material URANIUM_235 = create("uranium_235", "Uranium 235")
			.set(TIME_FACTOR, VERY_HARD)
			.set(ISOTOPE, new IsotopeFuel(0.6, 0.35, 2400, 900, 2300, 8, 0.5))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material URANIUM_238 = create("uranium_238", "Uranium 238")
			.set(TIME_FACTOR, SOFT)
			.set(ISOTOPE, new IsotopeFuel(0.6, 0.30, 3200, 1000, 3000, 6, 0.3))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material URANIUM = create("uranium", "Uranium")
			.set(ISOTOPE, IsotopeFuel.mix(URANIUM_238, URANIUM_235, 1D / 81))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.addNative(ALL_FUEL_RODS)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material LE_URANIUM = create("le_uranium", "LE Uranium")
			.set(TIME_FACTOR, VERY_HARD)
			.set(ISOTOPE, IsotopeFuel.mix(URANIUM_238, URANIUM_235, 1D / 9))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.addNative(ALL_FUEL_RODS)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material HE_URANIUM = create("he_uranium", "HE Uranium")
			.set(TIME_FACTOR, VERY_HARD)
			.set(ISOTOPE, IsotopeFuel.mix(URANIUM_238, URANIUM_235, 1D / 3))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.addNative(ALL_FUEL_RODS)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material PLUTONIUM = create("plutonium", "Plutonium")
			.set(TIME_FACTOR, VERY_HARD)
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.SUPERCONDUCTOR))
			.set(ISOTOPE, new IsotopeFuel(0.9, 0.25, 2100, 600, 2000, 9, 0.25))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(BATTERY)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material LE_MOX = create("le_mox", "LE Mox")
			.set(TIME_FACTOR, VERY_HARD)
			.set(ISOTOPE, IsotopeFuel.mix(URANIUM_238, PLUTONIUM, 1D / 9))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.addNative(ALL_FUEL_RODS)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material HE_MOX = create("he_mox", "HE Mox")
			.set(TIME_FACTOR, VERY_HARD)
			.set(ISOTOPE, IsotopeFuel.mix(URANIUM_238, PLUTONIUM, 1D / 3))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.addNative(ALL_FUEL_RODS)
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(128));
	
	Material PLATINUM = create("platinum", "Platinum")
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, DOUBLE_INGOT, WIRE, FINE_WIRE, HOT_INGOT)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE)
			.addNative(CABLE.set(CABLE_TIER, CableTier.EV))
			.recipes(STANDARD, STANDARD_MACHINES.without("raw_metal_to_dust"), blastFurnace(true, 128, 600));
	
	Material KANTHAL = create("kanthal", "Kanthal")
			.set(TIME_FACTOR, HARD)
			.addNative(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(COIL)
			.addNative(BLOCK)
			.addNative(CABLE.set(CABLE_TIER, CableTier.HV))
			.recipes(STANDARD, STANDARD_MACHINES, blastFurnace(true, 32, 400));
	
	Material IRIDIUM = create("iridium", "Iridium")
			.set(TIME_FACTOR, VERY_HARD)
			.addNative(ITEM_PURE_METAL)
			.addNative(CURVED_PLATE)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.addNative(MACHINE_CASING.formattingRaw("quantum_machine_casing", "Quantum Machine Casing").set(BLAST_RESISTANCE, 6000f))
			.addNative(MACHINE_CASING_PIPE.set(BLAST_RESISTANCE, 6000f))
			.addNative(PLASMA_HANDLING_MACHINE_CASING.set(BLAST_RESISTANCE, 6000f))
			.addNative(BARREL.set(BARREL_CAPACITY, (long) Integer.MAX_VALUE).formattingRaw("quantum_barrel", "Quantum Barrel"))
			// TODO without tank
			.recipes(STANDARD.without("barrel"), STANDARD_MACHINES.without("main_to_plate", "barrel"), SMELTING);
	
	Material MONAZITE = create("monazite", "Monazite")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.set(ORE_DROP_PART, OrePartDrops.experience(DUST, UniformInt.of(1, 4)))
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material CADMIUM = create("cadmium", "Cadmium")
			.set(TIME_FACTOR, SOFT)
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.EV))
			.addNative(DUST, TINY_DUST, INGOT, PLATE, ROD, DOUBLE_INGOT)
			.addNative(BATTERY)
			.recipes(STANDARD, STANDARD_MACHINES, SMELTING);
	
	Material NEODYMIUM = create("neodymium", "Neodymium")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material YTTRIUM = create("yttrium", "Yttrium")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material SUPERCONDUCTOR = create("superconductor", "Superconductor")
			.set(TIME_FACTOR, HARD)
			.addNative(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(COIL)
			.addNative(CABLE.set(CABLE_TIER, CableTier.SUPERCONDUCTOR))
			.recipes(STANDARD.without("cable"), STANDARD_MACHINES.without("cable_rubber", "cable_synthetic_rubber", "cable_styrene_rubber"));
	
	Material TUNGSTEN = create("tungsten", "Tungsten")
			.set(TIME_FACTOR, VERY_HARD)
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, LARGE_PLATE, DOUBLE_INGOT)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material BLASTPROOF_ALLOY = create("blastproof_alloy", "Blastproof Alloy")
			.set(TIME_FACTOR, VERY_HARD)
			.addNative(INGOT, PLATE, LARGE_PLATE, CURVED_PLATE)
			.addNative(MACHINE_CASING_SPECIAL.formattingRaw("blastproof_casing", "Blastproof Casing").set(BLAST_RESISTANCE, 6000f))
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material NUCLEAR_ALLOY = create("nuclear_alloy", "Nuclear Alloy")
			.set(TIME_FACTOR, VERY_HARD)
			.addNative(PLATE, LARGE_PLATE)
			.addNative(MACHINE_CASING_PIPE.set(BLAST_RESISTANCE, 6000f))
			.addNative(MACHINE_CASING_SPECIAL.formattingMaterialOnly("nuclear_casing"::formatted, "%s Casing"::formatted))
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material SOLDERING_ALLOY = create("soldering_alloy", "Soldering Alloy")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	Material SULFUR = create("sulfur", "Sulfur")
			.set(MAIN_PART, DUST)
			.set(TIME_FACTOR, SOFT)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.recipes(STANDARD, STANDARD_MACHINES);
	
	static Material create(String id, String englishName)
	{
		return new Material(MI.id(id), englishName);
	}
}
