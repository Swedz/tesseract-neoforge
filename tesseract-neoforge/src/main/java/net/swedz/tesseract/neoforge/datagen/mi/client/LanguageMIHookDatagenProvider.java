package net.swedz.tesseract.neoforge.datagen.mi.client;

import aztech.modern_industrialization.MI;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;

import java.util.function.Consumer;

public final class LanguageMIHookDatagenProvider extends LanguageProvider
{
	private final String actualModId;
	
	public LanguageMIHookDatagenProvider(GatherDataEvent event, String actualModId)
	{
		super(event.getGenerator().getPackOutput(), MI.ID, "en_us");
		this.actualModId = actualModId;
	}
	
	@Override
	protected void addTranslations()
	{
		for(Consumer<LanguageProvider> action : MIHookTracker.getLanguageEntries(actualModId))
		{
			action.accept(this);
		}
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}
