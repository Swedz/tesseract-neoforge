package net.swedz.tesseract.neoforge.registry.common;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

public final class CommonCapabilities
{
	public static <Type extends Item> void bucketItem(Type item, RegisterCapabilitiesEvent event)
	{
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new FluidBucketWrapper(stack), item);
	}
}
