package net.swedz.tesseract.neoforge.compat.mi.hook;

import net.swedz.tesseract.neoforge.compat.mi.hook.context.efficiency.EfficiencyMIHookContext;

public interface MIHookEfficiency
{
	/**
	 * Gets the priority for the efficiency hook. Higher priorities are run first, and lower priorities are run last.
	 *
	 * @return the priority of the hook
	 */
	int getPriority();
	
	/**
	 * Checks whether this hook should always run. It is recommended to leave this as default unless you know what you are doing. Changing this can yield unexpected or unintended results with other hooks.
	 * <br><br>
	 * When <code>false</code> (default), the hook will not run if a higher priority hook has modified the hook context for that call.
	 * <br>
	 * When <code>true</code>, the hook will always run regardless of any other hooks.
	 *
	 * @return whether this hook should always run
	 */
	default boolean shouldAlwaysRun()
	{
		return false;
	}
	
	default void onGetRecipeMaxEu(EfficiencyMIHookContext context)
	{
	}
	
	default void onDecreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
	}
	
	default void onIncreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
	}
	
	default void onTickStart(EfficiencyMIHookContext context)
	{
	}
	
	default void onResetRecipe(EfficiencyMIHookContext context)
	{
	}
	
	default void onReadNbt(EfficiencyMIHookContext context)
	{
	}
	
	MIHookEfficiency NONE = () -> Integer.MIN_VALUE;
}
