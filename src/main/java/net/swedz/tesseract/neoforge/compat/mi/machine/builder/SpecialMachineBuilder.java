package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityWithGuiFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineGuiConfigurator;

import java.util.function.Consumer;

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
	
	@Override
	public SpecialMachineBuilder builtinModel(MachineCasing casing, String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		return super.builtinModel(casing, overlayFolder, (b) ->
		{
			// Machines by default have a front and active overlay
			b.front().active();
			if(builder != null)
			{
				builder.accept(b);
			}
		});
	}
	
	public SpecialMachineBuilder builtinModel(String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		return this.builtinModel(null, overlayFolder, builder);
	}
	
	public SpecialMachineBuilder builtinModel(String overlayFolder)
	{
		return this.builtinModel(overlayFolder, null);
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
				blockFactory,
				(holder) -> holderModifiers.forEach((modifier) -> modifier.modify(holder)),
				(properties) ->
				{
					if(defaultBlockProperties)
					{
						HackedMachineRegistrationHelper.applyDefaultMachineProperties(properties);
					}
					propertiesModifiers.forEach((modifier) -> modifier.modify(properties));
				},
				defaultMineableTags,
				blockEntityFactory,
				registrators.toArray(MachineBlockRegistrators[]::new)
		);
		if(builtinModel != null)
		{
			builtinModel.build(hook, name);
		}
	}
	
	public SpecialMachineBuilder registerMultiblockShape(ShapeTemplate shape, String alternative)
	{
		Assert.that(isMultiblock, "Multiblock shapes can only be registered on multiblock machines");
		Assert.notNull(shape);
		ReiMachineRecipes.registerMultiblockShape(hook.id(name), shape, alternative);
		return this;
	}
	
	public SpecialMachineBuilder registerMultiblockShape(ShapeTemplate shape)
	{
		return this.registerMultiblockShape(shape, null);
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
	
	public SpecialMachineBuilder registerExtraWorkstations(ResourceLocation... workstations)
	{
		Assert.notNull(workstations);
		if(gui != null && gui.hasRecipeCategory())
		{
			for(var workstation : workstations)
			{
				Assert.notNull(workstation);
				ReiMachineRecipes.registerWorkstation(hook.id(name), workstation);
			}
		}
		return this;
	}
	
	public SpecialMachineBuilder registerAsWorkstationFor(ResourceLocation otherMachineId)
	{
		Assert.notNull(otherMachineId);
		ReiMachineRecipes.registerWorkstation(otherMachineId, hook.id(name));
		return this;
	}
}
