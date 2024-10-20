package net.swedz.tesseract.neoforge.material;

import net.minecraft.tags.BlockTags;

import java.util.Optional;

import static net.swedz.tesseract.neoforge.material.VanillaMaterial.*;
import static net.swedz.tesseract.neoforge.material.part.VanillaMaterialParts.*;
import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public interface VanillaMaterials
{
	Material GOLD = create("gold", "Gold")
			.set(HARDNESS, 3f)
			.set(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.add(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK);
	
	Material IRON = create("iron", "Iron")
			.add(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK);
	
	Material COPPER = create("copper", "Copper")
			.set(HARDNESS, 3f)
			.add(INGOT, NUGGET, BLOCK, RAW_METAL, RAW_METAL_BLOCK);
	
	Material COAL = create("coal", "Coal")
			.set(NEEDS_TOOL, Optional.empty())
			.add(GEM, BLOCK);
	
	Material DIAMOND = create("diamond", "Diamond")
			.set(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.add(GEM, BLOCK);
	
	Material EMERALD = create("emerald", "Emerald")
			.set(NEEDS_TOOL, BlockTags.NEEDS_IRON_TOOL)
			.add(GEM, BLOCK);
	
	Material LAPIS = create("lapis", "Lapis")
			.set(BLAST_RESISTANCE, 3f)
			.set(HARDNESS, 3f)
			.add(GEM, BLOCK);
	
	Material REDSTONE = create("redstone", "Redstone")
			.set(NEEDS_TOOL, Optional.empty())
			.add(DUST.withoutSuffix(), BLOCK);
	
	Material QUARTZ = create("quartz", "Quartz")
			.set(BLAST_RESISTANCE, 0.8f)
			.set(HARDNESS, 0.8f)
			.set(NEEDS_TOOL, Optional.empty())
			.add(GEM, BLOCK);
	
	Material NETHERITE = create("netherite", "Netherite")
			.set(BLAST_RESISTANCE, 1200f)
			.set(HARDNESS, 50f)
			.set(NEEDS_TOOL, BlockTags.NEEDS_DIAMOND_TOOL)
			.add(INGOT, SCRAP, BLOCK);
}
