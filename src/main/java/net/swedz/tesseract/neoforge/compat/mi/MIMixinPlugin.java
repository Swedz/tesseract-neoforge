package net.swedz.tesseract.neoforge.compat.mi;

import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.neoforged.neoforgespi.locating.IModFile;
import net.swedz.tesseract.neoforge.TesseractMod;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.TesseractMIHookEntrypoint;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public final class MIMixinPlugin implements IMixinConfigPlugin
{
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
		Type entrypointType = Type.getType(TesseractMIHookEntrypoint.class);
		for(ModFileScanData data : this.getAllScanData())
		{
			for(ModFileScanData.AnnotationData annotation : data.getAnnotations())
			{
				try
				{
					if(entrypointType.equals(annotation.annotationType()))
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
							TesseractMod.LOGGER.error("TesseractMIHookEntrypoint {} does not implement a valid hook entrypoint", annotation.memberName());
						}
					}
				}
				catch (Throwable ex)
				{
					TesseractMod.LOGGER.error("Exception constructing entrypoint:", ex);
				}
			}
		}
	}
	
	@Override
	public String getRefMapperConfig()
	{
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		return TesseractMod.isMILoaded();
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
