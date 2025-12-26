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
	
	public void datagen(LanguageProvider provider)
	{
		for(var entry : handler.entries())
		{
			if(entry.defaultText() != null)
			{
				provider.add(entry.key(), entry.defaultText());
			}
		}
	}
}
