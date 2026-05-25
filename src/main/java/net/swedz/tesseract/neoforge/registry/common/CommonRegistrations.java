package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.world.item.Item;

public final class CommonRegistrations
{
	public static <I extends Item> void cauldronClearDye(I item)
	{
		CauldronInteractions.WATER.put(item, CauldronInteractions::dyedItemIteration);
	}
}
