package net.swedz.tesseract.neoforge.compat.mi.machine.builder.slots;

import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.item.ItemVariant;
import net.minecraft.world.item.Item;
import net.swedz.tesseract.neoforge.compat.mi.mixin.accessor.ConfigurableStackAccessor;

import java.util.function.Supplier;

public final class ItemMachineSlotDefinition extends MachineSlotDefinition<Item, ItemVariant>
{
	public ItemMachineSlotDefinition(int x, int y,
									 boolean playerInsert, boolean pipesInsert,
									 boolean playerExtract, boolean pipesExtract,
									 Supplier<Item> lock)
	{
		super(x, y, playerInsert, pipesInsert, playerExtract, pipesExtract, lock);
	}
	
	@Override
	public ConfigurableItemStack create()
	{
		var stack = new ConfigurableItemStack();
		var access = (ConfigurableStackAccessor<Item>) stack;
		
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
