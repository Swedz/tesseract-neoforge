package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineGuiConfiguration;

@FunctionalInterface
public interface MachineBlockEntityWithGuiFactory
{
	/**
	 * Creates the {@link MachineBlockEntity} for the machine.
	 *
	 * @param bep the block entity parameters
	 * @param gui the gui configuration
	 * @return the {@link MachineBlockEntity}
	 */
	MachineBlockEntity create(BEP bep, MachineGuiConfiguration gui);
}
