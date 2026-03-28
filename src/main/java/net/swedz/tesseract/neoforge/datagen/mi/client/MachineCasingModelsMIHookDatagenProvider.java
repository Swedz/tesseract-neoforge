package net.swedz.tesseract.neoforge.datagen.mi.client;

import aztech.modern_industrialization.client.machines.models.CasingModel;
import aztech.modern_industrialization.client.machines.models.CasingModels;
import aztech.modern_industrialization.client.machines.models.UseBlockModelBakedModel;
import aztech.modern_industrialization.machines.models.MachineCasing;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.model.ModelGenerators;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class MachineCasingModelsMIHookDatagenProvider extends ModelProvider
{
	private final PackOutput.PathProvider              machineCasingsPathProvider;
	private final Map<Identifier, CasingModel.Unbaked> casingModels = new HashMap<>();
	
	public MachineCasingModelsMIHookDatagenProvider(GatherDataEvent event, String modId)
	{
		super(event.getGenerator().getPackOutput(), modId);
		
		this.machineCasingsPathProvider = event.getGenerator().getPackOutput().createPathProvider(PackOutput.Target.RESOURCE_PACK, CasingModels.FOLDER_NAME);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		return CompletableFuture.allOf(
				super.run(cache),
				DataProvider.saveAll(cache, CasingModel.Unbaked.CODEC, machineCasingsPathProvider, casingModels)
		);
	}
	
	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels)
	{
		var generators = new ModelGenerators(blockModels, itemModels);
		for(var action : MIHookTracker.getMachineCasingModels(modId))
		{
			action.accept(this, generators);
		}
	}
	
	private void generateCasing(MachineCasing casing, BlockStateModel.Unbaked model)
	{
		casingModels.put(casing.key, new CasingModel.Unbaked(model));
	}
	
	public void imitateBlock(MachineCasing casing, Block block)
	{
		this.generateCasing(casing, new UseBlockModelBakedModel.Unbaked(block));
	}
	
	public void cubeBottomTop(BlockModelGenerators blockModels, MachineCasing casing, Identifier side, Identifier bottom, Identifier top)
	{
		var modelId = casing.key.withPrefix("machine_casing/");
		ModelTemplates.CUBE_BOTTOM_TOP.create(
				modelId,
				new TextureMapping()
						.put(TextureSlot.SIDE, side)
						.put(TextureSlot.BOTTOM, bottom)
						.put(TextureSlot.TOP, top),
				blockModels.modelOutput
		);
		generateCasing(casing, new SingleVariant.Unbaked(BlockModelGenerators.plainModel(modelId)));
	}
	
	public void cubeAll(BlockModelGenerators blockModels, MachineCasing casing, Identifier side)
	{
		var modelId = casing.key.withPrefix("machine_casing/");
		ModelTemplates.CUBE_ALL.create(
				modelId,
				new TextureMapping().put(TextureSlot.ALL, side),
				blockModels.modelOutput
		);
		generateCasing(casing, new SingleVariant.Unbaked(BlockModelGenerators.plainModel(modelId)));
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}
