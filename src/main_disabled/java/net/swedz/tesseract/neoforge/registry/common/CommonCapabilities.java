package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

public final class CommonCapabilities
{
	public static <I extends Item> void bucketItem(I item, RegisterCapabilitiesEvent event)
	{
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new FluidBucketWrapper(stack), item);
	}
}
