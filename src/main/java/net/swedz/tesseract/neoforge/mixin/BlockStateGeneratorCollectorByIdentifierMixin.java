package net.swedz.tesseract.neoforge.mixin;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.swedz.tesseract.neoforge.helper.datagen.BlockStateGeneratorCollectorByIdentifier;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(targets = "net.minecraft.client.data.models.ModelProvider$BlockStateGeneratorCollector")
@Implements(@Interface(iface = BlockStateGeneratorCollectorByIdentifier.class, prefix = "tesseractapi$"))
public abstract class BlockStateGeneratorCollectorByIdentifierMixin
{
	@Unique
	private final Map<Identifier, BlockStateModelDispatcher> generatorsById = Maps.newHashMap();
	
	public void tesseractapi$register(Identifier id, BlockStateModelDispatcher generator)
	{
		generatorsById.put(id, generator);
	}
	
	@WrapOperation(
			method = "save",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/data/DataProvider;saveAll(Lnet/minecraft/data/CachedOutput;Lcom/mojang/serialization/Codec;Ljava/util/function/Function;Ljava/util/Map;)Ljava/util/concurrent/CompletableFuture;"
			)
	)
	public CompletableFuture<?> save(
			CachedOutput cache,
			Codec<?> codec,
			Function<?, Path> pathGetter,
			Map<?, ?> contents,
			Operation<CompletableFuture<?>> original,
			@Local(argsOnly = true) PackOutput.PathProvider pathProvider
	)
	{
		return CompletableFuture.allOf(
				original.call(cache, codec, pathGetter, contents),
				DataProvider.saveAll(
						cache,
						BlockStateModelDispatcher.CODEC,
						pathProvider::json,
						generatorsById
				)
		);
	}
}
