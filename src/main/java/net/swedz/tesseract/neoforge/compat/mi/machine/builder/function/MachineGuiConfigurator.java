package net.swedz.tesseract.neoforge.compat.mi.machine.builder.function;

import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineGuiConfiguration;

@FunctionalInterface
public interface MachineGuiConfigurator
{
	/**
	 * <p>Configures the machine's gui configuration.</p>
	 *
	 * <p>It is extremely important you return the returned value of the last chained call of the configuration. Since
	 * it is immutable, if you return an instance from earlier in the chain, you will not make use of the subsequent
	 * modifications.</p>
	 *
	 * @param gui the machine gui configuration instance
	 * @return the resulting gui configuration
	 */
	MachineGuiConfiguration configure(MachineGuiConfiguration gui);
}
