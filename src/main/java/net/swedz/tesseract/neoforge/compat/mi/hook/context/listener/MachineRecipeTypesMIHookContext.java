package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

import java.util.function.Function;

public final class MachineRecipeTypesMIHookContext implements MIHookContext
{
	public MachineRecipeType create(String name)
	{
		return this.create(name, MachineRecipeType::new);
	}
	
	public MachineRecipeType create(String name, Function<ResourceLocation, MachineRecipeType> creator)
	{
		return HackedMachineRegistrationHelper.createMachineRecipeType(name, creator);
	}
}
