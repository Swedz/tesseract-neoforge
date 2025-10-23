package net.swedz.tesseract.neoforge.lang;

import net.swedz.tesseract.neoforge.tooltip.TextLine;

import java.util.function.Function;

public record TextLineReference<T extends TextLine>(
		Class<T> textLineClass,
		Function<String, T> textLineFactory
)
{
	public static final TextLineReference<TextLine> DEFAULT = new TextLineReference<>(TextLine.class, TextLine::new);
}
