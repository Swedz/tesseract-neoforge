package net.swedz.tesseract.neoforge.compat.vanilla.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.swedz.tesseract.neoforge.material.Material;

import java.util.Optional;

import static net.swedz.tesseract.neoforge.compat.vanilla.material.part.VanillaMaterialParts.*;
import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public interface VanillaMaterials
{
	Material GOLD = create("gold", "Gold")
			.set(HARDNESS, 3f)
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.addNative(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material IRON = create("iron", "Iron")
			.addNative(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material COPPER = create("copper", "Copper")
			.set(HARDNESS, 3f)
			.addNative(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK)
			.immutable();
	
	Material COAL = create("coal", "Coal")
			.set(NEEDS_TOOL, Optional.empty())
			.set(MAIN_PART, GEM)
			.addNative(GEM, BLOCK)
			.immutable();
	
	Material DIAMOND = create("diamond", "Diamond")
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.set(MAIN_PART, GEM)
			.addNative(GEM, BLOCK)
			.immutable();
	
	Material EMERALD = create("emerald", "Emerald")
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.set(MAIN_PART, GEM)
			.addNative(GEM, BLOCK)
			.immutable();
	
	Material LAPIS = create("lapis", "Lapis")
			.set(BLAST_RESISTANCE, 3f)
			.set(HARDNESS, 3f)
			.set(MAIN_PART, GEM)
			.addNative(GEM, BLOCK)
			.immutable();
	
	Material REDSTONE = create("redstone", "Redstone")
			.set(NEEDS_TOOL, Optional.empty())
			.set(MAIN_PART, DUST)
			.addNative(DUST.formattingMaterialOnly(), BLOCK)
			.immutable();
	
	Material QUARTZ = create("quartz", "Quartz")
			.set(BLAST_RESISTANCE, 0.8f)
			.set(HARDNESS, 0.8f)
			.set(NEEDS_TOOL, Optional.empty())
			.set(MAIN_PART, GEM)
			.addNative(GEM, BLOCK)
			.immutable();
	
	Material NETHERITE = create("netherite", "Netherite")
			.set(BLAST_RESISTANCE, 1200f)
			.set(HARDNESS, 50f)
			.setOptional(NEEDS_TOOL, BlockTags.NEEDS_DIAMOND_TOOL)
			.addNative(INGOT, SCRAP, BLOCK)
			.immutable();
	
	static Material create(String id, String englishName)
	{
		return new Material(ResourceLocation.withDefaultNamespace(id), englishName);
	}
}
