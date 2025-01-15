package net.swedz.tesseract.neoforge.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public record ItemStackClientTooltipComponent(ItemStackTooltipComponent component) implements ClientTooltipComponent
{
	@Override
	public int getHeight()
	{
		return 20;
	}
	
	@Override
	public int getWidth(Font font)
	{
		return 18;
	}
	
	@Override
	public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics graphics)
	{
		var stack = component.stack();
		graphics.renderItem(stack, mouseX, mouseY);
		graphics.renderItemDecorations(font, stack, mouseX, mouseY, null);
	}
}
