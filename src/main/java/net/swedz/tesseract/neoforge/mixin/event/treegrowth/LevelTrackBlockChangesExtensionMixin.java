package net.swedz.tesseract.neoforge.mixin.event.treegrowth;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.swedz.tesseract.neoforge.event.treegrowth.TrackBlockChangesExtension;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Level.class)
@Implements(@Interface(iface = TrackBlockChangesExtension.class, prefix = "ext$"))
public abstract class LevelTrackBlockChangesExtensionMixin
{
	@Unique
	private Set<BlockPos> trackBlockPositions = null;
	
	public void ext$startTrackingBlockChanges(Set<BlockPos> positions)
	{
		trackBlockPositions = positions;
	}
	
	public void ext$stopTrackingBlockChanges()
	{
		trackBlockPositions = null;
	}
	
	@Inject(
			method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
			at = @At("RETURN")
	)
	private void setBlock(
			BlockPos pos,
			BlockState state,
			int flags,
			int recursionLeft,
			CallbackInfoReturnable<Boolean> callback
	)
	{
		if(callback.getReturnValue() &&
		   trackBlockPositions != null)
		{
			trackBlockPositions.add(pos);
		}
	}
}
