package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.function.Consumer;
import java.util.function.Function;

public final class CommonMaterialPartRegisters
{
	public static MaterialPart.ExtraRegister<ItemHolder<Item>> itemModel(Function<ItemHolder<Item>, Consumer<ItemModelBuilder>> modelBuilderCreator)
	{
		return (r, m, h) -> h.withModel(modelBuilderCreator);
	}
	
	public static MaterialPart.ExtraRegister<ItemHolder<Item>> itemTagCommon(String path)
	{
		return (r, m, h) -> h.tag(TagHelper.itemCommonWithChild(path, m.id()));
	}
	
	public static MaterialPart.ExtraRegister<BlockWithItemHolder<Block, BlockItem>> blockModel(Function<BlockHolder<Block>, Consumer<BlockStateProvider>> modelBuilderCreator)
	{
		return (r, m, h) -> h.withModel(modelBuilderCreator);
	}
	
	public static MaterialPart.ExtraRegister<BlockWithItemHolder<Block, BlockItem>> blockDropsSelf()
	{
		return (r, m, h) -> h.withLootTable(CommonLootTableBuilders::self);
	}
	
	public static MaterialPart.ExtraRegister<BlockWithItemHolder<Block, BlockItem>> blockItemTagCommon(String path)
	{
		return (r, m, h) -> h.item().tag(TagHelper.itemCommonWithChild(path, m.id()));
	}
}
