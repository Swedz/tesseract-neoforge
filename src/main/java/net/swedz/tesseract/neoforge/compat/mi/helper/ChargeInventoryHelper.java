package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.api.energy.EnergyApi;
import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public final class ChargeInventoryHelper
{
	public static long charge(ItemStack stack, long maxEu, boolean simulate)
	{
		ILongEnergyStorage energy = stack.getCapability(EnergyApi.ITEM);
		return energy != null ? energy.receive(Math.max(0, maxEu), simulate) : 0;
	}
	
	public static long charge(Collection<ItemStack> items, long maxEu, boolean simulate)
	{
		long eu = 0;
		for(ItemStack stack : items)
		{
			eu += charge(stack, Math.max(0, maxEu - eu), simulate);
			if(eu == maxEu)
			{
				break;
			}
		}
		return eu;
	}
}
