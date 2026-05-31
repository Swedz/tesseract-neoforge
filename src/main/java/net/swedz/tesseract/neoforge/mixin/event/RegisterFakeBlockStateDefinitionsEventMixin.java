package net.swedz.tesseract.neoforge.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.model.BlockStateDefinitions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.fml.ModLoader;
import net.swedz.tesseract.neoforge.event.RegisterFakeBlockStateDefinitionsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Function;

@Mixin(BlockStateDefinitions.class)
public class RegisterFakeBlockStateDefinitionsEventMixin
{
	@Inject(
			method = "definitionLocationToBlockStateMapper",
			at = @At("TAIL")
	)
	private static void definitionLocationToBlockStateMapper(
			CallbackInfoReturnable<Function<Identifier, StateDefinition<Block, BlockState>>> callback,
			@Local Map<Identifier, StateDefinition<Block, BlockState>> result
	)
	{
		ModLoader.postEvent(new RegisterFakeBlockStateDefinitionsEvent(result));
	}
}
