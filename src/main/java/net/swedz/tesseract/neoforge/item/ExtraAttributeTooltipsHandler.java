package net.swedz.tesseract.neoforge.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface ExtraAttributeTooltipsHandler
{
	/**
	 * This method is called immediately before when the game builds the tooltips for the attributes on an item.
	 * <br><br>
	 * Triggered by {@link ItemStack#addAttributeTooltips(Consumer, Player)}. Called on both client and server.
	 *
	 * @param stack        the stack
	 * @param slotGroup    the slot group
	 * @param tooltipAdder the consumer to call to add lines to the tooltip
	 */
	default void appendAttributeTooltipsPre(ItemStack stack, EquipmentSlotGroup slotGroup, Consumer<Component> tooltipAdder)
	{
	}
	
	/**
	 * This method is called immediately after when the game builds the tooltips for the attributes on an item.
	 * <br><br>
	 * Triggered by {@link ItemStack#addAttributeTooltips(Consumer, Player)}. Called on both client and server.
	 *
	 * @param stack        the stack
	 * @param slotGroup    the slot group
	 * @param tooltipAdder the consumer to call to add lines to the tooltip
	 */
	default void appendAttributeTooltipsPost(ItemStack stack, EquipmentSlotGroup slotGroup, Consumer<Component> tooltipAdder)
	{
	}
}
