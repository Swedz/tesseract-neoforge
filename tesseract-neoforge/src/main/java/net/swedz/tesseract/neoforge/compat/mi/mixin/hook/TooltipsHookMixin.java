package net.swedz.tesseract.neoforge.compat.mi.mixin.hook;

import aztech.modern_industrialization.MITooltips;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookListener;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = MITooltips.class,
		remap = false
)
public class TooltipsHookMixin
{
	@Inject(
			method = "<clinit>",
			at = @At("TAIL")
	)
	private static void clinit(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners(MIHookListener::tooltips);
	}
}
