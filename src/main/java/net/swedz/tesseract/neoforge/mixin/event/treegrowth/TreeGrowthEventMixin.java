package net.swedz.tesseract.neoforge.mixin.event.treegrowth;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.tesseract.neoforge.event.treegrowth.TrackingWorldGenLevel;
import net.swedz.tesseract.neoforge.event.treegrowth.TreeGrowthEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Feature.class)
public class TreeGrowthEventMixin
{
	@ModifyArg(
			method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;<init>(Ljava/util/Optional;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)V"
			)
	)
	private WorldGenLevel place(WorldGenLevel level,
								@Share("trackingLevel") LocalRef<TrackingWorldGenLevel> trackingLevelReference)
	{
		if(!(level instanceof WorldGenRegion))
		{
			var trackingLevel = new TrackingWorldGenLevel(level);
			trackingLevelReference.set(trackingLevel);
			return trackingLevel;
		}
		return level;
	}
	
	@WrapOperation(
			method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/Feature;place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z"
			)
	)
	private boolean place(
			Feature instance,
			FeaturePlaceContext<FeatureConfiguration> context,
			Operation<Boolean> original,
			@Local(argsOnly = true) WorldGenLevel level,
			@Local(argsOnly = true) FeatureConfiguration configuration,
			@Local(argsOnly = true) BlockPos origin,
			@Share("trackingLevel") LocalRef<TrackingWorldGenLevel> trackingLevelReference
	)
	{
		boolean result = original.call(instance, context);
		if(result)
		{
			var trackingLevel = trackingLevelReference.get();
			if(trackingLevel != null)
			{
				var feature = (Feature) (Object) this;
				if(TreeGrowthEvent.shouldPostEventFor(feature, configuration))
				{
					var event = new TreeGrowthEvent(
							level,
							origin,
							level.getBlockState(origin),
							trackingLevel.getModifiedBlockPositions()
					);
					NeoForge.EVENT_BUS.post(event);
				}
			}
		}
		return result;
	}
}
