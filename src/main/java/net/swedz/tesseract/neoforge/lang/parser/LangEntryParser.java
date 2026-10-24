package net.swedz.tesseract.neoforge.lang.parser;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.lang.LangContext;

public record LangEntryParser(
		LangContext context,
		ParserProvider parser
)
{
	public Component parse(HolderLookup.Provider registries, Object value)
	{
		return parser.parse(context.withRegistries(registries), value);
	}
}
