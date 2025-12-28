package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface MaterialPartBlockFactory extends MaterialPartBlockBlockFactory, MaterialPartBlockItemFactory
{
	static MaterialPartBlockFactory of(MaterialPartBlockBlockFactory block, MaterialPartBlockItemFactory item)
	{
		if(block == null || item == null)
		{
			throw new NullPointerException("Block factory cannot have null block or item functions");
		}
		return new MaterialPartBlockFactory()
		{
			@Override
			public Block createBlock(MaterialPartRegisterContext context, BlockBehaviour.Properties blockProperties)
			{
				return block.createBlock(context, blockProperties);
			}
			
			@Override
			public BlockItem createItem(MaterialPartRegisterContext context, Block block, Item.Properties itemProperties)
			{
				return item.createItem(context, block, itemProperties);
			}
		};
	}
}
