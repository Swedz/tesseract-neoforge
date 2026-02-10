package net.swedz.tesseract.neoforge.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.swedz.tesseract.interfaceproxy.InterfaceProxyEntry;
import net.swedz.tesseract.neoforge.lang.parser.LangEntryParser;
import net.swedz.tesseract.neoforge.lang.placeholder.LangEntryPlaceholder;
import net.swedz.tesseract.neoforge.lang.style.LangEntryStyle;

public interface LangEntry<R> extends InterfaceProxyEntry<R>
{
	record SubSection(
			LangHandler handler,
			Object instance
	) implements LangEntry<Object>
	{
		@Override
		public Object resolve(Object[] args)
		{
			return instance;
		}
	}
	
	record Text(
			String key,
			String defaultText,
			boolean includeFallback,
			LangEntryStyle style,
			LangEntryParser[] parsers,
			LangEntryPlaceholder[] placeholders
	) implements LangEntry<Component>
	{
		@Override
		public MutableComponent resolve(Object[] args)
		{
			Object[] parsedArgs = new Object[placeholders.length];
			int argIndex = 0;
			for(int placeholderIndex = 0; placeholderIndex < placeholders.length; placeholderIndex++)
			{
				var placeholder = placeholders[placeholderIndex];
				if(placeholder == null)
				{
					if(args != null)
					{
						var arg = args[argIndex];
						var parsedArg = parsers[argIndex].parse(arg);
						parsedArgs[placeholderIndex] = parsedArg;
						argIndex++;
					}
				}
				else
				{
					parsedArgs[placeholderIndex] = placeholder.resolve();
				}
			}
			return Component.translatableWithFallback(key, includeFallback ? defaultText : null, parsedArgs).withStyle(style.get());
		}
	}
}
