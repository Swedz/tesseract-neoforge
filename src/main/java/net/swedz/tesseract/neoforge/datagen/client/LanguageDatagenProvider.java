package net.swedz.tesseract.neoforge.datagen.client;

import com.google.common.collect.Sets;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.lang.LangInstance;

import java.util.Set;

public final class LanguageDatagenProvider extends LanguageProvider
{
	private static final Set<LangInstance<?>> INSTANCES = Sets.newHashSet();
	
	public static void include(LangInstance<?> instance)
	{
		INSTANCES.add(instance);
	}
	
	public LanguageDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), Tesseract.ID, "en_us");
	}
	
	@Override
	protected void addTranslations()
	{
		for(var instance : INSTANCES)
		{
			instance.datagen(this);
		}
		
		this.add("eu_cost_transformer.%s.%s".formatted(Tesseract.ID, "percentage"), "%d%%");
	}
}
