package net.swedz.tesseract.neoforge.material.builtin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.builtin.property.OrePartDrops;

import java.util.Optional;

import static net.swedz.tesseract.neoforge.material.builtin.part.MaterialParts.*;
import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;
import static net.swedz.tesseract.neoforge.material.builtin.recipe.VanillaMaterialRecipeGroups.*;

public interface Materials
{
	Material GOLD = create("gold", "Gold")
			.set(HARDNESS, 3f)
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.set(ORE_DROP_PART, OrePartDrops.of(RAW_METAL))
			.addNative(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.addNative(ORE_NETHERRACK.set(ORE_DROP_PART, OrePartDrops.of(NUGGET, UniformInt.of(2, 6), UniformInt.of(0, 1))))
			.recipes(STANDARD, SMELTING);
	
	Material IRON = create("iron", "Iron")
			.set(ORE_DROP_PART, OrePartDrops.of(RAW_METAL))
			.addNative(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, SMELTING);
	
	Material COPPER = create("copper", "Copper")
			.set(HARDNESS, 3f)
			.set(ORE_DROP_PART, OrePartDrops.drops(RAW_METAL, UniformInt.of(2, 5)))
			.addNative(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, SMELTING);
	
	Material COAL = create("coal", "Coal")
			.set(NEEDS_TOOL, Optional.empty())
			.set(MAIN_PART, GEM)
			.set(ORE_DROP_PART, OrePartDrops.experience(GEM, UniformInt.of(0, 2)))
			.addNative(GEM, BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, SMELTING);
	
	Material DIAMOND = create("diamond", "Diamond")
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.set(MAIN_PART, GEM)
			.set(ORE_DROP_PART, OrePartDrops.experience(GEM, UniformInt.of(3, 7)))
			.addNative(GEM, BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, SMELTING);
	
	Material EMERALD = create("emerald", "Emerald")
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.set(MAIN_PART, GEM)
			.set(ORE_DROP_PART, OrePartDrops.experience(GEM, UniformInt.of(3, 7)))
			.addNative(GEM, BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, SMELTING);
	
	Material LAPIS = create("lapis", "Lapis")
			.set(BLAST_RESISTANCE, 3f)
			.set(HARDNESS, 3f)
			.set(MAIN_PART, GEM)
			.set(ORE_DROP_PART, OrePartDrops.of(GEM, UniformInt.of(4, 9), UniformInt.of(2, 5)))
			.addNative(GEM, BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, SMELTING);
	
	Material REDSTONE = create("redstone", "Redstone")
			.set(NEEDS_TOOL, Optional.empty())
			.set(MAIN_PART, DUST)
			.set(ORE_DROP_PART, OrePartDrops.of(DUST, UniformInt.of(4, 5), UniformInt.of(1, 5)))
			.addNative(DUST.formattingMaterialOnly(), BLOCK)
			.addNative(ORE, ORE_DEEPSLATE)
			.recipes(STANDARD, SMELTING);
	
	Material QUARTZ = create("quartz", "Quartz")
			.set(BLAST_RESISTANCE, 0.8f)
			.set(HARDNESS, 0.8f)
			.set(NEEDS_TOOL, Optional.empty())
			.set(MAIN_PART, GEM)
			.set(ORE_DROP_PART, OrePartDrops.experience(GEM, UniformInt.of(2, 5)))
			.addNative(GEM, BLOCK)
			.addNative(ORE_NETHERRACK)
			.recipes(STANDARD, SMELTING);
	
	Material NETHERITE = create("netherite", "Netherite")
			.set(BLAST_RESISTANCE, 1200f)
			.set(HARDNESS, 50f)
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_DIAMOND_TOOL)
			.addNative(INGOT, SCRAP, BLOCK)
			.recipes(STANDARD);
	
	static Material create(String id, String englishName)
	{
		return new Material(ResourceLocation.withDefaultNamespace(id), englishName);
	}
}
