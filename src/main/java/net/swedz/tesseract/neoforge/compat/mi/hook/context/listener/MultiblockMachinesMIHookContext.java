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
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.SpecialMachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityWithGuiFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MultiblockMachinesMIHookContext extends MIHookContext
{
	public MultiblockMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public SpecialMachineBuilder builder(String name, String englishName, MachineBlockEntityWithGuiFactory factory)
	{
		return MachineBuilder.special(hook, name, englishName, true, factory);
	}
	
	public SpecialMachineBuilder builder(String name, String englishName, MachineBlockEntityFactory factory)
	{
		return this.builder(name, englishName, (bep, gui) -> factory.create(bep));
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name,
							   MachineBlockFactory blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		List<MachineBlockRegistrators> wrappedRegistrators = Lists.newArrayList();
		for(var registrator : extraRegistrators)
		{
			wrappedRegistrators.add(registrator::accept);
		}
		HackedMachineRegistrationHelper.registerMachine(hook, englishName, name, blockCreator, modifyBlock != null ? modifyBlock::accept : null, overrideProperties != null ? overrideProperties::accept : null, defaultMineableTags, factory::apply, wrappedRegistrators.toArray(MachineBlockRegistrators[]::new));
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name,
							   MachineBlockFactory blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, null, modifyBlock, overrideProperties, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, null, null, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   MachineBlockFactory blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
		
		HackedMachineRegistrationHelper.addMachineModel(hook, name, defaultCasing, overlayFolder, frontOverlay, topOverlay, sideOverlay, hasActive);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   MachineBlockFactory blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, modifyBlock, overrideProperties, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, null, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   MachineBlockFactory blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, true, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   MachineBlockFactory blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, modifyBlock, overrideProperties, factory, extraRegistrators);
	}
	
	@Deprecated(forRemoval = true)
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
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
