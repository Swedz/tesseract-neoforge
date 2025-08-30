package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.machines.models.MachineCasing;
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

public final class SingleBlockSpecialMachinesMIHookContext extends MIHookContext
{
	public SingleBlockSpecialMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public SpecialMachineBuilder builder(String name, String englishName, MachineBlockEntityFactory factory)
	{
		return MachineBuilder.special(hook, name, englishName, false, factory);
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
		this.register(englishName, name, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
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
		this.register(englishName, name, null, null, null, factory, extraRegistrators);
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
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, null, null, factory, extraRegistrators);
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
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, null, null, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	public void registerReiTiers(String englishName, String machine, MachineRecipeType recipeType, MachineCategoryParams categoryParams, int tiers)
	{
		HackedMachineRegistrationHelper.registerReiTiers(hook, englishName, machine, recipeType, categoryParams, tiers);
	}
}
