package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MultiblockMachinesMIHookContext extends MIHookContext
{
	public MultiblockMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		HackedMachineRegistrationHelper.registerMachine(hook, englishName, name, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, null, null, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
		
		HackedMachineRegistrationHelper.addMachineModel(hook, name, defaultCasing, overlayFolder, frontOverlay, topOverlay, sideOverlay, hasActive);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, null, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, true, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, null, factory, extraRegistrators);
	}
	
	public void registerRecipeCategory(String id, String englishName, MachineRecipeType recipeType, MachineCategoryParams params)
	{
		HackedMachineRegistrationHelper.registerRecipeCategory(hook, id, englishName, recipeType, params);
	}
	
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
