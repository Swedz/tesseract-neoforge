package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.efficiency;

import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.mixin.accessor.CrafterComponentAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(
		value = RecipeEfficiencyBar.Server.class,
		remap = false
)
public class RecipeEfficiencyBarEfficiencyHookMixin
{
	@Shadow
	@Final
	private CrafterComponent crafter;
	
	@Redirect(
			method = {
					"copyData()Laztech/modern_industrialization/machines/guicomponents/RecipeEfficiencyBar$Data;",
					"needsSync(Laztech/modern_industrialization/machines/guicomponents/RecipeEfficiencyBar$Data;)Z",
					"writeCurrentData"
			},
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/components/CrafterComponent$Behavior;getMaxRecipeEu()J"
			)
	)
	private long getMaxRecipeEu(CrafterComponent.Behavior behavior)
	{
		long maxRecipeEu = behavior.getMaxRecipeEu();
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				((CrafterComponentAccessor) crafter).getConditionContext().getBlockEntity(), crafter.hasActiveRecipe(),
				crafter.getMaxEfficiencyTicks(), crafter.getEfficiencyTicks(), maxRecipeEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onGetRecipeMaxEu);
		return context.getMaxRecipeEu();
	}
}
