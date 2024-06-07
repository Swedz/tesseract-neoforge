package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.init.MachineRegistrationHelper;
import aztech.modern_industrialization.machines.models.MachineCasing;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookContext;

import java.util.function.Consumer;
import java.util.function.Function;

public final class SingleBlockSpecialMachinesMIHookContext implements MIHookContext
{
	@SafeVarargs
	public final void register(String englishName, String id,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		MachineRegistrationHelper.registerMachine(englishName, id, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void register(String englishName, String id, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, boolean hasActive,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, id, factory, extraRegistrators);
		MachineRegistrationHelper.addMachineModel(id, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, hasActive);
	}
	
	@SafeVarargs
	public final void register(String englishName, String id, String overlayFolder,
							   MachineCasing defaultCasing, boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
							   Function<BEP, MachineBlockEntity> factory,
							   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.register(englishName, id, overlayFolder, defaultCasing, frontOverlay, topOverlay, sideOverlay, true, factory, extraRegistrators);
	}
}
