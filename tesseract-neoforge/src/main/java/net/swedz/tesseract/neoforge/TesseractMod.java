package net.swedz.tesseract.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;
import net.swedz.tesseract.neoforge.compat.ModLoadedHelper;
import net.swedz.tesseract.neoforge.compat.mi.builtinhook.TesseractMIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.network.TesseractMIPackets;
import net.swedz.tesseract.neoforge.datagen.client.LanguageDatagenProvider;
import net.swedz.tesseract.neoforge.isolatedlistener.IsolatedListeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TesseractMod.ID)
public final class TesseractMod
{
	public static final String ID   = "tesseract";
	public static final String NAME = "Tesseract";
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(ID, path);
	}
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public TesseractMod(IEventBus bus)
	{
		IsolatedListeners.init();
		
		bus.addListener(RegisterCapabilitiesEvent.class, CapabilitiesListeners::triggerAll);
		
		if(isMILoaded())
		{
			MIHooks.register(this, MIHookRegistry.NONE, new TesseractMIHookListener());
			
			bus.addListener(RegisterPayloadHandlerEvent.class, TesseractMIPackets::init);
		}
		
		bus.addListener(GatherDataEvent.class, (event) ->
				event.getGenerator().addProvider(event.includeClient(), new LanguageDatagenProvider(event)));
	}
	
	public static boolean isMILoaded()
	{
		return ModLoadedHelper.isLoaded("modern_industrialization");
	}
}
