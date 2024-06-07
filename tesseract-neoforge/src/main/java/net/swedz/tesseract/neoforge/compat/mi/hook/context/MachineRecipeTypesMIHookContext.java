package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookContext;

public final class MachineRecipeTypesMIHookContext implements MIHookContext
{
	public static MachineRecipeType create(String name)
	{
		return MIMachineRecipeTypes.create(name);
	}
}
