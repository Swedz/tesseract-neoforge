package net.swedz.tesseract.neoforge.compat.mi.mixin.hook;

import aztech.modern_industrialization.machines.init.MultiblockMachines;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MultiblockMachinesMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = MultiblockMachines.class,
		remap = false
)
public class MultiblockMachinesHookMixin
{
	@Inject(
			method = "init",
			at = @At("TAIL")
	)
	private static void init(CallbackInfo callback)
	{
		MIHooks.triggerHooks((hook) -> hook.multiblockMachines(new MultiblockMachinesMIHookContext()));
	}
}
