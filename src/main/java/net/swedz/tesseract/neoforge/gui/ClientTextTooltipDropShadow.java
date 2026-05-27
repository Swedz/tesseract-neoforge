package net.swedz.tesseract.neoforge.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;

public record ClientTextTooltipDropShadow(
		FormattedCharSequence text,
		boolean dropShadow
) implements ClientTooltipComponent
{
	@Override
	public int getWidth(Font font)
	{
		return font.width(text);
	}
	
	@Override
	public int getHeight(Font font)
	{
		return 10;
	}
	
	@Override
	public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y)
	{
		graphics.text(font, text, x, y, -1, dropShadow);
	}
}
