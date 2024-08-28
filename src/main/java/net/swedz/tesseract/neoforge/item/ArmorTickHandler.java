package net.swedz.tesseract.neoforge.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ArmorTickHandler
{
	/**
	 * This method is called every tick for every living entity and every armor slot.
	 * <br><br>
	 * Triggered by {@link net.neoforged.neoforge.event.tick.EntityTickEvent.Pre}. This method is called on both the server and the client.
	 *
	 * @param entity the entity being ticked
	 * @param slot the slot being ticked
	 * @param stack the itemstack being ticked
	 */
	void armorTick(LivingEntity entity, EquipmentSlot slot, ItemStack stack);
}
