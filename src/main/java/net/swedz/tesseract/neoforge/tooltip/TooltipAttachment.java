package net.swedz.tesseract.neoforge.tooltip;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
	
	//<editor-fold desc="Helper methods">
	private static ItemTest toItemTest(ItemLike itemLike)
	{
		return (stack, item) -> item == itemLike.asItem();
	}
	
	private static ItemTest toItemTest(List<ResourceLocation> itemIds)
	{
		return (stack, item) ->
		{
			ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
			return itemIds.stream().anyMatch((itemId) -> itemId.equals(id));
		};
	}
	
	private static <I extends Item> ItemTest toItemTest(Class<I> itemClass)
	{
		return (stack, item) -> itemClass.isAssignableFrom(item.getClass());
	}
	
	public static <C extends Component> TooltipAttachment of(ItemTest itemTest, C line)
	{
		return new TooltipAttachment((stack, item) -> itemTest.test(stack, item) ? Optional.of(List.of(line)) : Optional.empty());
	}
	
	public static <C extends Component> TooltipAttachment of(ItemLike itemLike, C line)
	{
		return of(toItemTest(itemLike), line);
	}
	
	public static <C extends Component> TooltipAttachment of(List<ResourceLocation> itemIds, C line)
	{
		return of(toItemTest(itemIds), line);
	}
	
	public static <I extends Item, C extends Component> TooltipAttachment of(Class<I> itemClass, Function<I, C> line)
	{
		return of((stack, item) -> toItemTest(itemClass).test(stack, item) ? Optional.of(line.apply((I) item)) : Optional.empty());
	}
	
	public static TooltipAttachment of(ItemTest itemTest, TranslatableTextEnum text, Style style)
	{
		return of(itemTest, new TextLine(text, style));
	}
	
	public static TooltipAttachment of(ItemLike itemLike, TranslatableTextEnum text, Style style)
	{
		return of(itemLike, new TextLine(text, style));
	}
	
	public static TooltipAttachment of(List<ResourceLocation> itemIds, TranslatableTextEnum text, Style style)
	{
		return of(itemIds, new TextLine(text, style));
	}
	
	public static <I extends Item> TooltipAttachment of(Class<I> itemClass, TranslatableTextEnum text, Style style)
	{
		return of(itemClass, (__) -> new TextLine(text, style));
	}
	
	public static TooltipAttachment of(SingleLineTooltipFunction function)
	{
		return new TooltipAttachment(function);
	}
	
	public static <C extends Component> TooltipAttachment ofMultilines(ItemTest itemTest, List<C> lines)
	{
		return new TooltipAttachment((stack, item) -> itemTest.test(item) ? Optional.of(lines) : Optional.empty());
	}
	
	public static <C extends Component> TooltipAttachment ofMultilines(ItemLike itemLike, List<C> lines)
	{
		return ofMultilines(toItemTest(itemLike), lines);
	}
	
	public static <C extends Component> TooltipAttachment ofMultilines(List<ResourceLocation> itemIds, List<C> lines)
	{
		return ofMultilines(toItemTest(itemIds), lines);
	}
	
	public static <I extends Item, C extends Component> TooltipAttachment ofMultilines(Class<I> itemClass, Function<I, List<C>> lines)
	{
		return ofMultilines((stack, item) -> toItemTest(itemClass).test(stack, item) ? Optional.of(lines.apply((I) item)) : Optional.empty());
	}
	
	private static List<TextLine> toTextLines(Style style, TranslatableTextEnum... lines)
	{
		return Arrays.stream(lines).map((line) -> new TextLine(line, style)).toList();
	}
	
	public static TooltipAttachment ofMultilines(ItemTest itemTest, Style style, TranslatableTextEnum... lines)
	{
		return ofMultilines(itemTest, toTextLines(style, lines));
	}
	
	public static TooltipAttachment ofMultilines(ItemLike itemLike, Style style, TranslatableTextEnum... lines)
	{
		return ofMultilines(itemLike, toTextLines(style, lines));
	}
	
	public static TooltipAttachment ofMultilines(List<ResourceLocation> itemIds, Style style, TranslatableTextEnum... lines)
	{
		return ofMultilines(itemIds, toTextLines(style, lines));
	}
	
	public static <I extends Item> TooltipAttachment ofMultilines(Class<I> itemClass, Style style, TranslatableTextEnum... lines)
	{
		return ofMultilines(itemClass, (__) -> toTextLines(style, lines));
	}
	
	public static TooltipAttachment ofMultilines(TooltipFunction function)
	{
		return new TooltipAttachment(function);
	}
	//</editor-fold>
	
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
	
	public interface ItemTest
	{
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
