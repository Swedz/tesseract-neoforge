package net.swedz.tesseract.neoforge.helper.datagen;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.resources.Identifier;

public interface BlockStateGeneratorCollectorByIdentifier
{
	default void register(Identifier id, BlockStateModelDispatcher generator)
	{
		throw new UnsupportedOperationException();
	}
}
