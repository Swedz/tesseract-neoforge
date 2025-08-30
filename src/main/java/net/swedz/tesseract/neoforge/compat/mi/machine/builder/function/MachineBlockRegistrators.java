package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import net.minecraft.world.level.block.entity.BlockEntityType;

@FunctionalInterface
public interface MachineBlockRegistrators
{
	/**
	 * Applies some extra registration on the machine block entity.
	 *
	 * @param blockEntityType the block entity type
	 */
	void apply(BlockEntityType<?> blockEntityType);
}
