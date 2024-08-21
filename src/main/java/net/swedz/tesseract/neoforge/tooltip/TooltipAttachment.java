package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class TooltipAttachment implements Comparable<TooltipAttachment>
{
	private final TooltipFunction function;
	
	private boolean requiresShift = true;
	private int     priority;
	
	private TooltipAttachment(TooltipFunction function)
	{
		this.function = function;
		TooltipHandler.register(this);
	}
	
	public static <C extends Component> TooltipAttachment of(ItemLike itemLike, C line)
	{
		return new TooltipAttachment((stack, item) -> item == itemLike.asItem() ? Optional.of(List.of(line)) : Optional.empty());
	}
	
	public static TooltipAttachment of(ItemLike itemLike, TranslatableTextEnum text, Style style)
	{
		return of(itemLike, new TextLine(text, style));
	}
	
	public static TooltipAttachment of(SingleLineTooltipFunction function)
	{
		return new TooltipAttachment(function);
	}
	
	public static <C extends Component> TooltipAttachment ofMultilines(ItemLike itemLike, List<C> lines)
	{
		return new TooltipAttachment((stack, item) -> item == itemLike.asItem() ? Optional.of(lines) : Optional.empty());
	}
	
	public static TooltipAttachment ofMultilines(ItemLike itemLike, Style style, TranslatableTextEnum... lines)
	{
		return ofMultilines(itemLike, Arrays.stream(lines).map((line) -> new TextLine(line, style)).toList());
	}
	
	public static TooltipAttachment ofMultilines(TooltipFunction function)
	{
		return new TooltipAttachment(function);
	}
	
	public Optional<List<? extends Component>> lines(ItemStack stack)
	{
		return function.get(stack, stack.getItem());
	}
	
	public boolean requiresShift()
	{
		return requiresShift;
	}
	
	public TooltipAttachment noShiftRequired()
	{
		this.requiresShift = false;
		return this;
	}
	
	public int priority()
	{
		return priority;
	}
	
	public TooltipAttachment priority(int priority)
	{
		this.priority = priority;
		return this;
	}
	
	@Override
	public int compareTo(TooltipAttachment other)
	{
		return -Integer.compare(priority, other.priority());
	}
	
	public interface TooltipFunction
	{
		Optional<List<? extends Component>> get(ItemStack stack, Item item);
	}
	
	public interface SingleLineTooltipFunction extends TooltipFunction
	{
		Optional<? extends Component> getSingle(ItemStack stack, Item item);
		
		default Optional<List<? extends Component>> get(ItemStack stack, Item item)
		{
			return this.getSingle(stack, item).map(List::of);
		}
	}
}
