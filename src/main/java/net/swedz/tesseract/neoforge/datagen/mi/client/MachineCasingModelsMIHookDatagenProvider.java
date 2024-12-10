package net.swedz.tesseract.neoforge.datagen.mi.client;

import aztech.modern_industrialization.machines.models.MachineBakedModel;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.UseBlockModelUnbakedModel;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;

import java.util.function.Consumer;

public final class MachineCasingModelsMIHookDatagenProvider extends ModelProvider<BlockModelBuilder>
{
	public MachineCasingModelsMIHookDatagenProvider(GatherDataEvent event, String modId)
	{
		super(event.getGenerator().getPackOutput(), modId, MachineBakedModel.CASING_FOLDER, BlockModelBuilder::new, event.getExistingFileHelper());
	}
	
	@Override
	protected void registerModels()
	{
		for(Consumer<MachineCasingModelsMIHookDatagenProvider> action : MIHookTracker.getMachineCasingModels(modid))
		{
			action.accept(this);
		}
	}
	
	public void imitateBlock(MachineCasing casing, Block block)
	{
		getBuilder(casing.key.toString())
				.customLoader((bmb, existingFileHelper) -> new UseBlockModelModelBuilder<>(block, bmb, existingFileHelper));
	}
	
	public void cubeBottomTop(MachineCasing casing, ResourceLocation side, ResourceLocation bottom, ResourceLocation top)
	{
		this.cubeBottomTop(casing.key.toString(), side, bottom, top);
	}
	
	public void cubeAll(MachineCasing casing, ResourceLocation side)
	{
		this.cubeAll(casing.key.toString(), side);
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
	
	private static final class UseBlockModelModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T>
	{
		private final Block targetBlock;
		
		private UseBlockModelModelBuilder(Block targetBlock, T parent, ExistingFileHelper existingFileHelper)
		{
			super(UseBlockModelUnbakedModel.LOADER_ID, parent, existingFileHelper, false);
			this.targetBlock = targetBlock;
		}
		
		@Override
		public JsonObject toJson(JsonObject json)
		{
			JsonObject ret = super.toJson(json);
			ret.addProperty("block", BuiltInRegistries.BLOCK.getKey(targetBlock).toString());
			return ret;
		}
	}
}
