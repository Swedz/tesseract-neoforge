package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.listener;

import aztech.modern_industrialization.machines.recipe.condition.MachineProcessConditions;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineProcessConditionsMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = MachineProcessConditions.class,
		remap = false
)
public class MachineProcessConditionsHookMixin
{
	@Inject(
			method = "<clinit>",
			at = @At("TAIL")
	)
	private static void clinit(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners((hook, listener) -> listener.machineProcessConditions(new MachineProcessConditionsMIHookContext(hook)));
	}
}
