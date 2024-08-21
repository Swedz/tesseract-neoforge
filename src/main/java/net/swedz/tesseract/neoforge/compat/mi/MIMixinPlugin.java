package net.swedz.tesseract.neoforge.compat.mi;

import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.neoforged.neoforgespi.locating.IModFile;
import net.swedz.tesseract.neoforge.compat.ModLoadedHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.TesseractMIHookEntrypoint;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public final class MIMixinPlugin implements IMixinConfigPlugin
{
	private static final Logger LOGGER = LoggerFactory.getLogger("Tesseract API/MIMixinPlugin");
	
	private static final Type HOOK_ENTRYPOINT = Type.getType(TesseractMIHookEntrypoint.class);
	
	private <H> boolean registerEntrypoint(ModFileScanData data, Class<?> entrypointClass, Class<H> hookClass, BiConsumer<String, H> register) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
	{
		if(hookClass.isAssignableFrom(entrypointClass))
		{
			Class<? extends H> hookClassReference = entrypointClass.asSubclass(hookClass);
			H hook = hookClassReference.getConstructor().newInstance();
			String id = data.getIModInfoData().getFirst().getMods().getFirst().getModId();
			register.accept(id, hook);
			return true;
		}
		return false;
	}
	
	private List<ModFileScanData> getAllScanData()
	{
		return LoadingModList.get().getMods().stream()
				.map(IModInfo::getOwningFile)
				.filter(Objects::nonNull)
				.map(IModFileInfo::getFile)
				.distinct()
				.map(IModFile::getScanResult)
				.toList();
	}
	
	@Override
	public void onLoad(String mixinPackage)
	{
		LOGGER.info("Starting MI hook entrypoint loader");
		
		this.getAllScanData().stream()
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
						
						if(this.registerEntrypoint(data, entrypointClass, MIHookListener.class, MIHooks::registerListener))
						{
							registered = true;
						}
						else if(this.registerEntrypoint(data, entrypointClass, MIHookRegistry.class, MIHooks::registerRegistry))
						{
							registered = true;
						}
						else if(this.registerEntrypoint(data, entrypointClass, MIHookEfficiency.class, MIHooks::registerEfficiencyListener))
						{
							registered = true;
						}
						
						if(!registered)
						{
							LOGGER.error("TesseractMIHookEntrypoint {} does not implement a valid hook entrypoint", annotation.memberName());
						}
					}
					catch (Throwable ex)
					{
						LOGGER.error("Exception constructing entrypoint:", ex);
					}
				});
		
		LOGGER.info("Done MI hook entrypoint loader");
	}
	
	@Override
	public String getRefMapperConfig()
	{
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		return ModLoadedHelper.isLoaded("modern_industrialization");
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{
	}
	
	@Override
	public List<String> getMixins()
	{
		return null;
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
	}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
	}
}
