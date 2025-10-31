package net.swedz.tesseract.neoforge.event.treegrowth;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface FeaturePlacedBlockTracker
{
	default void clearTrackedBlockPositions()
	{
		throw new UnsupportedOperationException();
	}
	
	default void trackBlockPosition(BlockPos pos)
	{
		throw new UnsupportedOperationException();
	}
	
	default List<BlockPos> getTrackedBlockPositions()
	{
		throw new UnsupportedOperationException();
	}
}
