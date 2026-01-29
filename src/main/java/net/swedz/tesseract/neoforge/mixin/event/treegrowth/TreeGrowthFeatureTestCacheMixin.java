package net.swedz.tesseract.neoforge.mixin.event.treegrowth;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.swedz.tesseract.neoforge.event.treegrowth.TreeGrowthFeatureTestCache;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Feature.class)
@Implements(@Interface(iface = TreeGrowthFeatureTestCache.class, prefix = "treeGrowthFeatureTestCache$"))
public abstract class TreeGrowthFeatureTestCacheMixin
{
	@Unique
	private Boolean shouldPostTreeGrowthEvent = null;
	
	public boolean treeGrowthFeatureTestCache$needsToCalculateShouldPostTreeGrowthEvent()
	{
		return shouldPostTreeGrowthEvent == null;
	}
	
	public boolean treeGrowthFeatureTestCache$shouldPostTreeGrowthEvent()
	{
		return shouldPostTreeGrowthEvent;
	}
	
	public void treeGrowthFeatureTestCache$setShouldPostTreeGrowthEvent(boolean value)
	{
		shouldPostTreeGrowthEvent = value;
	}
}
