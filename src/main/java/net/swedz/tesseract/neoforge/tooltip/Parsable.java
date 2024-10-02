package net.swedz.tesseract.neoforge.tooltip;

public interface Parsable
{
	<T> Parsable arg(T arg, Parser<T> parser);
	
	<A, B> Parsable arg(A a, B b, BiParser<A, B> parser);
	
	Parsable arg(Object arg);
}
