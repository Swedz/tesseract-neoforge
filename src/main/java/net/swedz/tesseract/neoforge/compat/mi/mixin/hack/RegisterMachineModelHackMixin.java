package net.swedz.tesseract.neoforge.compat.mi.mixin.hack;

import aztech.modern_industrialization.datagen.model.MachineModelsToGenerate;
import aztech.modern_industrialization.machines.models.MachineCasing;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = MachineModelsToGenerate.class,
		remap = false
)
public class RegisterMachineModelHackMixin
{
	@Inject(
			method = "register",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void register(String id, MachineCasing defaultCasing, String overlay,
								 boolean front, boolean top, boolean side, boolean active,
								 CallbackInfo callback)
	{
		if(MIHookTracker.isTracking())
		{
			MIHookTracker.addMachineModel(MIHookTracker.id(id), defaultCasing, overlay, front, top, side, active);
			callback.cancel();
		}
	}
}
