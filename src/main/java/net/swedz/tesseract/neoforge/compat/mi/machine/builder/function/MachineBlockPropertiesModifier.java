package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import net.minecraft.world.level.block.state.BlockBehaviour;

@FunctionalInterface
public interface MachineBlockPropertiesModifier
{
	/**
	 * Modifies the block properties for the machine block.
	 *
	 * @param properties the {@link BlockBehaviour.Properties} of the block
	 */
	void modify(BlockBehaviour.Properties properties);
}
