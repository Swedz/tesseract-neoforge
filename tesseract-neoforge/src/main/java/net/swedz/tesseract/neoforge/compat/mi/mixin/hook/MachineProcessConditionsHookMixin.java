package net.swedz.tesseract.neoforge.compat.mi.mixin.hook;

import aztech.modern_industrialization.machines.recipe.condition.MachineProcessConditions;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MachineProcessConditionsMIHookContext;
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
			at = @At("RETURN")
	)
	private static void clinit(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners((hook) -> hook.machineProcessConditions(new MachineProcessConditionsMIHookContext()));
	}
}
