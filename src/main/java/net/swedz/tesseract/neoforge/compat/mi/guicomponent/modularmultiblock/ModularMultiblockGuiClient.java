package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.client.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.client.machines.gui.MachineScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Unit;
import net.swedz.tesseract.neoforge.helper.ComponentHelper;

import java.util.List;

public final class ModularMultiblockGuiClient extends GuiComponentClient<Unit, ModularMultiblockGui.Data>
{
	public ModularMultiblockGuiClient(Unit params, ModularMultiblockGui.Data data)
	{
		super(params, data);
	}
	
	@Override
	public ClientComponentRenderer createRenderer(MachineScreen machineScreen)
	{
		return new Renderer();
	}
	
	public final class Renderer implements ClientComponentRenderer
	{
		private static final Identifier TEXTURE = MI.id("textures/gui/container/multiblock_info.png");
		
		private static final int TEXTURE_WIDTH  = ModularMultiblockGui.WIDTH;
		private static final int TEXTURE_HEIGHT = ModularMultiblockGui.HEIGHT;
		
		private void renderInfoBackground(GuiGraphics graphics, int x, int y)
		{
			graphics.blit(
					TEXTURE,
					x + ModularMultiblockGui.X,
					y + ModularMultiblockGui.Y + data.y(),
					0,
					0,
					TEXTURE_WIDTH,
					2,
					TEXTURE_WIDTH,
					TEXTURE_HEIGHT
			);
			
			int remainingContentHeight = data.height() - 4;
			int maxSectionHeight = TEXTURE_HEIGHT - 4;
			int offsetY = 0;
			while(remainingContentHeight > 0)
			{
				int sectionHeight = Math.min(remainingContentHeight, maxSectionHeight);
				graphics.blit(
						TEXTURE,
						x + ModularMultiblockGui.X,
						y + ModularMultiblockGui.Y + data.y() + offsetY + 2,
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
					y + ModularMultiblockGui.Y + data.y() + data.height() - 2,
					0,
					TEXTURE_HEIGHT - 2,
					TEXTURE_WIDTH,
					2,
					TEXTURE_WIDTH,
					TEXTURE_HEIGHT
			);
		}
		
		private void renderInfoText(GuiGraphics graphics, int x, int y)
		{
			Minecraft minecraftClient = Minecraft.getInstance();
			Font font = minecraftClient.font;
			
			int spaceWidth = font.width(" ");
			int offsetY = 23;
			for(var line : data.content().lines())
			{
				List<FormattedCharSequence> wrappedLines = line.wrap() ?
						font.split(line.text(), TEXTURE_WIDTH - spaceWidth) :
						List.of(line.text().getVisualOrderText());
				int index = 0;
				for(FormattedCharSequence wrappedLine : wrappedLines)
				{
					graphics.drawString(
							font, ComponentHelper.stripStyle(wrappedLine),
							x + ModularMultiblockGui.X + 5 + (index > 0 ? spaceWidth : 0),
							y + data.y() + offsetY,
							line.color(),
							false
					);
					offsetY += 11;
					index++;
				}
			}
		}
		
		@Override
		public void renderBackground(GuiGraphics graphics, int x, int y)
		{
			this.renderInfoBackground(graphics, x, y);
			this.renderInfoText(graphics, x, y);
		}
	}
}
