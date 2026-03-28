package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface MaterialPartBlockBlockFactory
{
	Block createBlock(MaterialPartRegisterContext context, BlockBehaviour.Properties blockProperties);
}
