package net.swedz.tesseract.neoforge.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public record ItemStackClientTooltipComponent(ItemStackTooltipComponent component) implements ClientTooltipComponent
{
	@Override
	public int getHeight(Font font)
	{
		return 20;
	}
	
	@Override
	public int getWidth(Font font)
	{
		return 18;
	}
	
	@Override
	public void extractImage(Font font, int x, int y, int mouseX, int mouseY, GuiGraphicsExtractor graphics)
	{
		var stack = component.stack();
		graphics.item(stack, mouseX, mouseY);
		graphics.itemDecorations(font, stack, mouseX, mouseY, null);
	}
}
