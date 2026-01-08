package net.swedz.tesseract.neoforge.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyEntry;
import net.swedz.tesseract.neoforge.lang.parser.LangEntryParser;
import net.swedz.tesseract.neoforge.lang.style.LangEntryStyle;

public record LangEntry(
		String key,
		String defaultText,
		boolean includeFallback,
		LangEntryStyle style,
		LangEntryParser[] parsers
) implements InterfaceProxyEntry<Component>
{
	@Override
	public MutableComponent resolve(Object[] args)
	{
		Object[] parsedArgs = new Object[0];
		if(args != null)
		{
			parsedArgs = new Object[args.length];
			for(int index = 0; index < args.length; index++)
			{
				var arg = args[index];
				var parsedArg = parsers[index].parse(arg);
				parsedArgs[index] = parsedArg;
			}
		}
		return Component.translatableWithFallback(key, includeFallback ? defaultText : null, parsedArgs).withStyle(style.get());
	}
}
