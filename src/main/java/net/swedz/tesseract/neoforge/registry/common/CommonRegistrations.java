package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;

public final class CommonRegistrations
{
	public static <I extends Item> void cauldronClearDye(I item)
	{
		CauldronInteraction.WATER.map().put(item, CauldronInteraction.DYED_ITEM);
	}
}
