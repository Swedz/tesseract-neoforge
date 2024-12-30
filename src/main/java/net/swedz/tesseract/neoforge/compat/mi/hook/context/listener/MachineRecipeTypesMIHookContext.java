package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

import java.util.function.Function;

public final class MachineRecipeTypesMIHookContext extends MIHookContext
{
	public MachineRecipeTypesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public MachineRecipeType create(String name)
	{
		return this.create(name, MachineRecipeType::new);
	}
	
	public MachineRecipeType create(String name, Function<ResourceLocation, MachineRecipeType> creator)
	{
		return HackedMachineRegistrationHelper.createMachineRecipeType(hook, name, creator);
	}
}
