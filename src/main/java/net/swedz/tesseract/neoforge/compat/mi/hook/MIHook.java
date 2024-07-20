package net.swedz.tesseract.neoforge.compat.mi.hook;

import java.util.Objects;

public final class MIHook
{
	private MIHookRegistry   registry           = MIHookRegistry.NONE;
	private MIHookListener   listener           = MIHookListener.NONE;
	private MIHookEfficiency efficiencyListener = MIHookEfficiency.NONE;
	
	public MIHookRegistry registry()
	{
		return registry;
	}
	
	public boolean hasRegistry()
	{
		return registry != MIHookRegistry.NONE;
	}
	
	public MIHookListener listener()
	{
		return listener;
	}
	
	public boolean hasListener()
	{
		return listener != MIHookListener.NONE;
	}
	
	public MIHookEfficiency efficiencyListener()
	{
		return efficiencyListener;
	}
	
	public boolean hasEfficiencyListener()
	{
		return efficiencyListener != MIHookEfficiency.NONE;
	}
	
	MIHook withListener(MIHookRegistry registry, MIHookListener listener)
	{
		Objects.requireNonNull(registry);
		Objects.requireNonNull(listener);
		
		this.registry = registry;
		this.listener = listener;
		
		return this;
	}
	
	MIHook withEfficiencyListener(MIHookEfficiency efficiencyListener)
	{
		Objects.requireNonNull(efficiencyListener);
		
		this.efficiencyListener = efficiencyListener;
		
		return this;
	}
}
