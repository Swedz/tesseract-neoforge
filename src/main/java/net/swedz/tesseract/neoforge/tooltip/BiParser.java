package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.api.tuple.Pair;

public interface BiParser<A, B> extends Parser<Pair<A, B>>
{
	Component parse(A a, B b);
	
	default Component parse(Pair<A, B> value)
	{
		return this.parse(value.a(), value.b());
	}
	
	@Override
	default BiParser<A, B> withStyle(Style style)
	{
		return (a, b) -> this.parse(a, b).copy().setStyle(style);
	}
	
	@Override
	default BiParser<A, B> plain()
	{
		return (a, b) -> this.parse(a, b).plainCopy();
	}
}
