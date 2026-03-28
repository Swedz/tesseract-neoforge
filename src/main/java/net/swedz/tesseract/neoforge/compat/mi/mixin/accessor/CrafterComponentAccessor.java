package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = CrafterComponent.class,
		remap = false
)
public interface CrafterComponentAccessor
{
	@Accessor("activeRecipe")
	RecipeHolder<MachineRecipe> getActiveRecipe();
	
	@Accessor("conditionContext")
	MachineProcessCondition.Context getConditionContext();
}
