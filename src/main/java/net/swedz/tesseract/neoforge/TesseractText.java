package net.swedz.tesseract.neoforge;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum TesseractText
{
	MI_MACHINE_BATCHER_RECIPE("Can run %s recipes in batches."),
	MI_MACHINE_BATCHER_SIZE_AND_COST("Runs in batches of up to %d at %s the EU cost.");
	
	private final String englishText;
	
	TesseractText(String englishText)
	{
		this.englishText = englishText;
	}
	
	public String englishText()
	{
		return englishText;
	}
	
	public String getTranslationKey()
	{
		return "text.%s.%s".formatted(TesseractMod.ID, this.name().toLowerCase());
	}
	
	public MutableComponent text()
	{
		return Component.translatable(this.getTranslationKey());
	}
	
	public MutableComponent text(Object... args)
	{
		return Component.translatable(this.getTranslationKey(), args);
	}
}
