package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityWithGuiFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineGuiConfigurator;

public final class SpecialMachineBuilder extends MachineWithGuiBuilder<SpecialMachineBuilder>
{
	private final boolean isMultiblock;
	
	private final MachineBlockEntityFactory blockEntityFactory;
	
	SpecialMachineBuilder(MIHook hook,
						  String name, String englishName,
						  boolean isMultiblock,
						  MachineBlockEntityWithGuiFactory blockEntityFactory)
	{
		super(hook, name, englishName);
		Assert.notNull(blockEntityFactory);
		this.isMultiblock = isMultiblock;
		this.blockEntityFactory = (bep) -> blockEntityFactory.create(bep, gui);
	}
	
	public SpecialMachineBuilder gui(SteamMode steamMode, MachineRecipeType recipeType,
									 MachineGuiConfigurator builder)
	{
		return this.gui(isMultiblock, steamMode, recipeType, builder);
	}
	
	public SpecialMachineBuilder gui(MachineGuiConfigurator builder)
	{
		return this.gui(null, null, builder);
	}
	
	@Override
	protected void internalBuild()
	{
		HackedMachineRegistrationHelper.registerMachine(
				hook,
				englishName, name,
				blockFactory, holderModifier, propertiesModifier,
				defaultMineableTags,
				blockEntityFactory,
				registrators.toArray(MachineBlockRegistrators[]::new)
		);
		if(builtinModel != null)
		{
			builtinModel.build(hook, name);
		}
		if(gui != null && gui.hasRecipeCategory())
		{
			gui.registerRecipeCategory(hook, name, englishName);
		}
	}
}
