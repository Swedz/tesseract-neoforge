package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface MaterialPartBlockFactory
{
	Block createBlock(BlockBehaviour.Properties properties);
	
	BlockItem createItem(Block block, Item.Properties properties);
	
	static MaterialPartBlockFactory of(Function<BlockBehaviour.Properties, Block> block,
									   BiFunction<Block, Item.Properties, BlockItem> item)
	{
		if(block == null || item == null)
		{
			throw new NullPointerException("Block factory cannot have null block or item functions");
		}
		return new MaterialPartBlockFactory()
		{
			@Override
			public Block createBlock(BlockBehaviour.Properties properties)
			{
				return block.apply(properties);
			}
			
			@Override
			public BlockItem createItem(Block block, Item.Properties properties)
			{
				return item.apply(block, properties);
			}
		};
	}
}
