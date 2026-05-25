package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;

@FunctionalInterface
public interface MachineBlockEntityFactory
{
	/**
	 * Creates the {@link MachineBlockEntity} for the machine.
	 *
	 * @param bep the block entity parameters
	 * @return the {@link MachineBlockEntity}
	 */
	MachineBlockEntity create(BEP bep);
}
