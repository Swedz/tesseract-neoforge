package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;

@FunctionalInterface
public interface MachineBlockHolderModifier
{
	/**
	 * Modifies the holder for the machine block.
	 *
	 * @param holder the {@link BlockWithItemHolder} that will be registered
	 */
	void modify(BlockWithItemHolder<?, ?> holder);
}
