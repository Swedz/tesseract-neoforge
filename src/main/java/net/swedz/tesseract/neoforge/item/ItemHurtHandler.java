package net.swedz.tesseract.neoforge.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface ItemHurtHandler
{
	/**
	 * This method is called whenever the game tries to damage the item, even if the item is not damageable.
	 * <br><br>
	 * Triggered by {@link ItemStack#hurtAndBreak(int, ServerLevel, LivingEntity, Consumer)}. Only called on the server.
	 *
	 * @param entity the entity with the item
	 * @param stack the stack
	 * @param damageAmount the amount of damage applied
	 */
	void onHurt(LivingEntity entity, ItemStack stack, int damageAmount);
}
