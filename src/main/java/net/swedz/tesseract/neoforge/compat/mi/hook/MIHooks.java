package net.swedz.tesseract.neoforge.compat.mi.hook;

import com.google.common.collect.Maps;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class MIHooks
{
	private static final Map<String, MIHook>   HOOKS                = Maps.newHashMap();
	private static final Set<MIHookEfficiency> EFFICIENCY_LISTENERS = new TreeSet<>(Comparator.comparingInt(MIHookEfficiency::getPriority));
	
	public static void registerListener(String modId, MIHookListener listener)
	{
		Objects.requireNonNull(modId);
		Objects.requireNonNull(listener);
		
		MIHook hook = getHook(modId);
		
		if(hook.hasListener())
		{
			throw new IllegalArgumentException("The mod %s already has a registered MI listener hook".formatted(modId));
		}
		
		hook.withListener(listener);
	}
	
	public static void registerRegistry(String modId, MIHookRegistry registry)
	{
		Objects.requireNonNull(modId);
		Objects.requireNonNull(registry);
		
		MIHook hook = getHook(modId);
		
		if(hook.hasRegistry())
		{
			throw new IllegalArgumentException("The mod %s already has a registered MI registry hook".formatted(modId));
		}
		
		hook.withRegistry(registry);
	}
	
	public static void registerEfficiencyListener(String modId, MIHookEfficiency efficiencyListener)
	{
		Objects.requireNonNull(modId);
		Objects.requireNonNull(efficiencyListener);
		
		MIHook hook = getHook(modId);
		
		if(hook.hasEfficiencyListener())
		{
			throw new IllegalArgumentException("The mod %s already has a registered MI efficiency hook".formatted(modId));
		}
		
		hook.withEfficiencyListener(efficiencyListener);
		EFFICIENCY_LISTENERS.add(efficiencyListener);
	}
	
	public static Set<String> getModIds()
	{
		return HOOKS.keySet();
	}
	
	private static MIHook getHook(String modId)
	{
		return HOOKS.computeIfAbsent(modId, (k) -> new MIHook());
	}
	
	public static MIHookRegistry getRegistry(String modId)
	{
		if(!HOOKS.containsKey(modId))
		{
			throw new IllegalArgumentException("No hook registered for mod %s".formatted(modId));
		}
		return getHook(modId).registry();
	}
	
	public static void triggerHookListeners(Consumer<MIHookListener> action)
	{
		for(Map.Entry<String, MIHook> entry : HOOKS.entrySet())
		{
			MIHookTracker.startTracking(entry.getKey());
			
			action.accept(entry.getValue().listener());
			
			MIHookTracker.stopTracking();
		}
	}
	
	public static void triggerHookEfficiencyListeners(EfficiencyMIHookContext context,
													  BiConsumer<MIHookEfficiency, EfficiencyMIHookContext> action)
	{
		for(MIHookEfficiency listener : EFFICIENCY_LISTENERS)
		{
			if(!listener.shouldAlwaysRun() && context.hasBeenModified())
			{
				continue;
			}
			action.accept(listener, context);
		}
	}
}
