package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface RegisteredMaterialPart
{
	Item asItem();
	
	default String itemReference()
	{
		return BuiltInRegistries.ITEM.getKey(this.asItem()).toString();
	}
	
	boolean hasBlock();
	
	Block asBlock();
	
	static RegisteredMaterialPart existingItem(String itemReference, Supplier<? extends Item> item)
	{
		return new RegisteredMaterialPart()
		{
			@Override
			public Item asItem()
			{
				return item.get();
			}
			
			@Override
			public String itemReference()
			{
				return itemReference == null ? RegisteredMaterialPart.super.itemReference() : itemReference;
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
	
	static RegisteredMaterialPart existingItem(String itemReference, ResourceLocation itemId)
	{
		return existingItem(itemReference, () -> BuiltInRegistries.ITEM.get(itemId));
	}
	
	static RegisteredMaterialPart existingItem(String itemReference, String itemId)
	{
		return existingItem(itemReference, ResourceLocation.parse(itemId));
	}
	
	static RegisteredMaterialPart existingBlock(Supplier<? extends Block> block)
	{
		return new RegisteredMaterialPart()
		{
			@Override
			public Item asItem()
			{
				return block.get().asItem();
			}
			
			@Override
			public boolean hasBlock()
			{
				return true;
			}
			
			@Override
			public Block asBlock()
			{
				return block.get();
			}
		};
	}
	
	static RegisteredMaterialPart existingBlock(ResourceLocation blockId)
	{
		return existingBlock(() -> BuiltInRegistries.BLOCK.get(blockId));
	}
	
	static RegisteredMaterialPart existingBlock(String blockId)
	{
		return existingBlock(ResourceLocation.tryParse(blockId));
	}
}
