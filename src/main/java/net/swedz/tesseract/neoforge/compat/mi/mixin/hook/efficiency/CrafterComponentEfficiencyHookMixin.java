package net.swedz.tesseract.neoforge.compat.mi.mixin.hook.efficiency;

import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(
		value = CrafterComponent.class,
		remap = false
)
public abstract class CrafterComponentEfficiencyHookMixin
{
	@Shadow
	@Final
	private MachineProcessCondition.Context conditionContext;
	
	@Shadow
	@Final
	private CrafterComponent.Behavior behavior;
	
	@Shadow
	private int efficiencyTicks;
	
	@Shadow
	private int maxEfficiencyTicks;
	
	@Shadow
	private long recipeMaxEu;
	
	@Shadow
	public abstract boolean hasActiveRecipe();
	
	@Redirect(
			method = {"getRecipeMaxEfficiencyTicks", "tickRecipe", "getRecipeMaxEu"},
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/components/CrafterComponent$Behavior;getMaxRecipeEu()J"
			)
	)
	private long getMaxRecipeEu(CrafterComponent.Behavior behavior)
	{
		long maxRecipeEu = behavior.getMaxRecipeEu();
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				conditionContext.getBlockEntity(), this.hasActiveRecipe(),
				maxEfficiencyTicks, efficiencyTicks, maxRecipeEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onGetRecipeMaxEu);
		return context.getMaxRecipeEu();
	}
	
	@Inject(
			method = "decreaseEfficiencyTicks",
			at = @At("HEAD"),
			cancellable = true
	)
	private void decreaseEfficiencyTicks(CallbackInfo callback)
	{
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				conditionContext.getBlockEntity(), this.hasActiveRecipe(),
				maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onDecreaseEfficiencyTicks);
		if(context.isCancelled())
		{
			callback.cancel();
		}
	}
	
	@Inject(
			method = "increaseEfficiencyTicks",
			at = @At("HEAD"),
			cancellable = true
	)
	private void increaseEfficiencyTicks(int increment, CallbackInfo callback)
	{
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				conditionContext.getBlockEntity(), this.hasActiveRecipe(),
				maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onIncreaseEfficiencyTicks);
		if(context.isCancelled())
		{
			callback.cancel();
		}
	}
	
	@Inject(
			method = "tickRecipe",
			at = @At("HEAD")
	)
	private void tickRecipe(CallbackInfoReturnable<Boolean> callback)
	{
		if(!behavior.getCrafterWorld().isClientSide())
		{
			EfficiencyMIHookContext context = new EfficiencyMIHookContext(
					conditionContext.getBlockEntity(), this.hasActiveRecipe(),
					maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
			);
			MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onTickStart);
			efficiencyTicks = context.getEfficiencyTicks();
		}
	}
	
	@Inject(
			method = "tickRecipe",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/machines/components/CrafterComponent;clearActiveRecipeIfPossible()V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void tickRecipeReset(CallbackInfoReturnable<Boolean> cir, boolean isActive, boolean isEnabled, boolean recipeStarted, long eu, boolean finishedRecipe)
	{
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				conditionContext.getBlockEntity(), this.hasActiveRecipe(),
				maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, (h, c) -> h.onTickEnd(c, eu));
		efficiencyTicks = context.getEfficiencyTicks();
	}
	
	@Inject(
			method = "readNbt",
			at = @At("RETURN")
	)
	private void readNbt(CompoundTag tag, HolderLookup.Provider registries, boolean isUpgradingMachine, CallbackInfo ci)
	{
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				conditionContext.getBlockEntity(), this.hasActiveRecipe(),
				maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onReadNbt);
		efficiencyTicks = context.getEfficiencyTicks();
		recipeMaxEu = context.getMaxRecipeEu();
	}
}
