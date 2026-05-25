package net.swedz.tesseract.neoforge.compat.mi.mixin;

import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.swedz.tesseract.neoforge.compat.mi.api.ActiveRecipeHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(
		value = CrafterComponent.class,
		remap = false
)
public class ActiveRecipeHolderMixin implements ActiveRecipeHolder<RecipeHolder<MachineRecipe>>
{
	@Shadow
	@Final
	private MachineProcessCondition.Context conditionContext;
	
	@Shadow
	private RecipeHolder<MachineRecipe> activeRecipe;
	
	@Override
	public boolean hasActiveRecipe()
	{
		return activeRecipe != null;
	}
	
	@Override
	public RecipeHolder<MachineRecipe> getActiveRecipe()
	{
		return activeRecipe;
	}
	
	@Override
	public long getRecipeEuCost(RecipeHolder<MachineRecipe> recipe)
	{
		return recipe.value().eu;
	}
	
	@Override
	public long getRecipeTotalEuCost(RecipeHolder<MachineRecipe> recipe)
	{
		return recipe.value().getTotalEu();
	}
	
	@Override
	public boolean doConditionsMatchForRecipe(RecipeHolder<MachineRecipe> recipe)
	{
		return recipe.value().conditionsMatch(conditionContext);
	}
}
