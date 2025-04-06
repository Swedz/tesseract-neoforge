package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = CrafterComponent.class,
		remap = false
)
public interface CrafterComponentAccessor
{
	@Accessor("conditionContext")
	MachineProcessCondition.Context getConditionContext();
}
