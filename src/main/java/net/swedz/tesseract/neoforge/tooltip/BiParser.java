package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.network.chat.Component;

import java.util.Map;

public interface BiParser<A, B> extends Parser<Map.Entry<A, B>>
{
	Component parse(A a, B b);
	
	default Component parse(Map.Entry<A, B> value)
	{
		return this.parse(value.getKey(), value.getValue());
	}
}
