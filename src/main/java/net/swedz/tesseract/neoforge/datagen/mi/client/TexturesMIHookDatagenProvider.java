package net.swedz.tesseract.neoforge.datagen.mi.client;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.resource.FastPathPackResources;
import aztech.modern_industrialization.textures.TextureHelper;
import aztech.modern_industrialization.textures.TextureManager;
import aztech.modern_industrialization.textures.coloramp.Coloramp;
import aztech.modern_industrialization.textures.coloramp.IColoramp;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResourcesBuilder;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.registry.MIFluidProperties;
import net.swedz.tesseract.neoforge.registry.holder.FluidHolder;
import net.swedz.tesseract.neoforge.registry.holder.MIFluidHolder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.minecraft.client.resources.ClientPackSource.*;

/**
 * A lot of this code was taken directly from {@link aztech.modern_industrialization.datagen.texture.TexturesProvider} and {@link aztech.modern_industrialization.textures.MITextures}.
 * I do not like that I had to do this, but that really be how it do be sometimes.
 * <br><br>
 * The main part that is different from this is the actual content of {@link #offerTextures(BiConsumer, BiConsumer, ResourceProvider, ExistingFileHelper)},
 * since that's where the textures are actually generated. Also, I made sure to include MI's assets in the fallback pack defined in {@link #run(CachedOutput)} so that textures using MI textures can be generated.
 */
public final class TexturesMIHookDatagenProvider implements DataProvider
{
	private final PackOutput         output;
	private final ExistingFileHelper existingFileHelper;
	
	private final String                  actualModId;
	private final Collection<FluidHolder> fluidHolders;
	
	public TexturesMIHookDatagenProvider(GatherDataEvent event, String actualModId, Collection<FluidHolder> fluidHolders)
	{
		this.output = event.getGenerator().getPackOutput();
		this.existingFileHelper = event.getExistingFileHelper();
		
		this.actualModId = actualModId;
		this.fluidHolders = fluidHolders;
	}
	
	private CompletableFuture<?> offerTextures(
			BiConsumer<NativeImage, String> textureWriter, BiConsumer<JsonElement, String> mcMetaWriter,
			ResourceProvider manager, ExistingFileHelper fileHelper
	)
	{
		TextureManager mtm = new TextureManager(manager, textureWriter, mcMetaWriter);
		
		List<CompletableFuture<?>> futures = new ArrayList<>();
		Consumer<IORunnable> defer = (r) -> futures.add(CompletableFuture.runAsync(r::safeRun, Util.backgroundExecutor()));
		
		for(FluidHolder holder : fluidHolders)
		{
			if(holder instanceof MIFluidHolder fluid)
			{
				defer.accept(() -> this.registerFluidTextures(mtm, fluid));
			}
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenComposeAsync(v -> mtm.doEndWork(), Util.backgroundExecutor())
				.thenRun(() -> mtm.markTexturesAsGenerated(fileHelper))
				.thenRun(() -> Tesseract.LOGGER.info("\"I used the png to destroy the png.\": 2 Electric Boogaloo"));
	}
	
	private void registerFluidTextures(TextureManager tm, MIFluidHolder fluid)
	{
		MIFluidProperties properties = fluid.properties();
		
		String path = "modern_industrialization:textures/fluid/";
		String bucket = path + "bucket.png";
		String bucket_content = path + "bucket_content.png";
		
		IColoramp fluidColoramp = new Coloramp(properties.color());
		
		try
		{
			NativeImage bucket_image = tm.getAssetAsTexture(bucket);
			NativeImage bucket_content_image = tm.getAssetAsTexture(bucket_content);
			TextureHelper.colorize(bucket_content_image, fluidColoramp);
			NativeImage oldBucketImage = bucket_image;
			bucket_image = TextureHelper.blend(oldBucketImage, bucket_content_image);
			oldBucketImage.close();
			if(properties.isGas())
			{
				TextureHelper.flip(bucket_image);
			}
			tm.addTexture(String.format("%s:textures/item/%s_bucket.png", actualModId, fluid.identifier().id()), bucket_image);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		String pathFluid = path + String.format("template/%s.png", properties.texture().path);
		try
		{
			NativeImage fluidAnim = tm.getAssetAsTexture(pathFluid);
			TextureHelper.colorize(fluidAnim, fluidColoramp);
			TextureHelper.setAlpha(fluidAnim, properties.opacity());
			tm.addTexture(String.format("%s:textures/fluid/%s_still.png", actualModId, fluid.identifier().id()), fluidAnim, true);
			tm.addMcMeta(String.format("%s:textures/fluid/%s_still.png.mcmeta", actualModId, fluid.identifier().id()), properties.texture().mcMetaInfo);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@FunctionalInterface
	private interface IORunnable
	{
		void run() throws IOException;
		
		default void safeRun()
		{
			try
			{
				run();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		List<PackResources> packs = Lists.newArrayList();
		
		packs.add(new VanillaPackResourcesBuilder().exposeNamespace("minecraft").pushJarResources().build(VANILLA_PACK_INFO));
		
		Path nonGeneratedResources = output.getOutputFolder().resolve("../../main/resources");
		packs.add(new FastPathPackResources(
				new PackLocationInfo(
						"nonGen",
						Component.literal("Non-Generated Resources"),
						PackSource.BUILT_IN,
						Optional.empty()
				),
				nonGeneratedResources
		));
		
		packs.add(new FilePackResources(
				new PackLocationInfo(
						"modern_industrialization/generated",
						MIText.GeneratedResources.text(),
						PackSource.BUILT_IN,
						Optional.empty()
				),
				new FilePackResources.SharedZipFileAccess(ModList.get().getModFileById(MI.ID).getFile().getFilePath().toFile()),
				""
		));
		
		List<CompletableFuture<?>> jsonSaveFutures = Lists.newArrayList();
		MultiPackResourceManager fallbackProvider = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, packs);
		
		return this.generateTextures(cache, fallbackProvider, jsonSaveFutures::add)
				.whenComplete((result, throwable) -> fallbackProvider.close())
				.thenRunAsync(() -> CompletableFuture.allOf(jsonSaveFutures.toArray(CompletableFuture[]::new)), Util.backgroundExecutor());
	}
	
	private CompletableFuture<?> generateTextures(CachedOutput cache, ResourceProvider fallbackResourceProvider,
												  Consumer<CompletableFuture<?>> futureList)
	{
		Path generatedResources = output.getOutputFolder();
		List<PackResources> generatedPack = List.of(new FastPathPackResources(
				new PackLocationInfo(
						"gen",
						Component.literal("Generated Resources"),
						PackSource.BUILT_IN,
						Optional.empty()
				),
				generatedResources
		));
		
		MultiPackResourceManager outputPack = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, generatedPack);
		return this.offerTextures(
				(image, textureId) -> this.writeTexture(cache, image, textureId),
				(json, path) -> futureList.accept(this.customJsonSave(cache, json, path)),
				resourceLocation ->
				{
					Optional<Resource> generated = outputPack.getResource(resourceLocation);
					if(generated.isPresent())
					{
						return generated;
					}
					return fallbackResourceProvider.getResource(resourceLocation);
				},
				existingFileHelper
		).whenComplete((result, throwable) -> outputPack.close());
	}
	
	@SuppressWarnings("deprecation")
	private void writeTexture(CachedOutput cache, NativeImage image, String textureId)
	{
		try
		{
			Path path = output.getOutputFolder().resolve("assets").resolve(textureId.replace(':', '/'));
			cache.writeIfNeeded(path, image.asByteArray(), Hashing.sha1().hashBytes(image.asByteArray()));
		}
		catch (IOException ex)
		{
			throw new RuntimeException("Failed to write texture " + textureId, ex);
		}
	}
	
	private CompletableFuture<?> customJsonSave(CachedOutput cache, JsonElement jsonElement, String path)
	{
		Path pathFormatted = output.getOutputFolder().resolve("assets").resolve(path.replace(':', '/'));
		return DataProvider.saveStable(cache, jsonElement, pathFormatted);
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}
