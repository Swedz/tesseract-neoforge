package net.swedz.tesseract.neoforge.registry.common;

import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.items.ItemEnergyHandler;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.swedz.tesseract.neoforge.item.SimpleEnergyItem;

public final class MICommonCapabitilies
{
	public static <I extends Item & SimpleEnergyItem> void simpleEnergyItem(I item, RegisterCapabilitiesEvent event)
	{
		event.registerItem(
				EnergyApi.ITEM,
				(stack, access) -> new ItemEnergyHandler(
						access,
						item.getEnergyComponent(),
						Integer.MAX_VALUE
				)
				{
					@Override
					protected long getCapacity(ItemResource resource)
					{
						return item.getEnergyCapacity(resource.toStack());
					}
				},
				item
		);
	}
}
