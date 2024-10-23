package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.Item;

public interface MaterialPartItemFactory
{
	Item create(Item.Properties properties);
}
