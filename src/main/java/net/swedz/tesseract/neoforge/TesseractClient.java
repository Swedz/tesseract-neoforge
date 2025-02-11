package net.swedz.tesseract.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.swedz.tesseract.neoforge.tooltip.TooltipHandler;
import net.swedz.tesseract.neoforge.tooltip.component.ItemStackClientTooltipComponent;
import net.swedz.tesseract.neoforge.tooltip.component.ItemStackTooltipComponent;

@Mod(value = Tesseract.ID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = Tesseract.ID, bus = EventBusSubscriber.Bus.MOD)
public final class TesseractClient
{
	public TesseractClient(IEventBus bus)
	{
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ItemTooltipEvent.class, (event) ->
				TooltipHandler.attach(event.getFlags(), event.getContext(), event.getItemStack(), event.getToolTip()));
	}
	
	@SubscribeEvent
	private static void registerClientTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event)
	{
		event.register(ItemStackTooltipComponent.class, ItemStackClientTooltipComponent::new);
	}
}
