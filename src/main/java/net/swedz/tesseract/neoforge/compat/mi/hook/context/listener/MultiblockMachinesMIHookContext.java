package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockHolderModifier;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockPropertiesModifier;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.SpecialMachineBuilder;

import java.util.function.Predicate;

public final class MultiblockMachinesMIHookContext extends MIHookContext
{
	public MultiblockMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public SpecialMachineBuilder builder(String name, String englishName, MachineBlockEntityFactory factory)
	{
		return MachineBuilder.special(hook, name, englishName, true, factory);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name,
						 MachineBlockFactory blockCreator,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 boolean defaultMineableTags,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		HackedMachineRegistrationHelper.registerMachine(hook, englishName, name, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 boolean defaultMineableTags,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name,
						 MachineBlockFactory blockCreator,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, null, modifyBlock, overrideProperties, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, null, null, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
						 MachineBlockFactory blockCreator,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 boolean defaultMineableTags,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
		
		HackedMachineRegistrationHelper.addMachineModel(hook, name, defaultCasing, overlayFolder, frontOverlay, topOverlay, sideOverlay, hasActive);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 boolean defaultMineableTags,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
						 MachineBlockFactory blockCreator,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, modifyBlock, overrideProperties, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, null, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
						 MachineBlockFactory blockCreator,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 boolean defaultMineableTags,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, true, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 boolean defaultMineableTags,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
						 MachineBlockFactory blockCreator,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
						 MachineBlockHolderModifier modifyBlock,
						 MachineBlockPropertiesModifier overrideProperties,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, modifyBlock, overrideProperties, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String name, String overlayFolder,
						 MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
						 MachineBlockEntityFactory factory,
						 MachineBlockRegistrators... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, null, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void registerRecipeCategory(String id, String englishName, MachineRecipeType recipeType, MachineCategoryParams params)
	{
		HackedMachineRegistrationHelper.registerRecipeCategory(hook, id, englishName, recipeType, params);
	}
	
	@Deprecated(forRemoval = true)
	public void registerRecipeCategory(String id, String englishName, MachineRecipeType recipeType,
									   SlotPositions itemInputs, SlotPositions itemOutputs,
									   SlotPositions fluidInputs, SlotPositions fluidOutputs,
									   ProgressBar.Parameters progressBarParams,
									   Predicate<MachineRecipe> recipePredicate,
									   boolean isMultiblock,
									   SteamMode steamMode)
	{
		HackedMachineRegistrationHelper.registerRecipeCategory(hook, id, englishName, recipeType, itemInputs, itemOutputs, fluidInputs, fluidOutputs, progressBarParams, recipePredicate, isMultiblock, steamMode);
	}
}
