package net.swedz.tesseract.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;
import net.swedz.tesseract.neoforge.compat.ModLoadedHelper;
import net.swedz.tesseract.neoforge.datagen.client.provider.mi.LanguageMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.isolatedlistener.IsolatedListeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TesseractMod.ID)
public final class TesseractMod
{
	public static final String ID   = "tesseract";
	public static final String NAME = "Tesseract";
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public TesseractMod(IEventBus bus)
	{
		bus.addListener(GatherDataEvent.class, (event) ->
		{
			if(isMILoaded())
			{
				event.getGenerator().addProvider(event.includeClient(), new LanguageMIHookDatagenProvider(event));
			}
		});
		
		IsolatedListeners.init();
		
		bus.addListener(RegisterCapabilitiesEvent.class, CapabilitiesListeners::triggerAll);
	}
	
	public static boolean isMILoaded()
	{
		return ModLoadedHelper.isLoaded("modern_industrialization");
	}
}
