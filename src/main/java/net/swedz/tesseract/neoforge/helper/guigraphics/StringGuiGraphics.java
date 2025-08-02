package net.swedz.tesseract.neoforge.helper.guigraphics;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public interface StringGuiGraphics extends TextGuiGraphics
{
	boolean isStringDropShadow();
	
	void setStringDropShadow(boolean textDropShadow);
	
	default int drawString(String text, int x, int y)
	{
		return this.drawString(text, (float) x, (float) y);
	}
	
	default int drawString(Component text, int x, int y)
	{
		return this.drawString(text.getVisualOrderText(), x, y);
	}
	
	default int drawString(FormattedCharSequence text, int x, int y)
	{
		return this.drawString(text, (float) x, (float) y);
	}
	
	default int drawCenteredString(String text, int x, int y)
	{
		return this.drawCenteredString(text, (float) x, (float) y);
	}
	
	default int drawCenteredString(Component text, int x, int y)
	{
		return this.drawCenteredString(text, (float) x, (float) y);
	}
	
	default int drawCenteredString(FormattedCharSequence text, int x, int y)
	{
		return this.drawCenteredString(text, (float) x, (float) y);
	}
	
	default int drawString(String text, float x, float y)
	{
		return this.drawString(Component.literal(text), x, y);
	}
	
	default int drawString(Component text, float x, float y)
	{
		return this.drawString(text.getVisualOrderText(), x, y);
	}
	
	int drawString(FormattedCharSequence text, float x, float y);
	
	default int drawCenteredString(String text, float x, float y)
	{
		return this.drawCenteredString(Component.literal(text), x, y);
	}
	
	default int drawCenteredString(Component text, float x, float y)
	{
		return this.drawCenteredString(text.getVisualOrderText(), x, y);
	}
	
	default int drawCenteredString(FormattedCharSequence text, float x, float y)
	{
		x -= this.getFont().width(text) / 2f;
		y -= this.getFont().lineHeight / 2f;
		return this.drawString(text, (float) Math.round(x), (float) Math.round(y));
	}
}
