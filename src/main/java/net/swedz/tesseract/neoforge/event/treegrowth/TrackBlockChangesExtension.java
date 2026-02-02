package net.swedz.tesseract.neoforge.event.treegrowth;

import net.minecraft.core.BlockPos;

import java.util.Set;

public interface TrackBlockChangesExtension
{
	default void startTrackingBlockChanges(Set<BlockPos> positions)
	{
		throw new UnsupportedOperationException();
	}
	
	default void stopTrackingBlockChanges()
	{
		throw new UnsupportedOperationException();
	}
}
