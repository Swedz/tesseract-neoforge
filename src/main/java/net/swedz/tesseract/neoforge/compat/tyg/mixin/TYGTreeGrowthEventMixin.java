package net.swedz.tesseract.neoforge.compat.tyg.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.tesseract.neoforge.event.TreeGrowthEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(
		targets = "dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.TreeFromStructureNBTFeature",
		remap = false
)
public class TYGTreeGrowthEventMixin
{
	@Inject(
			method = "place",
			at = @At(value = "TAIL")
	)
	private void onTreeGrowth(FeaturePlaceContext context,
							  CallbackInfoReturnable<Boolean> callback,
							  @Local(name = "leavePositions") Set<BlockPos> leavePositions,
							  @Local(name = "trunkPositions") Set<BlockPos> trunkPositions,
							  @Local(name = "decorationPositions") Set<BlockPos> decorationPositions)
	{
		LevelAccessor level = context.level();
		BlockPos origin = context.origin();
		
		List<BlockPos> positions = Lists.newArrayList();
		positions.addAll(trunkPositions);
		positions.addAll(leavePositions);
		positions.addAll(decorationPositions);
		TreeGrowthEvent event = new TreeGrowthEvent(level, origin, level.getBlockState(origin), positions);
		NeoForge.EVENT_BUS.post(event);
	}
}
