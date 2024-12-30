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

public final class MIHooks
{
	private static final Map<String, MIHook>   HOOKS                = Maps.newHashMap();
	private static final Set<MIHookEfficiency> EFFICIENCY_LISTENERS = new TreeSet<>(Comparator.comparingInt(MIHookEfficiency::getPriority));
	
	static void registerListener(String modId, MIHookListener listener)
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
	
	static void registerRegistry(String modId, MIHookRegistry registry)
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
	
	static void registerEfficiencyListener(String modId, MIHookEfficiency efficiencyListener)
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
	
	@ApiStatus.Internal
	public static Set<String> getModIds()
	{
		return HOOKS.keySet();
	}
	
	@ApiStatus.Internal
	private static MIHook getHook(String modId)
	{
		MIHookEntrypointLoader.ensureLoaded();
		return HOOKS.computeIfAbsent(modId, MIHook::new);
	}
	
	@ApiStatus.Internal
	public static MIHookRegistry getRegistry(String modId)
	{
		MIHookEntrypointLoader.ensureLoaded();
		if(!HOOKS.containsKey(modId))
		{
			throw new IllegalArgumentException("No hook registered for mod %s".formatted(modId));
		}
		return getHook(modId).registry();
	}
	
	@ApiStatus.Internal
	public static void triggerHookListeners(BiConsumer<MIHook, MIHookListener> action)
	{
		MIHookEntrypointLoader.ensureLoaded();
		for(var entry : HOOKS.entrySet())
		{
			action.accept(entry.getValue(), entry.getValue().listener());
		}
	}
	
	@ApiStatus.Internal
	public static void triggerHookEfficiencyListeners(EfficiencyMIHookContext context,
													  BiConsumer<MIHookEfficiency, EfficiencyMIHookContext> action)
	{
		MIHookEntrypointLoader.ensureLoaded();
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
