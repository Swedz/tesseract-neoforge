package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.listener;

import aztech.modern_industrialization.MI;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockCraftingMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = MI.class,
		remap = false
)
public class RegisterMachinesHookMixin
{
	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void init(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners((hook, listener) ->
		{
			listener.singleBlockSpecialMachines(new SingleBlockSpecialMachinesMIHookContext(hook));
			listener.singleBlockCraftingMachines(new SingleBlockCraftingMachinesMIHookContext(hook));
			listener.multiblockMachines(new MultiblockMachinesMIHookContext(hook));
		});
	}
}
