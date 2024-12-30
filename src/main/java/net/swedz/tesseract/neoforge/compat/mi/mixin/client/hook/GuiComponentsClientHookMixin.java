package net.swedz.tesseract.neoforge.compat.mi.mixin.client.hook;

import aztech.modern_industrialization.machines.GuiComponentsClient;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ClientGuiComponentsMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = GuiComponentsClient.class,
		remap = false
)
public class GuiComponentsClientHookMixin
{
	@Inject(
			method = "<clinit>",
			at = @At("TAIL"),
			remap = false
	)
	private static void cinit(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners((hook, listener) -> listener.clientGuiComponents(new ClientGuiComponentsMIHookContext(hook)));
	}
}
