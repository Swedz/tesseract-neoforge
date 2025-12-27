package net.swedz.tesseract.neoforge.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyEntry;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.util.function.Supplier;

public record LangEntry(
		String key,
		String defaultText,
		Supplier<Style> style,
		Supplier<Parser<?>>[] parsers
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
				var parsedArg = ((Parser) parsers[index].get()).parse(arg);
				parsedArgs[index] = parsedArg;
			}
		}
		return Component.translatable(key, parsedArgs).withStyle(style.get());
	}
}
