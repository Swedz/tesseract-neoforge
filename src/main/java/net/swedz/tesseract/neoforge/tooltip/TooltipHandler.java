package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.proxy.TesseractMIProxy;
import net.swedz.tesseract.neoforge.proxy.Proxies;
import net.swedz.tesseract.neoforge.proxy.builtin.TesseractProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class TooltipHandler
{
	private static ConcurrentLinkedQueue<TooltipAttachment> TOOLTIPS = new ConcurrentLinkedQueue<>();
	
	static void register(TooltipAttachment tooltip)
	{
		List<TooltipAttachment> sorted = new ArrayList<>(TOOLTIPS);
		sorted.add(tooltip);
		Collections.sort(sorted);
		TOOLTIPS = new ConcurrentLinkedQueue<>(sorted);
	}
	
	public static void attach(TooltipFlag flags, Item.TooltipContext context, ItemStack stack, List<Component> lines)
	{
		var item = stack.getItem();
		boolean hasPrintRequiredShift = false;
		for(var tooltip : TOOLTIPS)
		{
			Optional<List<? extends Component>> maybeComponents = tooltip.lines(flags, context, stack);
			if(!tooltip.requiresShift() || Proxies.get(TesseractProxy.class).hasShiftDown())
			{
				maybeComponents.ifPresent(lines::addAll);
			}
			else if(tooltip.requiresShift() && !hasPrintRequiredShift && maybeComponents.isPresent())
			{
				hasPrintRequiredShift = true;
				if(Proxies.get(TesseractMIProxy.class).anyShiftTooltipsAreFor(context, stack, item))
				{
					continue;
				}
				lines.add(Tesseract.text().tooltipsShiftRequired());
			}
		}
	}
}
