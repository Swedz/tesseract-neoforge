package net.swedz.tesseract.neoforge.compat.mi.hook;

import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Internal
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
	
	MIHook withListener(MIHookListener listener)
	{
		Objects.requireNonNull(listener);
		
		this.listener = listener;
		
		return this;
	}
	
	MIHook withRegistry(MIHookRegistry registry)
	{
		Objects.requireNonNull(registry);
		
		this.registry = registry;
		
		return this;
	}
	
	MIHook withEfficiencyListener(MIHookEfficiency efficiencyListener)
	{
		Objects.requireNonNull(efficiencyListener);
		
		this.efficiencyListener = efficiencyListener;
		
		return this;
	}
}
