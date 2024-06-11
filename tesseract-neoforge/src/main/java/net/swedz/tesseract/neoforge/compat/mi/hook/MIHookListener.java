package net.swedz.tesseract.neoforge.compat.mi.hook;

import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.BlastFurnaceTiersMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ClientGuiComponentsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineCasingsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineProcessConditionsMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MachineRecipeTypesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.MultiblockMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockCraftingMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.SingleBlockSpecialMachinesMIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.listener.ViewerSetupMIHookContext;

public interface MIHookListener
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
	
	MIHookListener NONE = new MIHookListener() {};
}
