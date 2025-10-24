package net.swedz.tesseract.neoforge.lang;

import net.neoforged.neoforge.common.data.LanguageProvider;

public record LangInstance<L>(Class<L> langClass, L lang, LangHandler handler)
{
	public LangInstance<L> load()
	{
		handler.loadValues(langClass, lang);
		return this;
	}
	
	public void datagen(LanguageProvider provider)
	{
		for(var entry : handler.entries())
		{
			if(entry.defaultText().isEmpty())
			{
				continue;
			}
			provider.add(entry.key(), entry.defaultText());
		}
	}
}
