package net.swedz.tesseract.neoforge.mixin.event.treegrowth;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.tesseract.neoforge.event.treegrowth.TreeGrowthEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Feature.class)
public class TreeGrowthEventMixin
{
	@Inject(
			method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/Feature;place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z"
			)
	)
	private void beforePlace(
			FeatureConfiguration config,
			WorldGenLevel level,
			ChunkGenerator chunkGenerator,
			RandomSource random,
			BlockPos origin,
			CallbackInfoReturnable<Boolean> callback,
			@Local(argsOnly = true) FeatureConfiguration configuration,
			@Share("actualLevel") LocalRef<Level> actualLevelReference,
			@Share("trackedBlockChanges") LocalRef<Set<BlockPos>> trackedBlockChangesReference
	)
	{
		if(level instanceof Level actualLevel)
		{
			var feature = (Feature) (Object) this;
			if(TreeGrowthEvent.shouldPostEventFor(feature, configuration))
			{
				actualLevelReference.set(actualLevel);
				
				Set<BlockPos> trackedBlockChanges = Sets.newConcurrentHashSet();
				trackedBlockChangesReference.set(trackedBlockChanges);
				actualLevel.startTrackingBlockChanges(trackedBlockChanges);
			}
		}
	}
	
	@WrapOperation(
			method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/Feature;place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z"
			)
	)
	private boolean afterPlace(
			Feature instance,
			FeaturePlaceContext<FeatureConfiguration> context,
			Operation<Boolean> original,
			@Local(argsOnly = true) WorldGenLevel level,
			@Local(argsOnly = true) BlockPos origin,
			@Share("actualLevel") LocalRef<Level> actualLevelReference,
			@Share("trackedBlockChanges") LocalRef<Set<BlockPos>> trackedBlockChangesReference
	)
	{
		boolean result = original.call(instance, context);
		
		var actualLevel = actualLevelReference.get();
		if(actualLevel != null)
		{
			actualLevel.stopTrackingBlockChanges();
		}
		
		if(result)
		{
			var trackedBlockChanges = trackedBlockChangesReference.get();
			if(trackedBlockChanges != null)
			{
				var event = new TreeGrowthEvent(
						level,
						origin,
						level.getBlockState(origin),
						trackedBlockChanges
				);
				NeoForge.EVENT_BUS.post(event);
			}
		}
		
		return result;
	}
}
