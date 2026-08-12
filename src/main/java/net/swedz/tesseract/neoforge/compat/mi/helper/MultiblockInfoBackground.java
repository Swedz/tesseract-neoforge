package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.MI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGui;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine;
import net.swedz.tesseract.neoforge.helper.ComponentHelper;

import java.util.List;

public final class MultiblockInfoBackground
{
	private static final ResourceLocation TEXTURE        = MI.id("textures/gui/container/multiblock_info.png");
	private static final int              TEXTURE_WIDTH  = ModularMultiblockGui.WIDTH;
	private static final int              TEXTURE_HEIGHT = ModularMultiblockGui.HEIGHT;
	
	public static void renderBackground(GuiGraphics graphics, int x, int y, int height)
	{
		graphics.blit(
				TEXTURE,
				x + ModularMultiblockGui.X,
				y + ModularMultiblockGui.Y,
				0,
				0,
				TEXTURE_WIDTH,
				2,
				TEXTURE_WIDTH,
				TEXTURE_HEIGHT
		);
		
		int remainingContentHeight = height - 4;
		int maxSectionHeight = TEXTURE_HEIGHT - 4;
		int offsetY = 0;
		while(remainingContentHeight > 0)
		{
			int sectionHeight = Math.min(remainingContentHeight, maxSectionHeight);
			graphics.blit(
					TEXTURE,
					x + ModularMultiblockGui.X,
					y + ModularMultiblockGui.Y + offsetY + 2,
					0,
					2,
					TEXTURE_WIDTH,
					sectionHeight,
					TEXTURE_WIDTH,
					TEXTURE_HEIGHT
			);
			offsetY += sectionHeight;
			remainingContentHeight -= sectionHeight;
		}
		
		graphics.blit(
				TEXTURE,
				x + ModularMultiblockGui.X,
				y + ModularMultiblockGui.Y + height - 2,
				0,
				TEXTURE_HEIGHT - 2,
				TEXTURE_WIDTH,
				2,
				TEXTURE_WIDTH,
				TEXTURE_HEIGHT
		);
	}
	
	public static void renderText(GuiGraphics graphics, int x, int y, List<ModularMultiblockGuiLine> lines)
	{
		var font = Minecraft.getInstance().font;
		
		int spaceWidth = font.width(" ");
		int offsetY = 21;
		for(var line : lines)
		{
			List<FormattedCharSequence> wrappedLines = line.wrap() ?
					font.split(line.text(), TEXTURE_WIDTH - 10) :
					List.of(line.text().getVisualOrderText());
			int index = 0;
			for(var wrappedLine : wrappedLines)
			{
				graphics.drawString(
						font,
						ComponentHelper.stripStyle(wrappedLine),
						x + ModularMultiblockGui.X + 5 + (index > 0 ? spaceWidth : 0),
						y + offsetY,
						line.color(),
						false
				);
				offsetY += 11;
				index++;
			}
		}
	}
}
