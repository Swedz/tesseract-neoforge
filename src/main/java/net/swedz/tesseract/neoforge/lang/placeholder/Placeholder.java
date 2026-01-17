package net.swedz.tesseract.neoforge.lang.placeholder;

import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.lang.LangContext;

public record Placeholder(
		PlaceholderFilter filter,
		PlaceholderProvider provider
) implements PlaceholderFilter, PlaceholderProvider
{
	@Override
	public boolean test(String block)
	{
		return filter.test(block);
	}
	
	@Override
	public Component resolve(LangContext context, String block)
	{
		return provider.resolve(context, block);
	}
}
