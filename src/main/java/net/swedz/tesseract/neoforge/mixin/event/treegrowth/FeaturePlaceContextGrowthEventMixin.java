package net.swedz.tesseract.neoforge.mixin.event.treegrowth;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.swedz.tesseract.neoforge.event.treegrowth.FeaturePlacedBlockTracker;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(FeaturePlaceContext.class)
@Implements(@Interface(iface = FeaturePlacedBlockTracker.class, prefix = "featurePlacedBlockTracker$"))
public class FeaturePlaceContextGrowthEventMixin
{
	@Unique
	private final List<BlockPos> trackedPlacedBlockPositions = Lists.newArrayList();
	
	public void featurePlacedBlockTracker$clearTrackedBlockPositions()
	{
		trackedPlacedBlockPositions.clear();
	}
	
	public void featurePlacedBlockTracker$trackBlockPosition(BlockPos pos)
	{
		trackedPlacedBlockPositions.add(pos);
	}
	
	public List<BlockPos> featurePlacedBlockTracker$getTrackedBlockPositions()
	{
		return trackedPlacedBlockPositions;
	}
}
