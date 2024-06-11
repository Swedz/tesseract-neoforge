package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.efficiency;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.efficiency.EfficiencyMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		value = ElectricBlastFurnaceBlockEntity.class,
		remap = false
)
public class EBFEfficiencyHookMixin
{
	@Redirect(
			method = "banRecipe",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/blockentities/multiblocks/ElectricBlastFurnaceBlockEntity;getMaxRecipeEu()J"
			)
	)
	private long getMaxRecipeEu(ElectricBlastFurnaceBlockEntity behavior)
	{
		CrafterAccess crafter = behavior.getCrafterComponent();
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				behavior, crafter.hasActiveRecipe(),
				crafter.getMaxEfficiencyTicks(), crafter.getEfficiencyTicks(), behavior.getMaxRecipeEu()
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onGetRecipeMaxEu);
		return context.getMaxRecipeEu();
	}
}
