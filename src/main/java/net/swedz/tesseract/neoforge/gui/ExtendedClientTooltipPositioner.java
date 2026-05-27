package net.swedz.tesseract.neoforge.gui;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.joml.Vector2i;

public interface ExtendedClientTooltipPositioner
{
	Result positionTooltip(int guiWidth, int guiHeight, int x, int y, int textWidth, int textHeight);
	
	record Result(
			int x,
			int y,
			int paddingLeft,
			int paddingTop,
			int paddingRight,
			int paddingBottom
	)
	{
	}
	
	default ClientTooltipPositioner toVanillaPositioner()
	{
		return (guiWidth, guiHeight, x, y, textWidth, textHeight) ->
		{
			var result = this.positionTooltip(guiWidth, guiHeight, x, y, textWidth, textHeight);
			return new Vector2i(result.x(), result.y());
		};
	}
}
