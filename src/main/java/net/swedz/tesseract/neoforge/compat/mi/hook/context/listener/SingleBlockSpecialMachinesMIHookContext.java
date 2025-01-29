package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.models.MachineCasing;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
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
}
