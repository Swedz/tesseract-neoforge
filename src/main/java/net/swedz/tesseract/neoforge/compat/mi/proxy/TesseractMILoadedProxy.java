package net.swedz.tesseract.neoforge.compat.mi.proxy;

import aztech.modern_industrialization.MITooltips;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.network.TesseractMIPackets;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;
import net.swedz.tesseract.neoforge.proxy.ProxyEnvironment;

@ProxyEntrypoint(environment = ProxyEnvironment.MOD, modid = "modern_industrialization")
public class TesseractMILoadedProxy extends TesseractMIProxy
{
	@Override
	public void init()
	{
		IEventBus bus = ModList.get().getModContainerById(Tesseract.ID).orElseThrow().getEventBus();
		
		bus.addListener(RegisterPayloadHandlersEvent.class, TesseractMIPackets::init);
	}
	
	@Override
	public boolean isLoaded()
	{
		return true;
	}
	
	@Override
	public boolean anyShiftTooltipsAreFor(ItemStack stack, Item item)
	{
		return MITooltips.TOOLTIPS.stream().anyMatch((attachment) -> attachment.requiresShift && attachment.tooltipLines.apply(stack, item).isPresent());
	}
}
