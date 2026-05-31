package net.swedz.tesseract.neoforge.mixin;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.swedz.tesseract.neoforge.helper.datagen.SimpleModelCollectorByIdentifier;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(targets = "net.minecraft.client.data.models.ModelProvider$SimpleModelCollector")
@Implements(@Interface(iface = SimpleModelCollectorByIdentifier.class, prefix = "tesseractapi$"))
public abstract class SimpleModelCollectorByIdentifierMixin
{
	@Unique
	private final Map<Identifier, ItemModel.Unbaked> modelsById = Maps.newHashMap();
	
	public void tesseractapi$register(Identifier id, ItemModel.Unbaked model)
	{
		modelsById.put(id, model);
	}
	
	@WrapOperation(
			method = "save",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/data/DataProvider;saveAll(Lnet/minecraft/data/CachedOutput;Ljava/util/function/Function;Ljava/util/function/Function;Ljava/util/Map;)Ljava/util/concurrent/CompletableFuture;"
			)
	)
	public CompletableFuture<?> save(
			CachedOutput cache,
			Function<?, JsonElement> serializer,
			Function<?, Path> pathGetter,
			Map<?, ?> contents,
			Operation<CompletableFuture<?>> original,
			@Local(argsOnly = true) PackOutput.PathProvider pathProvider
	)
	{
		return CompletableFuture.allOf(
				original.call(cache, serializer, pathGetter, contents),
				DataProvider.saveAll(
						cache,
						ItemModels.CODEC,
						pathProvider::json,
						modelsById
				)
		);
	}
}
