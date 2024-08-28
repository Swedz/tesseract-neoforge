package net.swedz.tesseract.neoforge.item;

import net.minecraft.world.item.DyeColor;

public interface DynamicDyedItem
{
	int getDyeColor(DyeColor dyeColor);
	
	int getDefaultDyeColor();
}
