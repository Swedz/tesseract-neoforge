package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.compat.vanilla.material.property.OreDrops;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.compat.vanilla.material.property.VanillaMaterialProperties.*;

public final class CommonMaterialPartRegisters
{
	public static MaterialPartExtraRegister<ItemHolder<? extends Item>> itemTagCommon(String path)
	{
		return (registry, material, properties, holder) -> holder.tag(TagHelper.itemCommonWithChild(path, material.id().getPath()));
	}
	
	public static MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>> blockItemTagCommon(String path)
	{
		return (registry, material, properties, holder) -> holder.item().tag(TagHelper.itemCommonWithChild(path, material.id().getPath()));
	}
	
	public static MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>> oreDrop()
	{
		return (registry, material, properties, holder) -> holder.withLootTable((block) ->
		{
			if(!properties.has(ORE_DROP_PART))
			{
				throw new IllegalArgumentException("No ore drop part set");
			}
			OreDrops drops = properties.get(ORE_DROP_PART);
			if(drops == null || drops.drop() == null || !material.has(drops.drop()))
			{
				throw new IllegalArgumentException("Could not find ore drop part");
			}
			Item drop = material.get(drops.drop()).asItem();
			return (provider) -> provider.createOreDrop(block.get(), drop);
		});
	}
}
