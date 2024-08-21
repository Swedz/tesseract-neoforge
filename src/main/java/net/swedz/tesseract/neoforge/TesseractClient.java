package net.swedz.tesseract.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.swedz.tesseract.neoforge.tooltips.TooltipHandler;

@Mod(
		value = Tesseract.ID,
		dist = Dist.CLIENT
)
public final class TesseractClient
{
	public TesseractClient(IEventBus bus)
	{
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ItemTooltipEvent.class, (event) ->
				TooltipHandler.attach(event.getItemStack(), event.getToolTip()));
	}
}
