package net.swedz.tesseract.neoforge.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ArmorUnequippedHandler
{
	/**
	 * This method is called whenever an armor slot for this item is changed into a different stack.
	 * <br><br>
	 * Triggered by {@link net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent}. Only called on the server.
	 *
	 * @param entity the entity
	 * @param slot the slot
	 * @param fromStack the original stack (this item)
	 * @param toStack the new stack (potentially some other item)
	 */
	void onUnequipArmor(LivingEntity entity, EquipmentSlot slot, ItemStack fromStack, ItemStack toStack);
}
