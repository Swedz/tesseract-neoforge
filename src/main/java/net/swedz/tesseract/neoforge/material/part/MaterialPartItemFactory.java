package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.item.Item;

public interface MaterialPartItemFactory
{
	Item create(MaterialPartRegisterContext context, Item.Properties properties);
}
