package net.swedz.tesseract.neoforge.lang.style;

import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.lang.LangContext;

import java.util.function.Supplier;

public interface StyleProvider
{
	static StyleProvider simple(Supplier<Style> style)
	{
		Assert.notNull(style);
		return (__) -> style.get();
	}
	
	static StyleProvider simple(Style style)
	{
		Assert.notNull(style);
		return (__) -> style;
	}
	
	Style get(LangContext context);
}
