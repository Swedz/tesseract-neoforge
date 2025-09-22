package net.swedz.tesseract.neoforge.compat.mi;

import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.HatchMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineCasingsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineProcessConditionsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineRecipeTypesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockCraftingMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;

public final class TesseractMI
{
	public static void init(String modId)
	{
		MIHooks.triggerHookListeners(modId, (hook, listener) ->
		{
			listener.machineProcessConditions(new MachineProcessConditionsMIHookContext(hook));
			
			listener.machineRecipeTypes(new MachineRecipeTypesMIHookContext(hook));
			
			listener.machineCasings(new MachineCasingsMIHookContext(hook));
			
			listener.hatches(new HatchMIHookContext(hook));
			listener.singleBlockSpecialMachines(new SingleBlockSpecialMachinesMIHookContext(hook));
			listener.singleBlockCraftingMachines(new SingleBlockCraftingMachinesMIHookContext(hook));
			listener.multiblockMachines(new MultiblockMachinesMIHookContext(hook));
		});
	}
}
