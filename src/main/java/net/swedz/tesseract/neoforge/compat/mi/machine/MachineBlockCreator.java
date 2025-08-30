package net.swedz.tesseract.neoforge.compat.mi.machine;

import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

@FunctionalInterface
public interface MachineBlockCreator
{
	/**
	 * Creates a machine block.
	 *
	 * @param ctor the {@link MachineBlockEntity} creator, required by MI
	 * @param properties the {@link BlockBehaviour.Properties} for the block
	 * @return the {@link MachineBlock}
	 */
	MachineBlock create(BiFunction<BlockPos, BlockState, MachineBlockEntity> ctor, BlockBehaviour.Properties properties);
}
