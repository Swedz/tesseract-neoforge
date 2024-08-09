package net.swedz.tesseract.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.swedz.tesseract.neoforge.compat.ModLoadedHelper;
import net.swedz.tesseract.neoforge.compat.mi.network.TesseractMIPackets;
import net.swedz.tesseract.neoforge.datagen.client.LanguageDatagenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TesseractMod.ID)
public final class TesseractMod
{
	public static final String ID   = "tesseract_api";
	public static final String NAME = "Tesseract API";
	
	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
	
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	
	public TesseractMod(IEventBus bus)
	{
		if(ModLoadedHelper.isLoaded("modern_industrialization"))
		{
			MIOnly.init(bus);
		}
		
		bus.addListener(GatherDataEvent.class, (event) ->
				event.getGenerator().addProvider(event.includeClient(), new LanguageDatagenProvider(event)));
	}
	
	private static final class MIOnly
	{
		private static void init(IEventBus bus)
		{
			bus.addListener(RegisterPayloadHandlersEvent.class, TesseractMIPackets::init);
		}
	}
}
