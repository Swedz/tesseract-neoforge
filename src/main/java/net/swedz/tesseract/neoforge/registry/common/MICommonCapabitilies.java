package net.swedz.tesseract.neoforge.registry.common;

import aztech.modern_industrialization.api.energy.EnergyApi;
import dev.technici4n.grandpower.api.ISimpleEnergyItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class MICommonCapabitilies
{
	public static <I extends Item & ISimpleEnergyItem> void simpleEnergyItem(I item, RegisterCapabilitiesEvent event)
	{
		event.registerItem(
				EnergyApi.ITEM,
				(stack, ctx) -> ISimpleEnergyItem.createStorage(
						stack,
						item.getEnergyComponent(),
						item.getEnergyCapacity(stack),
						item.getEnergyMaxInput(stack),
						item.getEnergyMaxOutput(stack)
				),
				item
		);
	}
}
