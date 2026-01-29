package net.swedz.tesseract.neoforge.event.treegrowth;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.swedz.tesseract.neoforge.mixin.event.treegrowth.TreeGrowthFeatureTestCacheMixin;

/**
 * <p>Injected into {@link Feature}</p>
 *
 * @see TreeGrowthFeatureTestCacheMixin
 * @see TreeGrowthEvent#shouldPostEventFor(Feature, FeatureConfiguration)
 */
public interface TreeGrowthFeatureTestCache
{
	default boolean needsToCalculateShouldPostTreeGrowthEvent()
	{
		throw new UnsupportedOperationException();
	}
	
	default void setShouldPostTreeGrowthEvent(boolean value)
	{
		throw new UnsupportedOperationException();
	}
	
	default boolean shouldPostTreeGrowthEvent()
	{
		throw new UnsupportedOperationException();
	}
}
