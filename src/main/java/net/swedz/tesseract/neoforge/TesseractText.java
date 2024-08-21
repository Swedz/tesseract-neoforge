package net.swedz.tesseract.neoforge;

import net.swedz.tesseract.neoforge.tooltips.TranslatableTextEnum;

public enum TesseractText implements TranslatableTextEnum
{
	MI_MACHINE_BATCHER_RECIPE("Can run %s recipes in batches."),
	MI_MACHINE_BATCHER_SIZE_AND_COST("Runs in batches of up to %d at %s the EU cost."),
	TOOLTIPS_SHIFT_REQUIRED("Press [Shift] for info");
	
	private final String englishText;
	
	TesseractText(String englishText)
	{
		this.englishText = englishText;
	}
	
	@Override
	public String englishText()
	{
		return englishText;
	}
	
	@Override
	public String getTranslationKey()
	{
		return "text.%s.%s".formatted(TesseractMod.ID, this.name().toLowerCase());
	}
}
