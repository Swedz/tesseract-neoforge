package net.swedz.tesseract.neoforge.compat.mi.hook;

import net.swedz.tesseract.neoforge.compat.mi.hook.context.BlastFurnaceTiersMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.ClientGuiComponentsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MachineCasingsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MachineProcessConditionsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MachineRecipeTypesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.SingleBlockCraftingMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.ViewerSetupMIHookContext;

public interface MIHook
{
	default void blastFurnaceTiers(BlastFurnaceTiersMIHookContext hook)
	{
	}
	
	default void clientGuiComponents(ClientGuiComponentsMIHookContext hook)
	{
	}
	
	default void machineCasings(MachineCasingsMIHookContext hook)
	{
	}
	
	default void machineProcessConditions(MachineProcessConditionsMIHookContext hook)
	{
	}
	
	default void machineRecipeTypes(MachineRecipeTypesMIHookContext hook)
	{
	}
	
	default void multiblockMachines(MultiblockMachinesMIHookContext hook)
	{
	}
	
	default void singleBlockCraftingMachines(SingleBlockCraftingMachinesMIHookContext hook)
	{
	}
	
	default void singleBlockSpecialMachines(SingleBlockSpecialMachinesMIHookContext hook)
	{
	}
	
	default void tooltips()
	{
	}
	
	default void viewerSetup(ViewerSetupMIHookContext hook)
	{
	}
}
