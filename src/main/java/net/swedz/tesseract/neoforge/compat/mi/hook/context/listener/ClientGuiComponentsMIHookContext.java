package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.client.machines.GuiComponentsClient;
import aztech.modern_industrialization.client.machines.gui.GuiComponentClient;
import aztech.modern_industrialization.machines.gui.GuiComponentServer;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

import java.util.function.BiFunction;

public final class ClientGuiComponentsMIHookContext extends MIHookContext
{
	public ClientGuiComponentsMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public <P, D> void register(GuiComponentServer.Type<P, D> type, BiFunction<P, D, GuiComponentClient<P, D>> clientFactory)
	{
		hook.enqueue(() -> GuiComponentsClient.register(type, clientFactory));
	}
}
