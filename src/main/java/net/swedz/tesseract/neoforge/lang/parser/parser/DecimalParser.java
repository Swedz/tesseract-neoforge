package net.swedz.tesseract.neoforge.lang.parser.parser;

import net.minecraft.network.chat.Component;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.lang.LangContext;
import net.swedz.tesseract.neoforge.lang.annotation.ParsedDecimal;
import net.swedz.tesseract.neoforge.lang.exception.MalformedParserException;
import net.swedz.tesseract.neoforge.lang.parser.ParserProvider;

import java.util.Locale;

public final class DecimalParser<T> implements ParserProvider<T>
{
	@Override
	public Component parse(LangContext context, T value)
	{
		if(context.hasAnnotation(ParsedDecimal.class))
		{
			var annotation = context.getAnnotation(ParsedDecimal.class);
			var decimalPlaces = annotation.value();
			
			Assert.that(decimalPlaces >= 0, "ParsedDecimal value must be >= 0", MalformedParserException::new);
			
			var format = "%." + decimalPlaces + "f";
			var parsedString = String.format(Locale.ROOT, format, value);
			return Component.literal(parsedString);
		}
		return Component.literal(String.valueOf(value));
	}
}
