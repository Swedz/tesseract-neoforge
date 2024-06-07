package net.swedz.tesseract.neoforge.datagen.mi;

import aztech.modern_industrialization.datagen.texture.MISpriteSourceProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.datagen.mi.client.LanguageMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.datagen.mi.client.MachineCasingModelsMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.datagen.mi.client.TexturesMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.registry.holder.FluidHolder;

import java.util.Collection;

public final class MIDatagenHooks
{
	public static final class Client
	{
		public static void includeMISprites(GatherDataEvent event)
		{
			event.getGenerator().addProvider(event.includeClient(), new MISpriteSourceProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
		}
		
		public static void addTexturesHook(GatherDataEvent event, String modId, Collection<FluidHolder> fluidHolders)
		{
			event.getGenerator().addProvider(event.includeClient(), new TexturesMIHookDatagenProvider(event, modId, fluidHolders));
		}
		
		public static void addLanguageHook(GatherDataEvent event, String modId)
		{
			event.getGenerator().addProvider(event.includeClient(), new LanguageMIHookDatagenProvider(event, modId));
		}
		
		public static void addMachineCasingModelsHook(GatherDataEvent event, String modId)
		{
			event.getGenerator().addProvider(event.includeClient(), new MachineCasingModelsMIHookDatagenProvider(event, modId));
		}
	}
}
