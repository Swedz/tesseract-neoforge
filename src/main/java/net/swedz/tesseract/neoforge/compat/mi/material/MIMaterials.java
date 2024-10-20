package net.swedz.tesseract.neoforge.compat.mi.material;

import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.VanillaMaterials;

import java.util.Optional;

import static net.swedz.tesseract.neoforge.compat.mi.material.MIMaterial.*;
import static net.swedz.tesseract.neoforge.compat.mi.material.part.MIMaterialParts.*;
import static net.swedz.tesseract.neoforge.material.part.VanillaMaterialParts.*;
import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public interface MIMaterials
{
	Material GOLD = defer(VanillaMaterials.GOLD)
			.add(BOLT, RING, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, PLATE, TINY_DUST)
			.add(DRILL_HEAD, DRILL);
	
	Material IRON = defer(VanillaMaterials.IRON)
			.add(BOLT, RING, GEAR, ROD, DOUBLE_INGOT, DUST, LARGE_PLATE, PLATE, TINY_DUST);
	
	Material COPPER = defer(VanillaMaterials.COPPER)
			.add(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, NUGGET, PLATE, TINY_DUST)
			.add(WIRE, FINE_WIRE)
			.add(DRILL_HEAD, DRILL);
	
	Material COAL = defer(VanillaMaterials.COAL)
			.add(ITEM_PURE_NON_METAL);
	
	Material DIAMOND = defer(VanillaMaterials.DIAMOND)
			.add(ITEM_PURE_NON_METAL)
			.add(PLATE, LARGE_PLATE);
	
	Material EMERALD = defer(VanillaMaterials.EMERALD)
			.add(ITEM_PURE_NON_METAL)
			.add(PLATE);
	
	Material LAPIS = defer(VanillaMaterials.LAPIS)
			.add(ITEM_PURE_NON_METAL);
	
	Material REDSTONE = defer(VanillaMaterials.REDSTONE)
			.add(TINY_DUST, CRUSHED_DUST);
	
	Material QUARTZ = defer(VanillaMaterials.QUARTZ)
			.add(ITEM_PURE_NON_METAL);
	
	Material BRICK = create("brick", "Brick")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.add("minecraft", INGOT.withoutSuffix())
			.add(DUST, TINY_DUST);
	
	Material FIRE_CLAY = create("fire_clay", "Fire Clay")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.add(INGOT.as((m, p) -> "%s_brick".formatted(m), (m, p) -> "%s Brick".formatted(m)))
			.add(DUST, TINY_DUST);
	
	Material COKE = create("coke", "Coke")
			.add(GEM, DUST, BLOCK);
	
	Material BRONZE = create("bronze", "Bronze")
			.add(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.add(BLOCK)
			.add(DRILL_HEAD, DRILL);
	
	Material TIN = create("tin", "Tin")
			.add(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.add(WIRE)
			.add(RAW_METAL, RAW_METAL_BLOCK)
			.add(BLOCK);
	
	Material STEEL = create("steel", "Steel")
			.add(BOLT, RING, ROD, GEAR, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.add(ROD_MAGNETIC)
			.add(BLOCK)
			.add(DRILL_HEAD, DRILL);
	
	Material LIGNITE_COAL = create("lignite_coal", "Lignite Coal")
			.add(GEM, BLOCK)
			.add(ITEM_PURE_NON_METAL);
	
	Material ALUMINUM = create("aluminum", "Aluminum")
			.add(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.add(WIRE)
			.add(BLOCK)
			.add(DRILL_HEAD, DRILL);
	
	Material BAUXITE = create("bauxite", "Bauxite")
			.add(ITEM_PURE_NON_METAL)
			.add(BLOCK);
	
	Material LEAD = create("lead", "Lead")
			.add(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.add(BLOCK)
			.add(RAW_METAL, RAW_METAL_BLOCK);
	
	Material BATTERY_ALLOY = create("battery_alloy", "Battery Alloy")
			.add(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, CURVED_PLATE, NUGGET)
			.add(BLOCK);
	
	Material INVAR = create("invar", "Invar")
			.add(TINY_DUST, DUST, INGOT, ROD, DOUBLE_INGOT, RING, BOLT, PLATE, NUGGET, GEAR)
			.add(LARGE_PLATE)
			.add(BLOCK);
	
	Material CUPRONICKEL = create("cupronickel", "Cupronickel")
			.add(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, WIRE, NUGGET, WIRE_MAGNETIC)
			.add(COIL)
			.add(BLOCK);
	
	Material ANTIMONY = create("antimony", "Antimony")
			.add(ITEM_PURE_METAL)
			.add(RAW_METAL, RAW_METAL_BLOCK)
			.add(BLOCK);
	
	Material NICKEL = create("nickel", "Nickel")
			.add(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.add(RAW_METAL, RAW_METAL_BLOCK)
			.add(BLOCK);
	
	Material SILVER = create("silver", "Silver")
			.add(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.add(WIRE)
			.add(RAW_METAL, RAW_METAL_BLOCK)
			.add(BLOCK);
	
	Material SODIUM = create("sodium", "Sodium")
			.add(DUST, TINY_DUST)
			.add(BLOCK);
	
	Material SALT = create("salt", "Salt")
			.add(ITEM_PURE_NON_METAL)
			.add(BLOCK);
	
	Material TITANIUM = create("titanium", "Titanium")
			.add(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.add(BLOCK)
			.add(RAW_METAL, RAW_METAL_BLOCK)
			.add(HOT_INGOT)
			.add(DRILL_HEAD, DRILL);
	
	Material ELECTRUM = create("electrum", "Electrum")
			.add(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.add(BLOCK)
			.add(WIRE, FINE_WIRE);
	
	Material SILICON = create("silicon", "Silicon")
			.add(ITEM_PURE_METAL)
			.add(BLOCK)
			.add(N_DOPED_PLATE, P_DOPED_PLATE)
			.add(PLATE, DOUBLE_INGOT);
	
	Material STAINLESS_STEEL = create("stainless_steel", "Stainless Steel")
			.add(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.add(BLOCK)
			.add(HOT_INGOT)
			.add(DRILL_HEAD, DRILL)
			.add(ROD_MAGNETIC);
	
	Material RUBY = create("ruby", "Ruby")
			.add(DUST, TINY_DUST);
	
	Material CARBON = create("carbon", "Carbon")
			.add(DUST, TINY_DUST)
			.add(PLATE, LARGE_PLATE);
	
	Material CHROMIUM = create("chromium", "Chromium")
			.add(ITEM_PURE_METAL)
			.add(HOT_INGOT)
			.add(CRUSHED_DUST)
			.add(BLOCK)
			.add(PLATE, DOUBLE_INGOT);
	
	Material MANGANESE = create("manganese", "Manganese")
			.add(ITEM_PURE_NON_METAL);
	
	Material BERYLLIUM = create("beryllium", "Beryllium")
			.add(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.add(BLOCK);
	
	Material ANNEALED_COPPER = create("annealed_copper", "Annealed Copper")
			.add(ITEM_PURE_METAL)
			.add(PLATE, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.add(BLOCK);
	
	Material URANIUM_235 = create("uranium_235", "Uranium 235")
			.add(ITEM_PURE_METAL)
			.add(BLOCK);
	
	Material URANIUM_238 = create("uranium_238", "Uranium 238")
			.add(ITEM_PURE_METAL)
			.add(BLOCK);
	
	Material URANIUM = create("uranium", "Uranium")
			.add(ITEM_PURE_METAL)
			.add(BLOCK)
			.add(ROD)
			.add(RAW_METAL, RAW_METAL_BLOCK);
	
	Material LE_URANIUM = create("le_uranium", "LE Uranium")
			.add(ITEM_PURE_METAL)
			.add(BLOCK)
			.add(ROD);
	
	Material HE_URANIUM = create("he_uranium", "HE Uranium")
			.add(ITEM_PURE_METAL)
			.add(BLOCK)
			.add(ROD);
	
	Material PLUTONIUM = create("plutonium", "Plutonium")
			.add(ITEM_PURE_METAL)
			.add(BLOCK);
	
	Material LE_MOX = create("le_mox", "LE Mox")
			.add(ITEM_PURE_METAL)
			.add(BLOCK)
			.add(ROD);
	
	Material HE_MOX = create("he_mox", "HE Mox")
			.add(ITEM_PURE_METAL)
			.add(BLOCK)
			.add(ROD);
	
	Material PLATINUM = create("platinum", "Platinum")
			.add(ITEM_PURE_METAL)
			.add(PLATE, DOUBLE_INGOT, WIRE, FINE_WIRE, HOT_INGOT)
			.add(BLOCK)
			.add(RAW_METAL, RAW_METAL_BLOCK);
	
	Material KANTHAL = create("kanthal", "Kanthal")
			.add(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.add(COIL)
			.add(BLOCK);
	
	Material IRIDIUM = create("iridium", "Iridium")
			.add(ITEM_PURE_METAL)
			.add(CURVED_PLATE)
			.add(BLOCK)
			.add(RAW_METAL, RAW_METAL_BLOCK);
	
	Material MONAZITE = create("monazite", "Monazite")
			.add(ITEM_PURE_NON_METAL)
			.add(BLOCK);
	
	Material CADMIUM = create("cadmium", "Cadmium")
			.add(DUST, TINY_DUST, INGOT, PLATE, ROD, DOUBLE_INGOT);
	
	Material NEODYMIUM = create("neodymium", "Neodymium")
			.add(DUST, TINY_DUST)
			.add(BLOCK);
	
	Material YTTRIUM = create("yttrium", "Yttrium")
			.add(DUST, TINY_DUST)
			.add(BLOCK);
	
	Material SUPERCONDUCTOR = create("superconductor", "Superconductor")
			.add(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.add(COIL);
	
	Material TUNGSTEN = create("tungsten", "Tungsten")
			.add(ITEM_PURE_METAL)
			.add(PLATE, LARGE_PLATE, DOUBLE_INGOT)
			.add(BLOCK)
			.add(RAW_METAL, RAW_METAL_BLOCK);
	
	Material BLASTPROOF_ALLOY = create("blastproof_alloy", "Blastproof Alloy")
			.add(INGOT, PLATE, LARGE_PLATE, CURVED_PLATE);
	
	Material NUCLEAR_ALLOY = create("nuclear_alloy", "Nuclear Alloy")
			.add(PLATE, LARGE_PLATE);
	
	Material SOLDERING_ALLOY = create("soldering_alloy", "Soldering Alloy")
			.add(DUST, TINY_DUST)
			.add(BLOCK);
	
	Material SULFUR = create("sulfur", "Sulfur")
			.add(DUST, TINY_DUST)
			.add(BLOCK);
}
