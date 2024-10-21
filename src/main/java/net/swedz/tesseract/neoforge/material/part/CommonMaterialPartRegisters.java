package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

public final class CommonMaterialPartRegisters
{
	public static MaterialPartExtraRegister<ItemHolder<? extends Item>> itemTagCommon(String path)
	{
		return (r, m, h) -> h.tag(TagHelper.itemCommonWithChild(path, m.id().getPath()));
	}
	
	public static MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>> blockItemTagCommon(String path)
	{
		return (r, m, h) -> h.item().tag(TagHelper.itemCommonWithChild(path, m.id().getPath()));
	}
}
