package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.MachineBlockCreator;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;

import java.util.function.Consumer;
import java.util.function.Function;

public final class SingleBlockSpecialMachinesMIHookContext extends MIHookContext
{
	public SingleBlockSpecialMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name,
							   MachineBlockCreator blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		HackedMachineRegistrationHelper.registerMachine(hook, englishName, name, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
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
	
	@SafeVarargs
	public final void register(String englishName, String name,
							   MachineBlockCreator blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, null, modifyBlock, overrideProperties, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, null, null, null, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   MachineBlockCreator blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
		
		HackedMachineRegistrationHelper.addMachineModel(hook, name, defaultCasing, overlayFolder, frontOverlay, topOverlay, sideOverlay, hasActive);
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
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   MachineBlockCreator blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
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
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive, null, null, null, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   MachineBlockCreator blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   boolean defaultMineableTags,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, true, blockCreator, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
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
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, modifyBlock, overrideProperties, defaultMineableTags, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   MachineBlockCreator blockCreator,
							   Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
							   Consumer<BlockBehaviour.Properties> overrideProperties,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
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
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, null, null, null, factory, extraRegistrators);
	}
	
	public void registerReiTiers(String englishName, String machine, MachineRecipeType recipeType, MachineCategoryParams categoryParams, int tiers)
	{
		HackedMachineRegistrationHelper.registerReiTiers(hook, englishName, machine, recipeType, categoryParams, tiers);
	}
}
