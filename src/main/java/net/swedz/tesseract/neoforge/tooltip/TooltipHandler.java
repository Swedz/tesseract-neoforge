package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.TesseractText;
import net.swedz.tesseract.neoforge.compat.mi.proxy.TesseractMIProxy;
import net.swedz.tesseract.neoforge.proxy.ProxyManager;
import net.swedz.tesseract.neoforge.proxy.builtin.TesseractProxy;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public final class TooltipHandler
{
	private static final PriorityQueue<TooltipAttachment> TOOLTIPS = new PriorityQueue<>();
	
	static void register(TooltipAttachment tooltip)
	{
		TOOLTIPS.add(tooltip);
	}
	
	public static void attach(ItemStack stack, List<Component> lines)
	{
		Item item = stack.getItem();
		if(item != null)
		{
			boolean hasPrintRequiredShift = false;
			for(TooltipAttachment tooltip : TOOLTIPS)
			{
				Optional<List<? extends Component>> maybeComponents = tooltip.lines(stack);
				if(!tooltip.requiresShift() || ProxyManager.get(TesseractProxy.class).hasShiftDown())
				{
					maybeComponents.ifPresent(lines::addAll);
				}
				else if(tooltip.requiresShift() && !hasPrintRequiredShift && maybeComponents.isPresent())
				{
					hasPrintRequiredShift = true;
					if(ProxyManager.get(TesseractMIProxy.class).anyShiftTooltipsAreFor(stack, item))
					{
						continue;
					}
					lines.add(TesseractText.TOOLTIPS_SHIFT_REQUIRED.text().setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xa9a9a9)).withItalic(false)));
				}
			}
		}
	}
}
