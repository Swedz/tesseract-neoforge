package net.swedz.tesseract.neoforge.lang.parser;

import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.lang.LangContext;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.util.function.Supplier;

public interface ParserProvider<T>
{
	static <T> ParserProvider<T> simple(Supplier<Parser<T>> parser)
	{
		Assert.notNull(parser);
		return (__, value) -> parser.get().parse(value);
	}
	
	static <T> ParserProvider<T> simple(Parser<T> parser)
	{
		Assert.notNull(parser);
		return (__, value) -> parser.parse(value);
	}
	
	Component parse(LangContext context, T value);
}
