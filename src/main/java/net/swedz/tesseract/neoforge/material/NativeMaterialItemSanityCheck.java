package net.swedz.tesseract.neoforge.material;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.swedz.tesseract.neoforge.Tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@EventBusSubscriber(modid = Tesseract.ID)
public final class NativeMaterialItemSanityCheck
{
	private static final Logger LOGGER = LoggerFactory.getLogger("Tesseract API/NativeMaterialItemSanityCheck");
	
	private static final Map<Identifier, Identifier> IDS_TO_CHECK = Maps.newHashMap();
	
	static void track(Identifier material, Identifier id)
	{
		IDS_TO_CHECK.put(id, material);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void onLoadComplete(FMLCommonSetupEvent event)
	{
		LOGGER.info("Starting validation process...");
		IDS_TO_CHECK.forEach((id, material) ->
		{
			if(BuiltInRegistries.ITEM.getOptional(id).isEmpty())
			{
				throw new IllegalArgumentException("Failed to validate item id '%s' on material '%s' as it does not exist".formatted(id, material));
			}
		});
		LOGGER.info("Successfully validated {} item ids.", IDS_TO_CHECK.size());
	}
}
