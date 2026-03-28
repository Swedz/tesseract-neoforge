package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.SpecialMachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityWithGuiFactory;

public final class SingleBlockSpecialMachinesMIHookContext extends MIHookContext
{
	public SingleBlockSpecialMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public SpecialMachineBuilder builder(String name, String englishName, MachineBlockEntityWithGuiFactory factory)
	{
		return MachineBuilder.special(hook, name, englishName, false, factory);
	}
	
	public SpecialMachineBuilder builder(String name, String englishName, MachineBlockEntityFactory factory)
	{
		return this.builder(name, englishName, (bep, gui) -> factory.create(bep));
	}
}
