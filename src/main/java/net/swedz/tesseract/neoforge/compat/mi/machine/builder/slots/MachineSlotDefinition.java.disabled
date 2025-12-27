package net.swedz.tesseract.neoforge.compat.mi.machine.builder.slots;

import aztech.modern_industrialization.inventory.AbstractConfigurableStack;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.storage.TransferVariant;

import java.util.function.Supplier;

public abstract class MachineSlotDefinition<T, K extends TransferVariant<T>>
{
	protected final int x, y;
	
	protected final boolean playerInsert, pipesInsert;
	protected final boolean playerExtract, pipesExtract;
	
	protected final Supplier<T> lock;
	
	public MachineSlotDefinition(int x, int y,
								 boolean playerInsert, boolean pipesInsert,
								 boolean playerExtract, boolean pipesExtract,
								 Supplier<T> lock)
	{
		this.x = x;
		this.y = y;
		this.playerInsert = playerInsert;
		this.pipesInsert = pipesInsert;
		this.playerExtract = playerExtract;
		this.pipesExtract = pipesExtract;
		this.lock = lock;
	}
	
	public int x()
	{
		return x;
	}
	
	public int y()
	{
		return y;
	}
	
	public boolean playerInsert()
	{
		return playerInsert;
	}
	
	public boolean pipesInsert()
	{
		return pipesInsert;
	}
	
	public boolean playerExtract()
	{
		return playerExtract;
	}
	
	public boolean pipesExtract()
	{
		return pipesExtract;
	}
	
	public T lock()
	{
		return this.hasLock() ? lock.get() : null;
	}
	
	public boolean hasLock()
	{
		return lock != null;
	}
	
	public abstract AbstractConfigurableStack<T, K> create();
}
