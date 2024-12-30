package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;

public abstract class MIHookContext
{
	protected final MIHook hook;
	
	public MIHookContext(MIHook hook)
	{
		this.hook = hook;
	}
}
