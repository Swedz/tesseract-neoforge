package net.swedz.tesseract.neoforge.compat.mi.guicomponent.recipeefficiency;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.client.machines.gui.ClientComponentRenderer;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.client.machines.gui.MachineScreen;
import aztech.modern_industrialization.client.util.RenderHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ModularRecipeEfficiencyBarClient extends GuiComponentClient<ModularRecipeEfficiencyBar.Params, ModularRecipeEfficiencyBar.Data>
{
	public ModularRecipeEfficiencyBarClient(ModularRecipeEfficiencyBar.Params params, ModularRecipeEfficiencyBar.Data data)
	{
		super(params, data);
	}
	
	@Override
	public ClientComponentRenderer createRenderer(MachineScreen machineScreen)
	{
		return (guiGraphics, leftPos, topPos) ->
		{
		};
	}
	
	private static final ResourceLocation TEXTURE = MI.id("textures/gui/efficiency_bar.png");
	private static final int              WIDTH   = 100, HEIGHT = 2;
	
	public class Renderer implements ClientComponentRenderer
	{
		@Override
		public void renderBackground(GuiGraphics guiGraphics, int x, int y)
		{
			guiGraphics.blit(TEXTURE, x + params.renderX() - 1, y + params.renderY() - 1, 0, 2, WIDTH + 2, HEIGHT + 2, 102, 6);
			if(data.hasActiveRecipe())
			{
				int barPixels = (int) ((float) data.efficiencyTicks() / data.maxEfficiencyTicks() * WIDTH);
				guiGraphics.blit(TEXTURE, x + params.renderX(), y + params.renderY(), 0, 0, barPixels, HEIGHT, 102, 6);
			}
		}
		
		@Override
		public void renderTooltip(MachineScreen screen, Font font, GuiGraphics guiGraphics, int x, int y, int cursorX, int cursorY)
		{
			if(RenderHelper.isPointWithinRectangle(params.renderX(), params.renderY(), WIDTH, HEIGHT, cursorX - x, cursorY - y))
			{
				List<Component> tooltip = new ArrayList<>();
				if(data.hasActiveRecipe())
				{
					DecimalFormat factorFormat = new DecimalFormat("#.#");
					
					tooltip.add(MIText.EfficiencyTicks.text(data.efficiencyTicks(), data.maxEfficiencyTicks()));
					tooltip.add(MIText.EfficiencyFactor.text(factorFormat.format((double) data.currentRecipeEu() / data.baseRecipeEu())));
					tooltip.add(MIText.EfficiencyEu.text(data.currentRecipeEu()));
					
				}
				else
				{
					tooltip.add(MIText.EfficiencyDefaultMessage.text());
				}
				
				tooltip.add(MIText.EfficiencyMaxOverclock.text(data.maxRecipeEu()));
				
				guiGraphics.renderTooltip(font, tooltip, Optional.empty(), cursorX, cursorY);
			}
		}
	}
}
