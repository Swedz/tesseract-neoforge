package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineGuiConfigurator;

public abstract class MachineWithGuiBuilder<T extends MachineWithGuiBuilder<T>> extends MachineBuilder<T>
{
	protected MachineGuiConfiguration gui;
	
	MachineWithGuiBuilder(MIHook hook, String name, String englishName)
	{
		super(hook, name, englishName);
	}
	
	protected T gui(boolean isMultiblock, SteamMode steamMode, MachineRecipeType recipeType,
					MachineGuiConfigurator builder)
	{
		Assert.notNull(builder);
		gui = new MachineGuiConfiguration(isMultiblock, steamMode, recipeType);
		builder.configure(gui);
		return (T) this;
	}
}
