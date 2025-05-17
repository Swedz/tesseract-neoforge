package net.swedz.tesseract.neoforge.event;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class PlayerInventoryChangeEvent extends PlayerEvent
{
	private final Inventory inventory;
	private final ItemStack stack;
	
	public PlayerInventoryChangeEvent(Player player, Inventory inventory, ItemStack stack)
	{
		super(player);
		this.inventory = inventory;
		this.stack = stack;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public ItemStack getStack()
	{
		return stack;
	}
}
