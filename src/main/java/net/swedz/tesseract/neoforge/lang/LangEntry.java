package net.swedz.tesseract.neoforge.lang;

import net.minecraft.core.HolderLookup;
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
			boolean datagen,
			LangEntryStyle style,
			LangEntryParser[] parsers,
			LangEntryPlaceholder[] placeholders,
			int contextualRegistryParameterIndex
	) implements LangEntry<Component>
	{
		private HolderLookup.Provider resolveRegistries(Object[] args)
		{
			if(contextualRegistryParameterIndex == -1)
			{
				return null;
			}
			return (HolderLookup.Provider) args[contextualRegistryParameterIndex];
		}
		
		@Override
		public MutableComponent resolve(Object[] args)
		{
			var registries = this.resolveRegistries(args);
			Object[] parsedArgs = new Object[placeholders.length];
			int argIndex = 0;
			for(int placeholderIndex = 0; placeholderIndex < placeholders.length; placeholderIndex++)
			{
				var placeholder = placeholders[placeholderIndex];
				if(placeholder == null)
				{
					if(args != null)
					{
						if(argIndex != contextualRegistryParameterIndex)
						{
							var arg = args[argIndex];
							var parsedArg = parsers[argIndex].parse(registries, arg);
							parsedArgs[placeholderIndex] = parsedArg;
						}
						argIndex++;
					}
				}
				else
				{
					parsedArgs[placeholderIndex] = placeholder.resolve(registries);
				}
			}
			return Component.translatableWithFallback(key, includeFallback ? defaultText : null, parsedArgs).withStyle(style.get(registries));
		}
	}
}
