package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.multiblocks.HatchBlockEntity;
import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface MachineBlockHatchBlockEntityFactory
{
	/**
	 * Creates the {@link HatchBlockEntity} for the machine.
	 *
	 * @param bep       the block entity parameters
	 * @param input     true if this hatch is an input hatch, false otherwise
	 * @param machineId the full id of the machine
	 * @return the {@link HatchBlockEntity}
	 */
	HatchBlockEntity create(BEP bep, boolean input, Identifier machineId);
}
