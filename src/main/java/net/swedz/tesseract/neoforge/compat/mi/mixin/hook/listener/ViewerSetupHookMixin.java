package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.listener;

import aztech.modern_industrialization.compat.viewer.abstraction.ViewerCategory;
import aztech.modern_industrialization.compat.viewer.usage.ViewerSetup;
import com.llamalad7.mixinextras.sugar.Local;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ViewerSetupMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(
		value = ViewerSetup.class,
		remap = false
)
public class ViewerSetupHookMixin
{
	@Inject(
			method = "setup",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
					ordinal = 0
			)
	)
	private static void clinit(CallbackInfoReturnable<List<ViewerCategory<?>>> callback,
							   @Local(name = "registry") List<ViewerCategory<?>> registry)
	{
		MIHooks.triggerHookListeners((hook, listener) ->
		{
			ViewerSetupMIHookContext context = new ViewerSetupMIHookContext(hook);
			listener.viewerSetup(context);
			registry.addAll(context.getRegisteredCategories());
		});
	}
}
