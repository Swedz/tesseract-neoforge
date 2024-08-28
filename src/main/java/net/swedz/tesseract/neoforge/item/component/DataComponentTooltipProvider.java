package net.swedz.tesseract.neoforge.item.component;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

public interface DataComponentTooltipProvider
{
	/**
	 * This method is called in {@link net.minecraft.world.item.ItemStack#getTooltipLines(Item.TooltipContext, Player, TooltipFlag)} just before the <code>LORE</code> component tooltip is added.
	 *
	 * @param context the tooltip context
	 * @param tooltip the tooltip adder function
	 * @param flag the tooltip flag
	 */
	void addToTooltip(Item.TooltipContext context, TooltipAdder tooltip, TooltipFlag flag);
}
