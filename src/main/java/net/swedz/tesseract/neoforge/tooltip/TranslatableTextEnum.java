package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface TranslatableTextEnum
{
	String englishText();
	
	String getTranslationKey();
	
	default MutableComponent text()
	{
		return Component.translatable(this.getTranslationKey());
	}
	
	default MutableComponent text(Object... args)
	{
		return Component.translatable(this.getTranslationKey(), args);
	}
}
