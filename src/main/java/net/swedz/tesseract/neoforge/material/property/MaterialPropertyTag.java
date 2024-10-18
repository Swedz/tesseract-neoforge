package net.swedz.tesseract.neoforge.material.property;

import net.minecraft.tags.TagKey;

import java.util.Optional;

public abstract class MaterialPropertyTag<T> extends MaterialProperty<Optional<TagKey<T>>>
{
	public MaterialPropertyTag(String key, Optional<TagKey<T>> defaultValue)
	{
		super(key, defaultValue);
	}
	
	public MaterialPropertyTag(String key)
	{
		super(key, Optional.empty());
	}
	
	public static class Item extends ItemOptional
	{
		public Item(String key, TagKey<net.minecraft.world.item.Item> defaultValue)
		{
			super(key, Optional.ofNullable(defaultValue));
		}
	}
	
	public static class ItemOptional extends MaterialPropertyTag<net.minecraft.world.item.Item>
	{
		public ItemOptional(String key, Optional<TagKey<net.minecraft.world.item.Item>> defaultValue)
		{
			super(key, defaultValue);
		}
		
		public ItemOptional(String key, TagKey<net.minecraft.world.item.Item> defaultValue)
		{
			super(key, Optional.ofNullable(defaultValue));
		}
		
		public ItemOptional(String key)
		{
			super(key);
		}
	}
	
	public static class Block extends BlockOptional
	{
		public Block(String key, TagKey<net.minecraft.world.level.block.Block> defaultValue)
		{
			super(key, Optional.of(defaultValue));
		}
	}
	
	public static class BlockOptional extends MaterialPropertyTag<net.minecraft.world.level.block.Block>
	{
		public BlockOptional(String key, Optional<TagKey<net.minecraft.world.level.block.Block>> defaultValue)
		{
			super(key, defaultValue);
		}
		
		public BlockOptional(String key, TagKey<net.minecraft.world.level.block.Block> defaultValue)
		{
			super(key, Optional.of(defaultValue));
		}
		
		public BlockOptional(String key)
		{
			super(key);
		}
	}
}
