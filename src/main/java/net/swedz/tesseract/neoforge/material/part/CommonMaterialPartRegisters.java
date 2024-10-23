package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

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
	
	public static MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>> oreDrop()
	{
		return (registry, material, holder) -> holder.withLootTable((block) ->
		{
			MaterialPart dropPart = material.get(ORE_DROP_PART);
			if(dropPart == null || !material.has(dropPart))
			{
				throw new IllegalArgumentException("Could not find matching ore drop part");
			}
			Item drop = material.get(dropPart).asItem();
			return (provider) -> provider.createOreDrop(block.get(), drop);
		});
	}
}
