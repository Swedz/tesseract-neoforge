package net.swedz.tesseract.neoforge.compat.mi.tooltip;

import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.MITooltips;
import net.swedz.tesseract.neoforge.tooltip.Parser;
import net.swedz.tesseract.neoforge.tooltip.TranslatableTextEnum;

public final class MITooltipCompat
{
	public static TranslatableTextEnum wrap(MIText text)
	{
		return new TranslatableTextEnum()
		{
			@Override
			public String englishText()
			{
				return text.getEnglishText();
			}
			
			@Override
			public String getTranslationKey()
			{
				return text.getTranslationKey();
			}
		};
	}
	
	public static <T> Parser<T> wrap(MITooltips.Parser<T> parser)
	{
		return parser::parse;
	}
}
