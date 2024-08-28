package net.swedz.tesseract.neoforge.compat.mi.hook.builtin;

import net.swedz.tesseract.neoforge.compat.mi.TesseractMITooltips;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.configurationpanel.ConfigurationPanel;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.configurationpanel.ConfigurationPanelClient;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGui;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiClient;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.recipeefficiency.ModularRecipeEfficiencyBar;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.recipeefficiency.ModularRecipeEfficiencyBarClient;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEntrypoint;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ClientGuiComponentsMIHookContext;

@MIHookEntrypoint
public final class TesseractMIHookListener implements MIHookListener
{
	@Override
	public void clientGuiComponents(ClientGuiComponentsMIHookContext hook)
	{
		hook.register(ConfigurationPanel.ID, ConfigurationPanelClient::new);
		hook.register(ModularMultiblockGui.ID, ModularMultiblockGuiClient::new);
		hook.register(ModularRecipeEfficiencyBar.ID, ModularRecipeEfficiencyBarClient::new);
	}
	
	@Override
	public void tooltips()
	{
		TesseractMITooltips.init();
	}
}
