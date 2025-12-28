package net.swedz.tesseract.neoforge.compat.mi.guicomponent.configurationpanel;

import aztech.modern_industrialization.client.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.client.machines.gui.MachineScreen;
import aztech.modern_industrialization.util.Rectangle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.compat.mi.network.packet.UpdateMachineConfigurationPanelPacket;

import java.util.List;

/**
 * This was stolen from {@link aztech.modern_industrialization.machines.guicomponents.ShapeSelectionClient} to make my own generic "configuration panel" component to be used for non-shape related configuring of machines.
 */
public final class ConfigurationPanelClient extends GuiComponentClient<ConfigurationPanel.Params, ConfigurationPanel.Data>
{
	private Renderer renderer;
	
	public ConfigurationPanelClient(ConfigurationPanel.Params params, ConfigurationPanel.Data data)
	{
		super(params, data);
	}
	
	@Override
	public ClientComponentRenderer createRenderer(MachineScreen machineScreen)
	{
		// Compute the max width of all the components!
		int maxWidth = 1;
		for(ConfigurationPanel.LineInfo line : params.lines())
		{
			for(Component tooltip : line.translations())
			{
				maxWidth = Math.max(maxWidth, Minecraft.getInstance().font.width(tooltip));
			}
		}
		
		return renderer = new Renderer(maxWidth);
	}
	
	Renderer getRenderer()
	{
		return renderer;
	}
	
	final class Renderer implements ClientComponentRenderer
	{
		boolean isPanelOpen = false;
		private final int btnSize      = 12;
		private final int borderSize   = 3;
		private final int outerPadding = 5;
		private final int innerPadding = 5;
		
		private final int textMaxWidth;
		private final int panelWidth;
		
		private Renderer(int textMaxWidth)
		{
			this.textMaxWidth = textMaxWidth;
			this.panelWidth = borderSize + outerPadding + btnSize + innerPadding + textMaxWidth + innerPadding + btnSize + outerPadding;
		}
		
		private static int getVerticalPos(int lineId)
		{
			return 46 + 16 * lineId;
		}
		
		@Override
		public void addButtons(ButtonContainer container)
		{
			// Two buttons per line
			for(int i = 0; i < params.lines().size(); ++i)
			{
				int iCopy = i;
				var line = params.lines().get(i);
				int baseU = line.useArrows() ? 174 : 150;
				int v = 58;
				
				// Left button
				container.addButton(
						-panelWidth + borderSize + outerPadding, getVerticalPos(i), btnSize, btnSize,
						(syncId) -> new UpdateMachineConfigurationPanelPacket(syncId, iCopy, true).sendToServer(),
						List::of,
						(screen, button, guiGraphics, mouseX, mouseY, delta) ->
						{
							if(data.selectedIndexes().get(iCopy) == 0)
							{
								screen.blitButtonNoHighlight(button, guiGraphics, baseU, v + 12);
							}
							else
							{
								screen.blitButtonSmall(button, guiGraphics, baseU, v);
							}
						},
						() -> isPanelOpen
				);
				
				// Right button
				container.addButton(
						-btnSize - outerPadding, getVerticalPos(i), btnSize, btnSize,
						(syncId) -> new UpdateMachineConfigurationPanelPacket(syncId, iCopy, false).sendToServer(),
						List::of,
						(screen, button, guiGraphics, mouseX, mouseY, delta) ->
						{
							if(data.selectedIndexes().get(iCopy) == line.numValues() - 1)
							{
								screen.blitButtonNoHighlight(button, guiGraphics, baseU + 12, v + 12);
							}
							else
							{
								screen.blitButtonSmall(button, guiGraphics, baseU + 12, v);
							}
						},
						() -> isPanelOpen
				);
			}
			
			// Big button to open panel
			container.addButton(
					-24, 17, 20, 20,
					(syncId) -> isPanelOpen = !isPanelOpen,
					() -> List.of(params.title(), params.description()),
					(screen, button, guiGraphics, mouseX, mouseY, delta) -> screen.blitButton(button, guiGraphics, 138, 38)
			);
		}
		
		@Override
		public void renderBackground(GuiGraphics guiGraphics, int leftPos, int topPos)
		{
			Rectangle box = this.getBox(leftPos, topPos);
			
			guiGraphics.blit(MachineScreen.BACKGROUND, box.x(), box.y(), 0, 0, box.w(), box.h() - 4);
			guiGraphics.blit(MachineScreen.BACKGROUND, box.x(), box.y() + box.h() - 4, 0, 252, box.w(), 4);
			
			if(isPanelOpen)
			{
				RenderSystem.disableDepthTest();
				for(int index = 0; index < params.lines().size(); ++index)
				{
					var line = params.lines().get(index);
					Component tooltip = line.translations().get(data.selectedIndexes().get(index));
					int width = Minecraft.getInstance().font.width(tooltip);
					guiGraphics.drawString(
							Minecraft.getInstance().font, tooltip,
							box.x() + borderSize + outerPadding + btnSize + innerPadding + (textMaxWidth - width) / 2,
							topPos + getVerticalPos(index) + 2, 0x404040, false
					);
				}
				RenderSystem.enableDepthTest();
			}
		}
		
		public Rectangle getBox(int leftPos, int topPos)
		{
			if(isPanelOpen)
			{
				int topOffset = 10;
				return new Rectangle(leftPos - panelWidth, topPos + topOffset, panelWidth,
						getVerticalPos(params.lines().size() - 1) - topOffset + btnSize + outerPadding + borderSize
				);
			}
			else
			{
				return new Rectangle(leftPos - 31, topPos + 10, 31, 34);
			}
		}
		
		@Override
		public void addExtraBoxes(List<Rectangle> rectangles, int leftPos, int topPos)
		{
			rectangles.add(this.getBox(leftPos, topPos));
		}
	}
}
