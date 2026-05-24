package net.swedz.tesseract.neoforge.compat.mi.machine.builder.slots;

import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.fluid.FluidVariant;
import net.minecraft.world.level.material.Fluid;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.mixin.accessor.ConfigurableStackAccessor;

import java.util.function.Supplier;

public final class FluidMachineSlotDefinition extends MachineSlotDefinition<Fluid, FluidVariant>
{
	private final long capacity;
	
	public FluidMachineSlotDefinition(int x, int y,
									  boolean playerInsert, boolean pipesInsert,
									  boolean playerExtract, boolean pipesExtract,
									  Supplier<Fluid> lock, long capacity)
	{
		super(x, y, playerInsert, pipesInsert, playerExtract, pipesExtract, lock);
		Assert.that(capacity > 0, "Capacity must be > 0");
		this.capacity = capacity;
	}
	
	public long capacity()
	{
		return capacity;
	}
	
	@Override
	public ConfigurableFluidStack create()
	{
		var stack = new ConfigurableFluidStack(capacity);
		var access = (ConfigurableStackAccessor<Fluid>) stack;
		
		access.setLockedInstance(this.lock());
		access.setPlayerLockable(!this.hasLock());
		access.setPlayerLocked(this.hasLock());
		
		access.setPlayerInsert(playerInsert);
		access.setPipesInsert(pipesInsert);
		
		access.setPlayerExtract(playerExtract);
		access.setPipesExtract(pipesExtract);
		
		return stack;
	}
}
