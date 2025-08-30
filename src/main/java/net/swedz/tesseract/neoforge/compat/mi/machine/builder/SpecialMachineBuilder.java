package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;

import java.util.function.Consumer;

public final class SpecialMachineBuilder extends MachineBuilder<SpecialMachineBuilder>
{
	private final boolean isMultiblock;
	
	private final MachineBlockEntityFactory blockEntityFactory;
	
	private MachineRecipeCategoryBuilder recipeCategory;
	
	SpecialMachineBuilder(MIHook hook,
						  String name, String englishName,
						  boolean isMultiblock,
						  MachineBlockEntityFactory blockEntityFactory)
	{
		super(hook, name, englishName);
		Assert.notNull(blockEntityFactory);
		this.isMultiblock = isMultiblock;
		this.blockEntityFactory = blockEntityFactory;
	}
	
	public SpecialMachineBuilder recipeCategory(SteamMode steamMode, MachineRecipeType recipeType,
												Consumer<MachineRecipeCategoryBuilder> builder)
	{
		Assert.noneNull(steamMode, recipeType, builder);
		recipeCategory = new MachineRecipeCategoryBuilder(isMultiblock, steamMode, recipeType);
		builder.accept(recipeCategory);
		return this;
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
		if(recipeCategory != null)
		{
			recipeCategory.build(hook, name, englishName);
		}
	}
}
