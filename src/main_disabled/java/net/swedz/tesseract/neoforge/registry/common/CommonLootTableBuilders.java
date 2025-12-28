package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.world.level.storage.loot.LootTable;
import net.swedz.tesseract.neoforge.registry.AccessibleBlockLootSubProvider;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;

import java.util.function.Function;

public final class CommonLootTableBuilders
{
	public static Function<AccessibleBlockLootSubProvider, LootTable.Builder> self(BlockHolder block)
	{
		return (provider) -> provider.createSingleItemTable(block.get().asItem());
	}
}
