package net.swedz.tesseract.neoforge.compat.mi.proxy;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.proxy.Proxy;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;

@ProxyEntrypoint
public class TesseractMIProxy implements Proxy
{
	public boolean isLoaded()
	{
		return false;
	}
	
	public boolean anyShiftTooltipsAreFor(ItemStack stack, Item item)
	{
		return false;
	}
}
