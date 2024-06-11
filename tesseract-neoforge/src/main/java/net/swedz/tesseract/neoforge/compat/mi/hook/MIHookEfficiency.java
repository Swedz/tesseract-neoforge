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
	
	/**
	 * Called when the max recipe eu is read.
	 * <br><br>
	 * This context is not cancellable.<br>
	 * List of modifiable variables in this context:
	 * <ul>
	 *     <li>setMaxRecipeEu</li>
	 * </ul>
	 *
	 * @param context the context
	 */
	default void onGetRecipeMaxEu(EfficiencyMIHookContext context)
	{
	}
	
	/**
	 * Called when the <code>decreaseEfficiencyTicks</code> method is called. This is <b>not</b> the only time efficiency ticks are decreased. This is only called when a multiblock's shape is not matched or it failed to resume its recipe when re-loaded.
	 * <br><br>
	 * This context is cancellable.<br>
	 * There are no modifiable variables in this context.
	 *
	 * @param context the context
	 */
	default void onDecreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
	}
	
	/**
	 * Called when the <code>increaseEfficiencyTicks</code> method is called. This is <b>not</b> the only time efficiency ticks are increased. This is only called when lubricant is applied to the machine.
	 * <br><br>
	 * This context is cancellable.<br>
	 * There are no modifiable variables in this context.
	 *
	 * @param context the context
	 */
	default void onIncreaseEfficiencyTicks(EfficiencyMIHookContext context)
	{
	}
	
	/**
	 * Called at the start of the crafter component's tick.
	 * <br><br>
	 * This context is not cancellable.<br>
	 * List of modifiable variables in this context:
	 * <ul>
	 *     <li>setEfficiencyTicks</li>
	 * </ul>
	 *
	 * @param context the context
	 */
	default void onTickStart(EfficiencyMIHookContext context)
	{
	}
	
	/**
	 * Called at the end of the crafter component's tick.
	 * <br><br>
	 * This context is not cancellable.<br>
	 * List of modifiable variables in this context:
	 * <ul>
	 *     <li>setEfficiencyTicks</li>
	 * </ul>
	 *
	 * @param context the context
	 * @param eu      the eu used this tick
	 */
	default void onTickEnd(EfficiencyMIHookContext context, long eu)
	{
	}
	
	/**
	 * Called after NBT data is read for a crafter component.
	 * <br><br>
	 * This context is not cancellable.<br>
	 * List of modifiable variables in this context:
	 * <ul>
	 *     <li>setEfficiencyTicks</li>
	 *     <li>setMaxRecipeEu</li>
	 * </ul>
	 *
	 * @param context the context
	 */
	default void onReadNbt(EfficiencyMIHookContext context)
	{
	}
	
	MIHookEfficiency NONE = () -> Integer.MIN_VALUE;
}
