package net.swedz.tesseract.neoforge.lang;

import net.neoforged.neoforge.common.data.LanguageProvider;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyInstance;

public record LangInstance<L>(
		Class<L> proxyClass,
		L proxy,
		LangHandler handler
) implements InterfaceProxyInstance<L, LangHandler>
{
	public L lang()
	{
		return proxy;
	}
	
	@Override
	public LangInstance<L> load()
	{
		InterfaceProxyInstance.super.load();
		return this;
	}
	
	private static void datagen(LangHandler handler, LanguageProvider provider)
	{
		for(var entry : handler.entries())
		{
			if(entry instanceof LangEntry.SubSection subSection)
			{
				datagen(subSection.handler(), provider);
			}
			else if(entry instanceof LangEntry.Text text)
			{
				if(text.datagen() && text.defaultText() != null)
				{
					provider.add(text.key(), text.defaultText());
				}
			}
			else
			{
				throw new UnsupportedOperationException();
			}
		}
	}
	
	public void datagen(LanguageProvider provider)
	{
		datagen(handler, provider);
	}
}
