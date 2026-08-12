package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import aztech.modern_industrialization.client.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.client.machines.gui.MachineScreen;
import aztech.modern_industrialization.client.util.RenderHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Unit;
import net.swedz.tesseract.neoforge.compat.mi.helper.MultiblockInfoBackground;

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
		@Override
		public void renderBackground(GuiGraphics graphics, int x, int y)
		{
			MultiblockInfoBackground.renderBackground(graphics, x, y + data.y(), data.height());
			MultiblockInfoBackground.renderText(graphics, x, y + data.y(), data.content().lines());
		}
		
		@Override
		public boolean renderTooltip(MachineScreen screen, Font font, GuiGraphics graphics, int x, int y, int mouseX, int mouseY)
		{
			if(RenderHelper.isPointWithinRectangle(ModularMultiblockGui.X, ModularMultiblockGui.Y, ModularMultiblockGui.WIDTH, ModularMultiblockGui.HEIGHT, mouseX - x, mouseY - y))
			{
				var tooltip = data.tooltip();
				if(!tooltip.isEmpty())
				{
					graphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
					return true;
				}
			}
			return false;
		}
	}
}
