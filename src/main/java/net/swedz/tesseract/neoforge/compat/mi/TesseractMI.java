package net.swedz.tesseract.neoforge.compat.mi;

import net.neoforged.bus.api.IEventBus;

public final class TesseractMI
{
	public static void init(IEventBus bus)
	{
		TesseractMILootConditions.init(bus);
	}
}
