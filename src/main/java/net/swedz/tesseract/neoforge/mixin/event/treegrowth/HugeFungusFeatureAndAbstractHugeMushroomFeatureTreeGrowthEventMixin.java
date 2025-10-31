package net.swedz.tesseract.neoforge.mixin.event.treegrowth;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.tesseract.neoforge.event.treegrowth.TrackingWorldGenLevel;
import net.swedz.tesseract.neoforge.event.treegrowth.TreeGrowthEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({
		HugeFungusFeature.class,
		AbstractHugeMushroomFeature.class
})
public class HugeFungusFeatureAndAbstractHugeMushroomFeatureTreeGrowthEventMixin
{
	@WrapOperation(
			method = "place",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;level()Lnet/minecraft/world/level/WorldGenLevel;"
			)
	)
	private WorldGenLevel wrapLevel(FeaturePlaceContext instance,
									Operation<WorldGenLevel> original)
	{
		var level = original.call(instance);
		if(instance.level() instanceof WorldGenRegion)
		{
			return level;
		}
		// This is an extra safety precaution in case someone is re-using context instances
		instance.clearTrackedBlockPositions();
		return new TrackingWorldGenLevel(level, instance::trackBlockPosition);
	}
	
	@Inject(
			method = "place",
			at = @At("RETURN")
	)
	private void place(FeaturePlaceContext context,
					   CallbackInfoReturnable<Boolean> callback)
	{
		var level = context.level();
		if(level instanceof WorldGenRegion)
		{
			return;
		}
		var origin = context.origin();
		
		var positions = context.getTrackedBlockPositions();
		if(positions.isEmpty())
		{
			return;
		}
		
		var event = new TreeGrowthEvent(level, origin, level.getBlockState(origin), positions);
		NeoForge.EVENT_BUS.post(event);
	}
}
