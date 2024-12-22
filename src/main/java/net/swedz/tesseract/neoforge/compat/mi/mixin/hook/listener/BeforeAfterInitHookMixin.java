package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.listener;

import aztech.modern_industrialization.MI;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = MI.class,
		remap = false
)
public class BeforeAfterInitHookMixin
{
	@Inject(
			method = "<init>",
			at = @At("CTOR_HEAD")
	)
	private void beforeInit(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners(MIHookListener::beforeInit);
	}
	
	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void afterInit(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners(MIHookListener::afterInit);
	}
}
