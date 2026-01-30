package net.swedz.tesseract.neoforge.event.treegrowth;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class TreeGrowthEvent extends BlockEvent
{
	/**
	 * <p>Defines identifiers for features that should be considered trees for the sake of {@link TreeGrowthEvent}.</p>
	 */
	private static final Set<ResourceLocation> TREE_FEATURE_IDS = Set.of(
			// Vanilla tree features
			ResourceLocation.withDefaultNamespace("tree"),
			ResourceLocation.withDefaultNamespace("huge_fungus"),
			ResourceLocation.withDefaultNamespace("huge_brown_mushroom"),
			ResourceLocation.withDefaultNamespace("huge_red_mushroom"),
			// Oh The Trees You'll Grow (Oh The Biomes You'll Go)
			ResourceLocation.fromNamespaceAndPath("ohthetreesyoullgrow", "tree_from_nbt_v1"),
			ResourceLocation.fromNamespaceAndPath("ohthetreesyoullgrow", "tree_from_nbt_v2"),
			// Regions Unexplored has some tree features that don't use RuTreeConfiguration so we need to include them manually
			ResourceLocation.fromNamespaceAndPath("regions_unexplored", "small_yellow_bioshroom"),
			ResourceLocation.fromNamespaceAndPath("regions_unexplored", "cobalt_tree"),
			ResourceLocation.fromNamespaceAndPath("regions_unexplored", "large_joshua_tree"),
			ResourceLocation.fromNamespaceAndPath("regions_unexplored", "medium_joshua_tree"),
			ResourceLocation.fromNamespaceAndPath("regions_unexplored", "small_socotra_tree"),
			ResourceLocation.fromNamespaceAndPath("regions_unexplored", "brim_willow"),
			ResourceLocation.fromNamespaceAndPath("regions_unexplored", "tall_brim_willow")
	);
	
	/**
	 * <p>Defines features by namespace and configuration class name that should be considered trees for the sake of
	 * {@link TreeGrowthEvent}.</p>
	 *
	 * <p>This is to be used when a mod has a lot of features that use the same configuration and they all should be
	 * captured by this event. This is a product of my laziness and also not wanting to maintain a massive list of
	 * features.</p>
	 */
	private static final Set<SpecificFeatureConfiguration> FEATURE_CONFIGURATIONS = Set.of(
			new SpecificFeatureConfiguration("twilightforest", "TFTreeFeatureConfig"),
			new SpecificFeatureConfiguration("regions_unexplored", "GiantBioshroomConfiguration"),
			new SpecificFeatureConfiguration("regions_unexplored", "RuTreeConfiguration")
	);
	
	private record SpecificFeatureConfiguration(
			String namespace,
			String configurationClassName
	)
	{
	}
	
	private static boolean calculateShouldPostEventFor(Feature feature, FeatureConfiguration configuration)
	{
		var featureId = BuiltInRegistries.FEATURE.getKey(feature);
		
		if(featureId == null)
		{
			return false;
		}
		
		if(TREE_FEATURE_IDS.contains(featureId))
		{
			return true;
		}
		
		var featureNamespace = featureId.getNamespace();
		var configurationClassName = configuration.getClass().getSimpleName();
		
		for(var specific : FEATURE_CONFIGURATIONS)
		{
			if(featureNamespace.equals(specific.namespace()) &&
			   configurationClassName.equals(specific.configurationClassName()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean shouldPostEventFor(Feature feature, FeatureConfiguration configuration)
	{
		if(feature.needsToCalculateShouldPostTreeGrowthEvent())
		{
			boolean should = calculateShouldPostEventFor(feature, configuration);
			feature.setShouldPostTreeGrowthEvent(should);
			return should;
		}
		return feature.shouldPostTreeGrowthEvent();
	}
	
	private final List<BlockPos> positions;
	
	public TreeGrowthEvent(LevelAccessor level, BlockPos pos, BlockState state, Collection<BlockPos> positions)
	{
		super(level, pos.immutable(), state);
		
		this.positions = Lists.newArrayList();
		this.positions.addAll(positions);
	}
	
	public List<BlockPos> getPositions()
	{
		return new ArrayList<>(positions);
	}
}
