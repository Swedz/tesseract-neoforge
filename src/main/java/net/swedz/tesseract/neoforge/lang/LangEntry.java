package net.swedz.tesseract.neoforge.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.tooltip.Parser;

public record LangEntry(String key, String defaultText, Style style, Parser[] parsers)
{
	public MutableComponent toComponent(Object[] args)
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
		return Component.translatable(key, parsedArgs).withStyle(style);
	}
}
