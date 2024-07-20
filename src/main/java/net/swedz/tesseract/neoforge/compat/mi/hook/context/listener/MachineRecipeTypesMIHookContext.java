package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

public final class MachineRecipeTypesMIHookContext implements MIHookContext
{
	public MachineRecipeType create(String name)
	{
		return MIMachineRecipeTypes.create(name);
	}
}
