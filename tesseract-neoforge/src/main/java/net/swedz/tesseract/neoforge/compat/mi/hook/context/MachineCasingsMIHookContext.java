package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookContext;

public final class MachineCasingsMIHookContext implements MIHookContext
{
	public MachineCasing register(String name)
	{
		return MachineCasings.create(name);
	}
}
