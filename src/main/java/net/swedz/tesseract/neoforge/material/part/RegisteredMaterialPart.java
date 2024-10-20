package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public interface RegisteredMaterialPart
{
	boolean hasItem();
	
	Item asItem();
	
	boolean hasBlock();
	
	Block asBlock();
	
	static RegisteredMaterialPart of(String modId, String resolvedPartId)
	{
		if(modId == null || resolvedPartId == null)
		{
			throw new NullPointerException();
		}
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, resolvedPartId);
		return new RegisteredMaterialPart()
		{
			private boolean loaded;
			
			private Block   block;
			private boolean hasBlock;
			
			private Item    item;
			private boolean hasItem;
			
			private void load()
			{
				if(!loaded)
				{
					loaded = true;
					block = BuiltInRegistries.BLOCK.get(id);
					hasBlock = block != Blocks.AIR;
					item = hasBlock ? block.asItem() : BuiltInRegistries.ITEM.get(id);
					hasItem = item != Items.AIR;
					if(!hasBlock && !hasItem)
					{
						throw new IllegalArgumentException("Could not find block or item for the id '%s'".formatted(id.toString()));
					}
				}
			}
			
			@Override
			public boolean hasItem()
			{
				this.load();
				return hasItem;
			}
			
			@Override
			public Item asItem()
			{
				this.load();
				if(!hasItem)
				{
					throw new UnsupportedOperationException();
				}
				return item;
			}
			
			@Override
			public boolean hasBlock()
			{
				this.load();
				return hasBlock;
			}
			
			@Override
			public Block asBlock()
			{
				this.load();
				if(!hasBlock)
				{
					throw new UnsupportedOperationException();
				}
				return block;
			}
		};
	}
	
	static RegisteredMaterialPart of(Item item)
	{
		if(item == Items.AIR)
		{
			throw new IllegalArgumentException("Cannot use air item for a material part");
		}
		return new RegisteredMaterialPart()
		{
			@Override
			public boolean hasItem()
			{
				return true;
			}
			
			@Override
			public Item asItem()
			{
				return item;
			}
			
			@Override
			public boolean hasBlock()
			{
				return false;
			}
			
			@Override
			public Block asBlock()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
	
	static RegisteredMaterialPart of(Block block)
	{
		if(block == Blocks.AIR)
		{
			throw new IllegalArgumentException("Cannot use air block for a material part");
		}
		Item item = block.asItem();
		boolean hasItem = item != Items.AIR;
		return new RegisteredMaterialPart()
		{
			@Override
			public Item asItem()
			{
				if(!hasItem)
				{
					throw new UnsupportedOperationException();
				}
				return item;
			}
			
			@Override
			public boolean hasItem()
			{
				return hasItem;
			}
			
			@Override
			public boolean hasBlock()
			{
				return true;
			}
			
			@Override
			public Block asBlock()
			{
				return block;
			}
		};
	}
}
