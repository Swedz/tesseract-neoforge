package net.swedz.tesseract.neoforge.lang.placeholder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.lang.LangContext;

public record LangEntryPlaceholder(
		LangContext context,
		String block,
		PlaceholderProvider provider
)
{
	public Component resolve(HolderLookup.Provider registries)
	{
		return provider.resolve(context.withRegistries(registries), block);
	}
}
