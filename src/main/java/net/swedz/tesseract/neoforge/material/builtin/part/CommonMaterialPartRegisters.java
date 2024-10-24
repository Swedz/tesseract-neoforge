package net.swedz.tesseract.neoforge.material.builtin.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.material.builtin.property.OrePartDrops;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.material.part.MaterialPartAction;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public final class CommonMaterialPartRegisters
{
	public static MaterialPartAction<ItemHolder<? extends Item>> itemTagCommon(String path)
	{
		return (context, holder) -> holder.tag(TagHelper.itemCommonWithChild(path, context.material().id().getPath()));
	}
	
	public static MaterialPartAction<BlockWithItemHolder<Block, BlockItem>> blockItemTagCommon(String path)
	{
		return (context, holder) -> holder.item().tag(TagHelper.itemCommonWithChild(path, context.material().id().getPath()));
	}
	
	public static MaterialPartAction<BlockWithItemHolder<Block, BlockItem>> oreDrop()
	{
		return (context, holder) -> holder.withLootTable((block) ->
		{
			OrePartDrops drops = context.getOrThrow(ORE_DROP_PART);
			if(drops == null || drops.drop() == null)
			{
				throw new IllegalArgumentException("Could not find ore drop part");
			}
			Item drop = context.material().get(drops.drop()).asItem();
			return (provider) -> provider.createOreDrop(block.get(), drop);
		});
	}
}
