package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
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
	
	private boolean isRecipeCategoryRegistered;
	
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
	protected void internalRegister()
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
	}
	
	public SpecialMachineBuilder registerMultiblockShape(ShapeTemplate shape)
	{
		Assert.that(isMultiblock, "Multiblock shapes can only be registered on multiblock machines");
		Assert.notNull(shape);
		ReiMachineRecipes.registerMultiblockShape(hook.id(name), shape);
		return this;
	}
	
	public SpecialMachineBuilder registerRecipeCategory()
	{
		Assert.that(!isRecipeCategoryRegistered, "This recipe category is already registered");
		isRecipeCategoryRegistered = true;
		if(gui != null && gui.hasRecipeCategory())
		{
			gui.registerRecipeCategory(hook, name, englishName);
		}
		return this;
	}
}
