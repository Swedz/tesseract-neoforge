package net.swedz.tesseract.neoforge.lang;

public record LangInstance<L>(Class<L> langClass, L lang, LangHandler handler)
{
	public LangInstance<L> load()
	{
		handler.loadValues(langClass, lang);
		return this;
	}
}
