package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.api.energy.EnergyApi;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Collection;

public final class ChargeInventoryHelper
{
	public static int charge(ItemStack stack, int maxEu, boolean simulate)
	{
		try (var transaction = Transaction.openRoot())
		{
			var energy = stack.getCapability(EnergyApi.ITEM, ItemAccess.forStack(stack));
			int charge = energy != null ? Math.max(0, energy.insert(Math.max(0, maxEu), transaction)) : 0;
			if(!simulate)
			{
				transaction.commit();
			}
			return charge;
		}
	}
	
	public static long charge(Collection<ItemStack> items, int maxEu, boolean simulate)
	{
		int eu = 0;
		for(ItemStack stack : items)
		{
			int charged = charge(stack, Math.max(0, maxEu - eu), simulate);
			if(charged > 0)
			{
				eu += charged;
				if(eu == maxEu)
				{
					break;
				}
			}
		}
		return eu;
	}
}
