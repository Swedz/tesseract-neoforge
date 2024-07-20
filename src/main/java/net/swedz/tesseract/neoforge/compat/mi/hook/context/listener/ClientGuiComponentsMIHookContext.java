package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.GuiComponentsClient;
import aztech.modern_industrialization.machines.gui.GuiComponentClient;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

public final class ClientGuiComponentsMIHookContext implements MIHookContext
{
	public void register(ResourceLocation id, GuiComponentClient.Factory clientFactory)
	{
		GuiComponentsClient.register(id, clientFactory);
	}
}
