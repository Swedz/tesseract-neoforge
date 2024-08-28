package net.swedz.tesseract.neoforge.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class ItemHurtEvent extends LivingEvent implements ICancellableEvent
{
	private final ItemStack stack;
	private final int       damageAmount;
	
	public ItemHurtEvent(LivingEntity entity, ItemStack stack, int damageAmount)
	{
		super(entity);
		this.stack = stack;
		this.damageAmount = damageAmount;
	}
	
	public ItemStack getItemStack()
	{
		return stack;
	}
	
	public int getDamageAmount()
	{
		return damageAmount;
	}
}
