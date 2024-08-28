package net.swedz.tesseract.neoforge.datagen.client;

import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.TesseractText;

public final class LanguageDatagenProvider extends LanguageProvider
{
	public LanguageDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), Tesseract.ID, "en_us");
	}
	
	@Override
	protected void addTranslations()
	{
		for(TesseractText text : TesseractText.values())
		{
			this.add(text.getTranslationKey(), text.englishText());
		}
		
		this.add("eu_cost_transformer.%s.%s".formatted(Tesseract.ID, "percentage"), "%d%%");
	}
}
