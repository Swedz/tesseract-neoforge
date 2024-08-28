package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.models.MachineCasing;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

import java.util.function.Consumer;
import java.util.function.Function;

public final class SingleBlockSpecialMachinesMIHookContext implements MIHookContext
{
	@SafeVarargs
	public final void register(String englishName, String name,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		HackedMachineRegistrationHelper.registerMachine(englishName, name, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, factory, extraRegistrators);
		
		HackedMachineRegistrationHelper.addMachineModel(name, defaultCasing, overlayFolder, frontOverlay, topOverlay, sideOverlay, hasActive);
	}
	
	@SafeVarargs
	public final void register(String englishName, String name, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, name, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, true, factory, extraRegistrators);
	}
}
