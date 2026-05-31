package net.swedz.tesseract.neoforge.helper.datagen;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;

public interface SimpleModelCollectorByIdentifier
{
	default void register(Identifier id, ItemModel.Unbaked model)
	{
		throw new UnsupportedOperationException();
	}
}
