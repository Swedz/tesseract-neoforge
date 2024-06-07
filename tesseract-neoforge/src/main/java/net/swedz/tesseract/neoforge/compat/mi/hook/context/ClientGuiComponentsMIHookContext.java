package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import aztech.modern_industrialization.machines.GuiComponentsClient;
import aztech.modern_industrialization.machines.gui.GuiComponentClient;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookContext;

public final class ClientGuiComponentsMIHookContext implements MIHookContext
{
	public void register(ResourceLocation id, GuiComponentClient.Factory clientFactory)
	{
		GuiComponentsClient.register(id, clientFactory);
	}
}
