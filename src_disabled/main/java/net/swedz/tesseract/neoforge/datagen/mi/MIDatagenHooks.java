package net.swedz.tesseract.neoforge.datagen.mi;

import aztech.modern_industrialization.client.datagen.texture.MISpriteSourceProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
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
			event.getGenerator().addProvider(true, new MISpriteSourceProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
		}
		
		public static void addTexturesHook(GatherDataEvent event, String modId, Collection<FluidHolder> fluidHolders)
		{
			event.getGenerator().addProvider(true, new TexturesMIHookDatagenProvider(event, modId, fluidHolders));
		}
		
		public static void withLanguageHook(LanguageProvider languageProvider, String modId)
		{
			for(var entry : MIHookTracker.getReiCategoryNames(modId))
			{
				languageProvider.add("rei_categories.%s.%s".formatted(entry.getKey().getNamespace(), entry.getKey().getPath()), entry.getValue());
			}
		}
		
		public static void addMachineCasingModelsHook(GatherDataEvent event, String modId)
		{
			event.getGenerator().addProvider(true, new MachineCasingModelsMIHookDatagenProvider(event, modId));
		}
	}
}
