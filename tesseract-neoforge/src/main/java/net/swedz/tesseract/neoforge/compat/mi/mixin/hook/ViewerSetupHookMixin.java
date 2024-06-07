package net.swedz.tesseract.neoforge.compat.mi.mixin.hook;

import aztech.modern_industrialization.compat.viewer.abstraction.ViewerCategory;
import aztech.modern_industrialization.compat.viewer.usage.ViewerSetup;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.ViewerSetupMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(
		value = ViewerSetup.class,
		remap = false
)
public class ViewerSetupHookMixin
{
	@Inject(
			method = "setup",
			at = @At("RETURN"),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private static void clinit(CallbackInfoReturnable<List<ViewerCategory<?>>> callback, List<ViewerCategory<?>> registry)
	{
		MIHooks.triggerHooks((hook) ->
		{
			ViewerSetupMIHookContext context = new ViewerSetupMIHookContext();
			hook.viewerSetup(context);
			registry.addAll(context.getRegisteredCategories());
		});
	}
}
