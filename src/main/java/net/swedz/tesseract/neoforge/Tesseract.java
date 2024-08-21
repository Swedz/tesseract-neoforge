package net.swedz.tesseract.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.datagen.client.LanguageDatagenProvider;
import net.swedz.tesseract.neoforge.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Tesseract.ID)
public final class Tesseract
{
	public static final String ID   = "tesseract_api";
	public static final String NAME = "Tesseract API";
	
	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public Tesseract(IEventBus bus)
	{
		ProxyManager.initEntrypoints();
		
		bus.addListener(GatherDataEvent.class, (event) ->
				event.getGenerator().addProvider(event.includeClient(), new LanguageDatagenProvider(event)));
	}
}
