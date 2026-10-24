package net.swedz.tesseract.neoforge.lang.style;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.lang.LangContext;

public record LangEntryStyle(
		LangContext context,
		StyleProvider style
)
{
	public Style get(HolderLookup.Provider registries)
	{
		return style.get(context.withRegistries(registries));
	}
}
