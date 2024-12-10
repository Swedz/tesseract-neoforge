package net.swedz.tesseract.neoforge.mixin.event;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.tesseract.neoforge.event.TreeGrowthEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(TreeFeature.class)
public class TreeGrowthEventMixin
{
	@Inject(
			method = "place",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/structure/BoundingBox;encapsulatingPositions(Ljava/lang/Iterable;)Ljava/util/Optional;",
					shift = At.Shift.BEFORE
			)
	)
	private void onTreeGrowth(FeaturePlaceContext<TreeConfiguration> context,
							  CallbackInfoReturnable<Boolean> callback,
							  @Local(ordinal = 0) Set<BlockPos> trunkPositions,
							  @Local(ordinal = 1) Set<BlockPos> branchPositions,
							  @Local(ordinal = 2) Set<BlockPos> foliagePositions,
							  @Local(ordinal = 3) Set<BlockPos> decoratorPositions)
	{
		LevelAccessor level = context.level();
		BlockPos origin = context.origin();
		
		List<BlockPos> positions = Lists.newArrayList();
		positions.addAll(trunkPositions);
		positions.addAll(branchPositions);
		positions.addAll(foliagePositions);
		positions.addAll(decoratorPositions);
		TreeGrowthEvent event = new TreeGrowthEvent(level, origin, level.getBlockState(origin), positions);
		NeoForge.EVENT_BUS.post(event);
	}
}
