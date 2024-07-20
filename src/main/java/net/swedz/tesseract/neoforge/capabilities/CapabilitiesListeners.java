package net.swedz.tesseract.neoforge.capabilities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class CapabilitiesListeners
{
	private static final Map<String, List<Consumer<RegisterCapabilitiesEvent>>> LISTENERS = Maps.newHashMap();
	
	private static List<Consumer<RegisterCapabilitiesEvent>> getListeners(String modId)
	{
		return LISTENERS.computeIfAbsent(modId, (k) -> Lists.newArrayList());
	}
	
	public static void triggerAll(String modId, RegisterCapabilitiesEvent event)
	{
		getListeners(modId).forEach((action) -> action.accept(event));
	}
	
	public static void register(String modId, Consumer<RegisterCapabilitiesEvent> listener)
	{
		getListeners(modId).add(listener);
	}
}
