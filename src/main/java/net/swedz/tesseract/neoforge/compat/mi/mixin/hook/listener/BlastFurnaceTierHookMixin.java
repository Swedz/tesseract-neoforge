package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.listener;

import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.BlastFurnaceTiersMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(
		value = ElectricBlastFurnaceBlockEntity.class,
		remap = false
)
public class BlastFurnaceTierHookMixin
{
	@ModifyVariable(
			method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;sort(Ljava/util/Comparator;)V"
			)
	)
	private static List clinit(List value)
	{
		MIHooks.triggerHookListeners((hook) ->
		{
			BlastFurnaceTiersMIHookContext context = new BlastFurnaceTiersMIHookContext();
			hook.blastFurnaceTiers(context);
			value.addAll(context.getRegisteredTiers());
		});
		return value;
	}
}
