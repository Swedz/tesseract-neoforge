package net.swedz.tesseract.neoforge.compat.mi.material;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.api.energy.CableTier;
import net.minecraft.util.valueproviders.UniformInt;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.builtin.Materials;
import net.swedz.tesseract.neoforge.material.builtin.property.OrePartDrops;

import java.util.Optional;

import static net.swedz.tesseract.neoforge.compat.mi.material.part.MIMaterialParts.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.property.MIMaterialProperties.*;
import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;
import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public interface MIMaterials
{
	Material GOLD = Materials.GOLD.as(MI.ID)
			.addNative(BOLT, RING, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, PLATE, TINY_DUST)
			.addNative(DRILL_HEAD, DRILL);
	
	Material IRON = Materials.IRON.as(MI.ID)
			.addNative(BOLT, RING, GEAR, ROD, DOUBLE_INGOT, DUST, LARGE_PLATE, PLATE, TINY_DUST);
	
	Material COPPER = Materials.COPPER.as(MI.ID)
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE, FINE_WIRE)
			.addNative(DRILL_HEAD, DRILL);
	
	Material COAL = Materials.COAL.as(MI.ID)
			.addNative(ITEM_PURE_NON_METAL);
	
	Material DIAMOND = Materials.DIAMOND.as(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(PLATE, LARGE_PLATE);
	
	Material EMERALD = Materials.EMERALD.as(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(PLATE);
	
	Material LAPIS = Materials.LAPIS.as(MI.ID)
			.addNative(ITEM_PURE_NON_METAL);
	
	Material REDSTONE = Materials.REDSTONE.as(MI.ID)
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.LV))
			.addNative(TINY_DUST, CRUSHED_DUST)
			.addNative(BATTERY);
	
	Material QUARTZ = Materials.QUARTZ.as(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(ORE);
	
	Material BRICK = create("brick", "Brick")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.addNative("minecraft", INGOT.formattingMaterialOnly())
			.addNative(DUST, TINY_DUST);
	
	Material FIRE_CLAY = create("fire_clay", "Fire Clay")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.addNative(INGOT.formattingMaterialOnly("%s_brick"::formatted, "%s Brick"::formatted))
			.addNative(DUST, TINY_DUST);
	
	Material COKE = create("coke", "Coke")
			.set(MAIN_PART, GEM)
			.addNative(GEM, DUST, BLOCK);
	
	Material BRONZE = create("bronze", "Bronze")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL);
	
	Material TIN = create("tin", "Tin")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material STEEL = create("steel", "Steel")
			.addNative(BOLT, RING, ROD, GEAR, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(ROD_MAGNETIC)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL);
	
	Material LIGNITE_COAL = create("lignite_coal", "Lignite Coal")
			.set(MAIN_PART, GEM)
			.set(ORE_DROP_PART, OrePartDrops.experience(GEM, UniformInt.of(0, 2)))
			.addNative(GEM, BLOCK)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material ALUMINUM = create("aluminum", "Aluminum")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL);
	
	Material BAUXITE = create("bauxite", "Bauxite")
			.set(MAIN_PART, DUST)
			.set(ORE_DROP_PART, OrePartDrops.experience(DUST, UniformInt.of(1, 4)))
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material LEAD = create("lead", "Lead")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material BATTERY_ALLOY = create("battery_alloy", "Battery Alloy")
			.addNative(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, CURVED_PLATE, NUGGET)
			.addNative(BLOCK);
	
	Material INVAR = create("invar", "Invar")
			.addNative(TINY_DUST, DUST, INGOT, ROD, DOUBLE_INGOT, RING, BOLT, PLATE, NUGGET, GEAR)
			.addNative(LARGE_PLATE)
			.addNative(BLOCK);
	
	Material CUPRONICKEL = create("cupronickel", "Cupronickel")
			.addNative(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, WIRE, NUGGET, WIRE_MAGNETIC)
			.addNative(COIL)
			.addNative(BLOCK);
	
	Material ANTIMONY = create("antimony", "Antimony")
			.addNative(ITEM_PURE_METAL)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material NICKEL = create("nickel", "Nickel")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material SILVER = create("silver", "Silver")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK);
	
	Material SODIUM = create("sodium", "Sodium")
			.set(MAIN_PART, DUST)
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.HV))
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.addNative(BATTERY);
	
	Material SALT = create("salt", "Salt")
			.set(MAIN_PART, DUST)
			.set(ORE_DROP_PART, OrePartDrops.experience(DUST, UniformInt.of(1, 3)))
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material TITANIUM = create("titanium", "Titanium")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(HOT_INGOT)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(ORE);
	
	Material ELECTRUM = create("electrum", "Electrum")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(WIRE, FINE_WIRE);
	
	Material SILICON = create("silicon", "Silicon")
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.MV))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(N_DOPED_PLATE, P_DOPED_PLATE)
			.addNative(PLATE, DOUBLE_INGOT)
			.addNative(BATTERY);
	
	Material STAINLESS_STEEL = create("stainless_steel", "Stainless Steel")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(HOT_INGOT)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(ROD_MAGNETIC);
	
	Material RUBY = create("ruby", "Ruby")
			.set(MAIN_PART, DUST)
			.addNative(DUST, TINY_DUST);
	
	Material CARBON = create("carbon", "Carbon")
			.set(MAIN_PART, DUST)
			.addNative(DUST, TINY_DUST)
			.addNative(PLATE, LARGE_PLATE);
	
	Material CHROMIUM = create("chromium", "Chromium")
			.addNative(ITEM_PURE_METAL)
			.addNative(HOT_INGOT)
			.addNative(CRUSHED_DUST)
			.addNative(BLOCK)
			.addNative(PLATE, DOUBLE_INGOT);
	
	Material MANGANESE = create("manganese", "Manganese")
			.set(MAIN_PART, DUST)
			.addNative(ITEM_PURE_NON_METAL);
	
	Material BERYLLIUM = create("beryllium", "Beryllium")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK);
	
	Material ANNEALED_COPPER = create("annealed_copper", "Annealed Copper")
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(BLOCK);
	
	Material URANIUM_235 = create("uranium_235", "Uranium 235")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK);
	
	Material URANIUM_238 = create("uranium_238", "Uranium 238")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK);
	
	Material URANIUM = create("uranium", "Uranium")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material LE_URANIUM = create("le_uranium", "LE Uranium")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD);
	
	Material HE_URANIUM = create("he_uranium", "HE Uranium")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD);
	
	Material PLUTONIUM = create("plutonium", "Plutonium")
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.SUPERCONDUCTOR))
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(BATTERY);
	
	Material LE_MOX = create("le_mox", "LE Mox")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD);
	
	Material HE_MOX = create("he_mox", "HE Mox")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD);
	
	Material PLATINUM = create("platinum", "Platinum")
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, DOUBLE_INGOT, WIRE, FINE_WIRE, HOT_INGOT)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE);
	
	Material KANTHAL = create("kanthal", "Kanthal")
			.addNative(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(COIL)
			.addNative(BLOCK);
	
	Material IRIDIUM = create("iridium", "Iridium")
			.addNative(ITEM_PURE_METAL)
			.addNative(CURVED_PLATE)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material MONAZITE = create("monazite", "Monazite")
			.set(MAIN_PART, DUST)
			.set(ORE_DROP_PART, OrePartDrops.experience(DUST, UniformInt.of(1, 4)))
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material CADMIUM = create("cadmium", "Cadmium")
			.set(BATTERY_CAPACITY, batteryCapacity(CableTier.EV))
			.addNative(DUST, TINY_DUST, INGOT, PLATE, ROD, DOUBLE_INGOT)
			.addNative(BATTERY);
	
	Material NEODYMIUM = create("neodymium", "Neodymium")
			.set(MAIN_PART, DUST)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK);
	
	Material YTTRIUM = create("yttrium", "Yttrium")
			.set(MAIN_PART, DUST)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK);
	
	Material SUPERCONDUCTOR = create("superconductor", "Superconductor")
			.addNative(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(COIL);
	
	Material TUNGSTEN = create("tungsten", "Tungsten")
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, LARGE_PLATE, DOUBLE_INGOT)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE);
	
	Material BLASTPROOF_ALLOY = create("blastproof_alloy", "Blastproof Alloy")
			.addNative(INGOT, PLATE, LARGE_PLATE, CURVED_PLATE);
	
	Material NUCLEAR_ALLOY = create("nuclear_alloy", "Nuclear Alloy")
			.addNative(PLATE, LARGE_PLATE);
	
	Material SOLDERING_ALLOY = create("soldering_alloy", "Soldering Alloy")
			.set(MAIN_PART, DUST)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK);
	
	Material SULFUR = create("sulfur", "Sulfur")
			.set(MAIN_PART, DUST)
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK);
	
	static Material create(String id, String englishName)
	{
		return new Material(MI.id(id), englishName);
	}
}
