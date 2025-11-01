package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.SingleBlockCraftingMachineBuilder;

public final class SingleBlockCraftingMachinesMIHookContext extends MIHookContext
{
	public SingleBlockCraftingMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public SingleBlockCraftingMachineBuilder builder(String name, String englishName, MachineRecipeType recipeType)
	{
		return MachineBuilder.singleBlockCrafting(hook, name, englishName, recipeType);
	}
}
