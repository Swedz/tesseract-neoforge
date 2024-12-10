package net.swedz.tesseract.neoforge.compat.mi.hook;

import net.neoforged.neoforgespi.language.ModFileScanData;
import net.swedz.tesseract.neoforge.helper.ScanDataHelper;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.BiConsumer;

public final class MIHookEntrypointLoader
{
	private static final Logger LOGGER = LoggerFactory.getLogger("Tesseract API/MIMixinPlugin");
	
	private static final Type HOOK_ENTRYPOINT = Type.getType(MIHookEntrypoint.class);
	
	private static boolean LOADED = false;
	
	public static boolean isLoaded()
	{
		return LOADED;
	}
	
	public static void ensureLoaded()
	{
		if(!isLoaded())
		{
			load();
		}
	}
	
	private static <H> boolean registerEntrypoint(
			ModFileScanData data, Class<?> entrypointClass, Class<H> hookClass, BiConsumer<String, H> register
	) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
	{
		if(hookClass.isAssignableFrom(entrypointClass))
		{
			Class<? extends H> hookClassReference = entrypointClass.asSubclass(hookClass);
			H hook = hookClassReference.getConstructor().newInstance();
			String id = data.getIModInfoData().getFirst().getMods().getFirst().getModId();
			LOGGER.info("Registered entrypoint for mod {}: {}", id, hookClassReference.getName());
			register.accept(id, hook);
			return true;
		}
		return false;
	}
	
	private static void load()
	{
		LOADED = true;
		
		LOGGER.info("Starting MI hook entrypoint loader");
		
		ScanDataHelper.getAllScanData().stream()
				.flatMap((data) -> data.getAnnotations().stream()
						.filter((annotation) -> HOOK_ENTRYPOINT.equals(annotation.annotationType()))
						.map((annotation) -> Map.entry(data, annotation)))
				.forEach((entry) ->
				{
					ModFileScanData data = entry.getKey();
					ModFileScanData.AnnotationData annotation = entry.getValue();
					try
					{
						Class<?> entrypointClass = Class.forName(annotation.memberName());
						
						boolean registered = false;
						
						if(registerEntrypoint(data, entrypointClass, MIHookListener.class, MIHooks::registerListener))
						{
							registered = true;
						}
						else if(registerEntrypoint(data, entrypointClass, MIHookRegistry.class, MIHooks::registerRegistry))
						{
							registered = true;
						}
						else if(registerEntrypoint(data, entrypointClass, MIHookEfficiency.class, MIHooks::registerEfficiencyListener))
						{
							registered = true;
						}
						
						if(!registered)
						{
							LOGGER.error("MIHookEntrypoint {} does not implement a valid hook entrypoint", annotation.memberName());
						}
					}
					catch (Throwable ex)
					{
						LOGGER.error("Exception constructing entrypoint:", ex);
					}
				});
		
		LOGGER.info("Done MI hook entrypoint loader");
	}
}
