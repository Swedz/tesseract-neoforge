package net.swedz.tesseract.neoforge.compat.mi.material;

import aztech.modern_industrialization.MI;
import net.swedz.tesseract.neoforge.compat.vanilla.material.VanillaMaterials;
import net.swedz.tesseract.neoforge.material.Material;

import java.util.Optional;

import static net.swedz.tesseract.neoforge.compat.mi.material.part.MIMaterialParts.*;
import static net.swedz.tesseract.neoforge.compat.vanilla.material.part.VanillaMaterialParts.*;
import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public interface MIMaterials
{
	Material GOLD = VanillaMaterials.GOLD.copy(MI.ID)
			.addNative(BOLT, RING, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, PLATE, TINY_DUST)
			.addNative(DRILL_HEAD, DRILL)
			.immutable();
	
	Material IRON = VanillaMaterials.IRON.copy(MI.ID)
			.addNative(BOLT, RING, GEAR, ROD, DOUBLE_INGOT, DUST, LARGE_PLATE, PLATE, TINY_DUST)
			.immutable();
	
	Material COPPER = VanillaMaterials.COPPER.copy(MI.ID)
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE, FINE_WIRE)
			.addNative(DRILL_HEAD, DRILL)
			.immutable();
	
	Material COAL = VanillaMaterials.COAL.copy(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.immutable();
	
	Material DIAMOND = VanillaMaterials.DIAMOND.copy(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(PLATE, LARGE_PLATE)
			.immutable();
	
	Material EMERALD = VanillaMaterials.EMERALD.copy(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(PLATE)
			.immutable();
	
	Material LAPIS = VanillaMaterials.LAPIS.copy(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.immutable();
	
	Material REDSTONE = VanillaMaterials.REDSTONE.copy(MI.ID)
			.addNative(TINY_DUST, CRUSHED_DUST)
			.immutable();
	
	Material QUARTZ = VanillaMaterials.QUARTZ.copy(MI.ID)
			.addNative(ITEM_PURE_NON_METAL)
			.immutable();
	
	Material BRICK = create("brick", "Brick")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.addNative("minecraft", INGOT.formattingMaterialOnly())
			.addNative(DUST, TINY_DUST)
			.immutable();
	
	Material FIRE_CLAY = create("fire_clay", "Fire Clay")
			.set(HARDNESS, 2f)
			.set(NEEDS_TOOL, Optional.empty())
			.addNative(INGOT.formattingMaterialOnly("%s_brick"::formatted, "%s Brick"::formatted))
			.addNative(DUST, TINY_DUST)
			.immutable();
	
	Material COKE = create("coke", "Coke")
			.addNative(GEM, DUST, BLOCK)
			.immutable();
	
	Material BRONZE = create("bronze", "Bronze")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL)
			.immutable();
	
	Material TIN = create("tin", "Tin")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.immutable();
	
	Material STEEL = create("steel", "Steel")
			.addNative(BOLT, RING, ROD, GEAR, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(ROD_MAGNETIC)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL)
			.immutable();
	
	Material LIGNITE_COAL = create("lignite_coal", "Lignite Coal")
			.addNative(GEM, BLOCK)
			.addNative(ITEM_PURE_NON_METAL)
			.immutable();
	
	Material ALUMINUM = create("aluminum", "Aluminum")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(BLOCK)
			.addNative(DRILL_HEAD, DRILL)
			.immutable();
	
	Material BAUXITE = create("bauxite", "Bauxite")
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.immutable();
	
	Material LEAD = create("lead", "Lead")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material BATTERY_ALLOY = create("battery_alloy", "Battery Alloy")
			.addNative(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, CURVED_PLATE, NUGGET)
			.addNative(BLOCK)
			.immutable();
	
	Material INVAR = create("invar", "Invar")
			.addNative(TINY_DUST, DUST, INGOT, ROD, DOUBLE_INGOT, RING, BOLT, PLATE, NUGGET, GEAR)
			.addNative(LARGE_PLATE)
			.addNative(BLOCK)
			.immutable();
	
	Material CUPRONICKEL = create("cupronickel", "Cupronickel")
			.addNative(TINY_DUST, DUST, INGOT, DOUBLE_INGOT, PLATE, WIRE, NUGGET, WIRE_MAGNETIC)
			.addNative(COIL)
			.addNative(BLOCK)
			.immutable();
	
	Material ANTIMONY = create("antimony", "Antimony")
			.addNative(ITEM_PURE_METAL)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.immutable();
	
	Material NICKEL = create("nickel", "Nickel")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.immutable();
	
	Material SILVER = create("silver", "Silver")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(WIRE)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(BLOCK)
			.immutable();
	
	Material SODIUM = create("sodium", "Sodium")
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.immutable();
	
	Material SALT = create("salt", "Salt")
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.immutable();
	
	Material TITANIUM = create("titanium", "Titanium")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.addNative(HOT_INGOT)
			.addNative(DRILL_HEAD, DRILL)
			.immutable();
	
	Material ELECTRUM = create("electrum", "Electrum")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(WIRE, FINE_WIRE)
			.immutable();
	
	Material SILICON = create("silicon", "Silicon")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(N_DOPED_PLATE, P_DOPED_PLATE)
			.addNative(PLATE, DOUBLE_INGOT)
			.immutable();
	
	Material STAINLESS_STEEL = create("stainless_steel", "Stainless Steel")
			.addNative(BOLT, BLADE, RING, ROTOR, GEAR, ROD, CURVED_PLATE, DOUBLE_INGOT, DUST, INGOT, LARGE_PLATE, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.addNative(HOT_INGOT)
			.addNative(DRILL_HEAD, DRILL)
			.addNative(ROD_MAGNETIC)
			.immutable();
	
	Material RUBY = create("ruby", "Ruby")
			.addNative(DUST, TINY_DUST)
			.immutable();
	
	Material CARBON = create("carbon", "Carbon")
			.addNative(DUST, TINY_DUST)
			.addNative(PLATE, LARGE_PLATE)
			.immutable();
	
	Material CHROMIUM = create("chromium", "Chromium")
			.addNative(ITEM_PURE_METAL)
			.addNative(HOT_INGOT)
			.addNative(CRUSHED_DUST)
			.addNative(BLOCK)
			.addNative(PLATE, DOUBLE_INGOT)
			.immutable();
	
	Material MANGANESE = create("manganese", "Manganese")
			.addNative(ITEM_PURE_NON_METAL)
			.immutable();
	
	Material BERYLLIUM = create("beryllium", "Beryllium")
			.addNative(DOUBLE_INGOT, DUST, INGOT, NUGGET, PLATE, TINY_DUST)
			.addNative(BLOCK)
			.immutable();
	
	Material ANNEALED_COPPER = create("annealed_copper", "Annealed Copper")
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(BLOCK)
			.immutable();
	
	Material URANIUM_235 = create("uranium_235", "Uranium 235")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.immutable();
	
	Material URANIUM_238 = create("uranium_238", "Uranium 238")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.immutable();
	
	Material URANIUM = create("uranium", "Uranium")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material LE_URANIUM = create("le_uranium", "LE Uranium")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.immutable();
	
	Material HE_URANIUM = create("he_uranium", "HE Uranium")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.immutable();
	
	Material PLUTONIUM = create("plutonium", "Plutonium")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.immutable();
	
	Material LE_MOX = create("le_mox", "LE Mox")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.immutable();
	
	Material HE_MOX = create("he_mox", "HE Mox")
			.addNative(ITEM_PURE_METAL)
			.addNative(BLOCK)
			.addNative(ROD)
			.immutable();
	
	Material PLATINUM = create("platinum", "Platinum")
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, DOUBLE_INGOT, WIRE, FINE_WIRE, HOT_INGOT)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material KANTHAL = create("kanthal", "Kanthal")
			.addNative(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(COIL)
			.addNative(BLOCK)
			.immutable();
	
	Material IRIDIUM = create("iridium", "Iridium")
			.addNative(ITEM_PURE_METAL)
			.addNative(CURVED_PLATE)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material MONAZITE = create("monazite", "Monazite")
			.addNative(ITEM_PURE_NON_METAL)
			.addNative(BLOCK)
			.immutable();
	
	Material CADMIUM = create("cadmium", "Cadmium")
			.addNative(DUST, TINY_DUST, INGOT, PLATE, ROD, DOUBLE_INGOT)
			.immutable();
	
	Material NEODYMIUM = create("neodymium", "Neodymium")
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.immutable();
	
	Material YTTRIUM = create("yttrium", "Yttrium")
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.immutable();
	
	Material SUPERCONDUCTOR = create("superconductor", "Superconductor")
			.addNative(TINY_DUST, DUST, PLATE, INGOT, NUGGET, WIRE, DOUBLE_INGOT, HOT_INGOT)
			.addNative(COIL)
			.immutable();
	
	Material TUNGSTEN = create("tungsten", "Tungsten")
			.addNative(ITEM_PURE_METAL)
			.addNative(PLATE, LARGE_PLATE, DOUBLE_INGOT)
			.addNative(BLOCK)
			.addNative(RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material BLASTPROOF_ALLOY = create("blastproof_alloy", "Blastproof Alloy")
			.addNative(INGOT, PLATE, LARGE_PLATE, CURVED_PLATE)
			.immutable();
	
	Material NUCLEAR_ALLOY = create("nuclear_alloy", "Nuclear Alloy")
			.addNative(PLATE, LARGE_PLATE)
			.immutable();
	
	Material SOLDERING_ALLOY = create("soldering_alloy", "Soldering Alloy")
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.immutable();
	
	Material SULFUR = create("sulfur", "Sulfur")
			.addNative(DUST, TINY_DUST)
			.addNative(BLOCK)
			.immutable();
	
	static Material create(String id, String englishName)
	{
		return new Material(MI.id(id), englishName);
	}
}
