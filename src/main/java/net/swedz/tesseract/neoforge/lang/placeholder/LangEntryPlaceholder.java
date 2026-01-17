package net.swedz.tesseract.neoforge.lang.placeholder;

import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.lang.LangContext;

public record LangEntryPlaceholder(
		LangContext context,
		String block,
		PlaceholderProvider provider
)
{
	public Component resolve()
	{
		return provider.resolve(context, block);
	}
}
