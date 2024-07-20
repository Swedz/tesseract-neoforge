package net.swedz.tesseract.neoforge.api;

import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface BiParser<T, K>
{
	Component parse(T t, K k);
}
