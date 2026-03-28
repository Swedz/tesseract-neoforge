package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;

public final class CommonCapabilities
{
	public static <I extends Item> void bucketItem(I item, RegisterCapabilitiesEvent event)
	{
		event.registerItem(Capabilities.Fluid.ITEM, (stack, access) -> new BucketResourceHandler(access), item);
	}
}
