package net.swedz.tesseract.neoforge.compat.mi.hook;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class MIHooks
{
	private static final Map<String, MIHook> HOOKS = Maps.newHashMap();
	
	/**
	 * Registers a MI hook for your mod.
	 *
	 * @param modId    the id of your mod
	 * @param registry the registry to use for your hook
	 * @param listener the listener to use for your hook
	 * @throws IllegalArgumentException if the mod already has a hook registered
	 */
	public static void register(String modId, MIHookRegistry registry, MIHookListener listener)
	{
		Objects.requireNonNull(modId);
		Objects.requireNonNull(registry);
		Objects.requireNonNull(listener);
		
		if(HOOKS.containsKey(modId))
		{
			throw new IllegalArgumentException("The mod %s already has a registered MI hook".formatted(modId));
		}
		
		HOOKS.put(modId, new MIHook(registry, listener));
	}
	
	@ApiStatus.Internal
	public static Set<String> getModIds()
	{
		return HOOKS.keySet();
	}
	
	@ApiStatus.Internal
	public static MIHookRegistry getRegistry(String modId)
	{
		return HOOKS.get(modId).registry();
	}
	
	@ApiStatus.Internal
	public static void triggerHookListeners(Consumer<MIHookListener> action)
	{
		for(Map.Entry<String, MIHook> entry : HOOKS.entrySet())
		{
			MIHookTracker.startTracking(entry.getKey());
			
			action.accept(entry.getValue().listener());
			
			MIHookTracker.stopTracking();
		}
	}
}
