package net.swedz.tesseract.neoforge.compat.mi.hook;

import com.google.common.collect.Maps;
import net.neoforged.fml.ModList;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;

@ApiStatus.Internal
public final class MIHooks
{
	private static final Map<String, MIHook>   HOOKS                = Maps.newHashMap();
	private static final Set<MIHookEfficiency> EFFICIENCY_LISTENERS = new TreeSet<>(Comparator.comparingInt(MIHookEfficiency::getPriority));
	
	private static List<MIHook> sortedHooks()
	{
		var modList = ModList.get();
		return HOOKS.values().stream()
				.sorted(Comparator.comparingInt((hook) ->
				{
					var container = modList.getModContainerById(hook.modId());
					if(container.isEmpty())
					{
						throw new IllegalStateException("Mod container for " + hook.modId() + " does not exist!");
					}
					return modList.getSortedMods().indexOf(container.get());
				}))
				.toList();
	}
	
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
	
	public static Set<String> getModIds()
	{
		return HOOKS.keySet();
	}
	
	private static MIHook getHook(String modId)
	{
		MIHookEntrypointLoader.ensureLoaded();
		return HOOKS.computeIfAbsent(modId, MIHook::new);
	}
	
	public static MIHookRegistry getRegistry(String modId)
	{
		MIHookEntrypointLoader.ensureLoaded();
		if(!HOOKS.containsKey(modId))
		{
			throw new IllegalArgumentException("No hook registered for mod %s".formatted(modId));
		}
		return getHook(modId).registry();
	}
	
	public static void triggerHookListeners(String modId, BiConsumer<MIHook, MIHookListener> action)
	{
		MIHookEntrypointLoader.ensureLoaded();
		var hook = getHook(modId);
		action.accept(hook, hook.listener());
	}
	
	public static void triggerHookListeners(BiConsumer<MIHook, MIHookListener> action)
	{
		MIHookEntrypointLoader.ensureLoaded();
		for(var entry : HOOKS.entrySet())
		{
			action.accept(entry.getValue(), entry.getValue().listener());
		}
	}
	
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
	
	public static void executeEnqueuedTasks()
	{
		MIHookEntrypointLoader.ensureLoaded();
		for(var hook : sortedHooks())
		{
			hook.executeEnqueuedTasks();
		}
	}
}
