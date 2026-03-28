package net.swedz.tesseract.neoforge.compat.mi.api;

import net.minecraft.world.item.ItemStack;

public interface ComponentStackHolder
{
	ItemStack getStack();
	
	void setStack(ItemStack stack);
}
