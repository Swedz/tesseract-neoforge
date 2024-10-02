package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface TranslatableTextEnum extends Parsable
{
	String englishText();
	
	String getTranslationKey();
	
	default MutableComponent text()
	{
		return Component.translatable(this.getTranslationKey());
	}
	
	default MutableComponent text(Object... args)
	{
		return Component.translatable(this.getTranslationKey(), args);
	}
	
	@Override
	default <T> TextLine arg(T arg, Parser<T> parser)
	{
		return TextLine.line(this).arg(arg, parser);
	}
	
	@Override
	default <A, B> TextLine arg(A a, B b, BiParser<A, B> parser)
	{
		return TextLine.line(this).arg(a, b, parser);
	}
	
	@Override
	default TextLine arg(Object arg)
	{
		return TextLine.line(this).arg(arg);
	}
}
