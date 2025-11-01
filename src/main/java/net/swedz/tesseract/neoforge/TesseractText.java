package net.swedz.tesseract.neoforge;

import net.minecraft.network.chat.MutableComponent;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;
import net.swedz.tesseract.neoforge.lang.annotation.WithStyle;

public interface TesseractText
{
	@LangKey(text = "Press [Shift] for info")
	@WithStyle("tooltip")
	MutableComponent tooltipsShiftRequired();
}
