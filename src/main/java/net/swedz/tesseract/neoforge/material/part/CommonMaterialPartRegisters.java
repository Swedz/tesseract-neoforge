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
		return (registry, material, holder) -> holder.tag(TagHelper.itemCommonWithChild(path, material.id().getPath()));
	}
	
	public static MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>> blockItemTagCommon(String path)
	{
		return (registry, material, holder) -> holder.item().tag(TagHelper.itemCommonWithChild(path, material.id().getPath()));
	}
}
