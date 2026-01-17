package net.swedz.tesseract.neoforge.lang.placeholder;

import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.lang.LangContext;

import java.util.function.Supplier;

public interface PlaceholderProvider
{
	static PlaceholderProvider simple(Supplier<Component> provider)
	{
		return (__, ___) -> provider.get();
	}
	
	static PlaceholderProvider simple(Component component)
	{
		return (__, ___) -> component;
	}
	
	Component resolve(LangContext context, String block);
}
