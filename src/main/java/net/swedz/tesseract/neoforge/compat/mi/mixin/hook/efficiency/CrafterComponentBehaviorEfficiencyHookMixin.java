package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.efficiency;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.api.machine.holder.CrafterComponentHolder;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		value = CrafterComponent.Behavior.class,
		remap = false
)
public interface CrafterComponentBehaviorEfficiencyHookMixin
{
	@Redirect(
			method = "banRecipe",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/components/CrafterComponent$Behavior;getMaxRecipeEu()J"
			)
	)
	private long getMaxRecipeEu(CrafterComponent.Behavior behavior)
	{
		long maxRecipeEu = behavior.getMaxRecipeEu();
		if(behavior instanceof MachineBlockEntity machineBlockEntity &&
				machineBlockEntity instanceof CrafterComponentHolder crafterComponentHolder)
		{
			CrafterAccess crafter = crafterComponentHolder.getCrafterComponent();
			EfficiencyMIHookContext context = new EfficiencyMIHookContext(
					machineBlockEntity, crafter.hasActiveRecipe(),
					crafter.getMaxEfficiencyTicks(), crafter.getEfficiencyTicks(), maxRecipeEu
			);
			MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onGetRecipeMaxEu);
			return context.getMaxRecipeEu();
		}
		return maxRecipeEu;
	}
}
