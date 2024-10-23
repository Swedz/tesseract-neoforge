package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface MaterialPartBlockItemFactory
{
	BlockItem createItem(MaterialPartRegisterContext context, Block block, Item.Properties itemProperties);
}
