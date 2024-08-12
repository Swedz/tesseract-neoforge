package net.swedz.tesseract.neoforge.datagen.mi;

import aztech.modern_industrialization.datagen.texture.MISpriteSourceProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.datagen.mi.client.MachineCasingModelsMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.datagen.mi.client.TexturesMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.registry.holder.FluidHolder;

import java.util.Collection;
import java.util.Map;

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
		
		public static void withLanguageHook(LanguageProvider languageProvider, String modId)
		{
			for(Map.Entry<ResourceLocation, String> entry : MIHookTracker.getReiCategoryNames(modId))
			{
				languageProvider.add("rei_categories.%s.%s".formatted(entry.getKey().getNamespace(), entry.getKey().getPath()), entry.getValue());
			}
		}
		
		public static void addMachineCasingModelsHook(GatherDataEvent event, String modId)
		{
			event.getGenerator().addProvider(event.includeClient(), new MachineCasingModelsMIHookDatagenProvider(event, modId));
		}
	}
}
