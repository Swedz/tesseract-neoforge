package net.swedz.tesseract.neoforge.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

public interface SimpleEnergyItem
{
	DataComponentType<Long> getEnergyComponent();
	
	long getEnergyCapacity(ItemStack stack);
	
	default long getStoredEnergy(ItemStack stack)
	{
		return stack.get(this.getEnergyComponent());
	}
}
