package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Optional;

public final class TooltipAttachment implements Comparable<TooltipAttachment>
{
	public static TooltipAttachment multilinesOptional(TooltipFunction function)
	{
		return new TooltipAttachment(function);
	}
	
	public static TooltipAttachment multilinesOptional(ItemFilter filter, TooltipFunction function)
	{
		return multilinesOptional(TooltipFunction.of(filter, function));
	}
	
	public static TooltipAttachment multilinesOptional(ItemLike itemLike, TooltipFunction function)
	{
		return multilinesOptional(ItemFilter.of(itemLike), function);
	}
	
	public static TooltipAttachment multilinesOptional(List<ResourceLocation> itemIds, TooltipFunction function)
	{
		return multilinesOptional(ItemFilter.of(itemIds), function);
	}
	
	public static <I extends Item> TooltipAttachment multilinesOptional(Class<I> itemClass, TooltipFunction<I> function)
	{
		return multilinesOptional(ItemFilter.of(itemClass), function);
	}
	
	public static TooltipAttachment multilines(ItemFilter filter, TooltipContentFunction function)
	{
		return multilinesOptional(TooltipFunction.of(filter, function));
	}
	
	public static TooltipAttachment multilines(ItemLike itemLike, TooltipContentFunction function)
	{
		return multilines(ItemFilter.of(itemLike), function);
	}
	
	public static TooltipAttachment multilines(List<ResourceLocation> itemIds, TooltipContentFunction function)
	{
		return multilines(ItemFilter.of(itemIds), function);
	}
	
	public static <I extends Item> TooltipAttachment multilines(Class<I> itemClass, TooltipContentFunction<I> function)
	{
		return multilines(ItemFilter.of(itemClass), function);
	}
	
	public static TooltipAttachment multilines(ItemFilter filter, List<Component> lines)
	{
		return multilinesOptional(TooltipFunction.of(filter, lines));
	}
	
	public static TooltipAttachment multilines(ItemLike itemLike, List<Component> lines)
	{
		return multilines(ItemFilter.of(itemLike), lines);
	}
	
	public static TooltipAttachment multilines(List<ResourceLocation> itemIds, List<Component> lines)
	{
		return multilines(ItemFilter.of(itemIds), lines);
	}
	
	public static <I extends Item> TooltipAttachment multilines(Class<I> itemClass, List<Component> lines)
	{
		return multilines(ItemFilter.of(itemClass), lines);
	}
	
	public static TooltipAttachment singleLineOptional(SingleLineTooltipFunction function)
	{
		return new TooltipAttachment(function);
	}
	
	public static TooltipAttachment singleLineOptional(ItemFilter filter, SingleLineTooltipFunction function)
	{
		return multilinesOptional(TooltipFunction.of(filter, function));
	}
	
	public static TooltipAttachment singleLineOptional(ItemLike itemLike, SingleLineTooltipFunction function)
	{
		return singleLineOptional(ItemFilter.of(itemLike), function);
	}
	
	public static TooltipAttachment singleLineOptional(List<ResourceLocation> itemIds, SingleLineTooltipFunction function)
	{
		return singleLineOptional(ItemFilter.of(itemIds), function);
	}
	
	public static <I extends Item> TooltipAttachment singleLineOptional(Class<I> itemClass, SingleLineTooltipFunction<I> function)
	{
		return singleLineOptional(ItemFilter.of(itemClass), function);
	}
	
	public static TooltipAttachment singleLine(ItemFilter filter, SingleLineTooltipContentFunction function)
	{
		return multilinesOptional(TooltipFunction.of(filter, function));
	}
	
	public static TooltipAttachment singleLine(ItemLike itemLike, SingleLineTooltipContentFunction function)
	{
		return singleLine(ItemFilter.of(itemLike), function);
	}
	
	public static TooltipAttachment singleLine(List<ResourceLocation> itemIds, SingleLineTooltipContentFunction function)
	{
		return singleLine(ItemFilter.of(itemIds), function);
	}
	
	public static <I extends Item> TooltipAttachment singleLine(Class<I> itemClass, SingleLineTooltipContentFunction<I> function)
	{
		return singleLine(ItemFilter.of(itemClass), function);
	}
	
	public static TooltipAttachment singleLine(ItemFilter filter, Component line)
	{
		return multilinesOptional(TooltipFunction.of(filter, line));
	}
	
	public static TooltipAttachment singleLine(ItemLike itemLike, Component line)
	{
		return singleLine(ItemFilter.of(itemLike), line);
	}
	
	public static TooltipAttachment singleLine(List<ResourceLocation> itemIds, Component line)
	{
		return singleLine(ItemFilter.of(itemIds), line);
	}
	
	public static <I extends Item> TooltipAttachment singleLine(Class<I> itemClass, Component line)
	{
		return singleLine(ItemFilter.of(itemClass), line);
	}
	
	private final TooltipFunction function;
	
	private boolean requiresShift = true;
	private int     priority;
	
	private TooltipAttachment(TooltipFunction function)
	{
		this.function = function;
		TooltipHandler.register(this);
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
	
	public interface ItemFilter
	{
		static ItemFilter of(ItemLike itemLike)
		{
			return (stack, item) -> item == itemLike.asItem();
		}
		
		static ItemFilter of(List<ResourceLocation> itemIds)
		{
			return (stack, item) -> itemIds.contains(BuiltInRegistries.ITEM.getKey(item));
		}
		
		static <I extends Item> ItemFilter of(Class<I> itemClass)
		{
			return (stack, item) -> itemClass.isAssignableFrom(item.getClass());
		}
		
		boolean test(ItemStack stack, Item item);
		
		default boolean test(ItemStack stack)
		{
			return this.test(stack, stack.getItem());
		}
		
		default boolean test(Item item)
		{
			return this.test(item.getDefaultInstance(), item);
		}
	}
	
	public interface TooltipFunction<I extends Item>
	{
		static TooltipFunction of(ItemFilter filter, TooltipFunction function)
		{
			return (stack, item) -> filter.test(stack, item) ? function.get(stack, item) : Optional.empty();
		}
		
		static TooltipFunction of(ItemFilter filter, TooltipContentFunction function)
		{
			return (stack, item) -> filter.test(stack, item) ? Optional.of(function.get(stack, item)) : Optional.empty();
		}
		
		static TooltipFunction of(ItemFilter filter, List<Component> lines)
		{
			return TooltipFunction.of(filter, (TooltipContentFunction) (stack, item) -> lines);
		}
		
		static TooltipFunction of(ItemFilter filter, Component line)
		{
			return TooltipFunction.of(filter, List.of(line));
		}
		
		Optional<List<? extends Component>> get(ItemStack stack, I item);
	}
	
	public interface SingleLineTooltipFunction<I extends Item> extends TooltipFunction<I>
	{
		Optional<Component> getSingleLine(ItemStack stack, I item);
		
		@Override
		default Optional<List<? extends Component>> get(ItemStack stack, I item)
		{
			return this.getSingleLine(stack, item).map(List::of);
		}
	}
	
	public interface TooltipContentFunction<I extends Item>
	{
		List<? extends Component> get(ItemStack stack, I item);
	}
	
	public interface SingleLineTooltipContentFunction<I extends Item> extends TooltipContentFunction<I>
	{
		Component getSingleLine(ItemStack stack, I item);
		
		@Override
		default List<? extends Component> get(ItemStack stack, I item)
		{
			return List.of(this.getSingleLine(stack, item));
		}
	}
}
