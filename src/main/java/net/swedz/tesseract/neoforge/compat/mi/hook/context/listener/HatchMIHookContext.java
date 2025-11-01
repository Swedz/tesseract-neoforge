package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.HatchMachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineBuilder;

public final class HatchMIHookContext extends MIHookContext
{
	public HatchMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public HatchMachineBuilder builder(String name, String englishName)
	{
		return MachineBuilder.hatch(hook, name, englishName);
	}
}
