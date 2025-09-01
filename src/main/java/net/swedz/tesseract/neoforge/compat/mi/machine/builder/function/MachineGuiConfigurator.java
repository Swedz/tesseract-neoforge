package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineGuiConfiguration;

@FunctionalInterface
public interface MachineGuiConfigurator
{
	/**
	 * Configures the machine's gui configuration.
	 *
	 * @param gui the machine gui configuration instance
	 */
	void configure(MachineGuiConfiguration gui);
}
