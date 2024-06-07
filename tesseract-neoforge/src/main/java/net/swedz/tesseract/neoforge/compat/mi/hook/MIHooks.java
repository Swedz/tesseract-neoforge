package net.swedz.tesseract.neoforge.compat.mi.hook;

import com.google.common.collect.Maps;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public final class MIHooks
{
	private static final Map<String, MIHook> HOOKS = Maps.newHashMap();
	
	/**
	 * Registers a MI hook for your mod.
	 *
	 * @param modInstance the instance of your mod's main class
	 * @param hook the hook you want to use as the listener
	 * @throws IllegalArgumentException if the modInstance's class doesn't have the {@link Mod} annotation or if the mod already has a hook registered
	 */
	public static void register(Object modInstance, MIHook hook)
	{
		Objects.requireNonNull(modInstance);
		Objects.requireNonNull(hook);
		if(!modInstance.getClass().isAnnotationPresent(Mod.class))
		{
			throw new IllegalArgumentException("The mod argument must have the @Mod annotation");
		}
		
		Mod mod = modInstance.getClass().getAnnotation(Mod.class);
		String modId = mod.value();
		if(HOOKS.containsKey(modId))
		{
			throw new IllegalArgumentException("The mod %s already has a registered MI hook".formatted(modId));
		}
		
		HOOKS.put(modId, hook);
	}
	
	@ApiStatus.Internal
	public static void triggerHooks(Consumer<MIHook> action)
	{
		for(Map.Entry<String, MIHook> entry : HOOKS.entrySet())
		{
			MIHookTracker.startTracking(entry.getKey());
			
			action.accept(entry.getValue());
			
			MIHookTracker.stopTracking();
		}
	}
}
