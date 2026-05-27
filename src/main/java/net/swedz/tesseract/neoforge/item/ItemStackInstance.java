package net.swedz.tesseract.neoforge.item;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.helper.CodecHelper;

import java.util.List;
import java.util.stream.Stream;

public record ItemStackInstance(
		Holder<Item> item,
		int count,
		DataComponentPatch components
) implements ItemLike, ItemInstance
{
	public static final Codec<ItemStackInstance> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(
					CodecHelper.forRegistryHolder(BuiltInRegistries.ITEM).fieldOf("item").forGetter(ItemStackInstance::item),
					Codec.INT.fieldOf("count").forGetter(ItemStackInstance::count),
					DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStackInstance::components)
			)
			.apply(instance, ItemStackInstance::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackInstance> STREAM_CODEC = StreamCodec.composite(
			CodecHelper.forRegistryHolderStream(BuiltInRegistries.ITEM),
			ItemStackInstance::item,
			ByteBufCodecs.VAR_INT,
			ItemStackInstance::count,
			DataComponentPatch.STREAM_CODEC,
			ItemStackInstance::components,
			ItemStackInstance::new
	);
	
	public static final ItemStackInstance EMPTY = new ItemStackInstance(ItemStack.EMPTY);
	
	public ItemStackInstance(ItemStack stack)
	{
		this(stack.typeHolder(), stack.getCount(), stack.getComponentsPatch());
	}
	
	@Override
	public Item asItem()
	{
		return item.value();
	}
	
	@Override
	public Holder<Item> typeHolder()
	{
		return item;
	}
	
	@Override
	public <T> T get(DataComponentType<? extends T> type)
	{
		return components.get(item.components(), type);
	}
	
	public ItemStack asStack()
	{
		if(count <= 0)
		{
			return ItemStack.EMPTY;
		}
		Assert.that(count <= 99, "Count must be less than 100 to create an ItemStack");
		return new ItemStack(item, count, components);
	}
	
	public ItemStackTemplate asTemplate()
	{
		Assert.that(count >= 1 && count <= 99, "Count must be within bounds 1-99 to create an ItemStackTemplate");
		return new ItemStackTemplate(item, count, components);
	}
	
	public Stream<ItemStack> asStacks()
	{
		List<ItemStack> stacks = Lists.newArrayList();
		int remaining = count;
		while(remaining > 0)
		{
			int stackCount = Math.min(64, remaining);
			var stack = new ItemStack(item, stackCount);
			if(!components.isEmpty())
			{
				stack.applyComponents(components);
			}
			stacks.add(stack);
			remaining -= stackCount;
		}
		return stacks.stream();
	}
}
