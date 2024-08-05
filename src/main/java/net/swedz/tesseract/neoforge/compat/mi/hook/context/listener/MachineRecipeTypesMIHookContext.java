package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.mixin.accessor.MIMachineRecipeTypesAccessor;

import java.util.function.Function;

public final class MachineRecipeTypesMIHookContext implements MIHookContext
{
	public MachineRecipeType create(String name)
	{
		return MIMachineRecipeTypes.create(name);
	}
	
	public MachineRecipeType create(String name, Function<ResourceLocation, MachineRecipeType> creator)
	{
		return MIMachineRecipeTypesAccessor.create(name, creator);
	}
}
