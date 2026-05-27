package net.swedz.tesseract.neoforge.item;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.helper.CodecHelper;

import java.util.List;
import java.util.stream.Stream;

public record ItemInstance(
		Holder<Item> item,
		int count,
		DataComponentPatch components
) implements ItemLike
{
	public static final Codec<ItemInstance> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(
					CodecHelper.forRegistryHolder(BuiltInRegistries.ITEM).fieldOf("item").forGetter(ItemInstance::item),
					Codec.INT.fieldOf("count").forGetter(ItemInstance::count),
					DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemInstance::components)
			)
			.apply(instance, ItemInstance::new));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemInstance> STREAM_CODEC = StreamCodec.composite(
			CodecHelper.forRegistryHolderStream(BuiltInRegistries.ITEM),
			ItemInstance::item,
			ByteBufCodecs.VAR_INT,
			ItemInstance::count,
			DataComponentPatch.STREAM_CODEC,
			ItemInstance::components,
			ItemInstance::new
	);
	
	public static final ItemInstance EMPTY = new ItemInstance(ItemStack.EMPTY);
	
	public ItemInstance(ItemStack stack)
	{
		this(stack.getItemHolder(), stack.getCount(), stack.getComponentsPatch());
	}
	
	@Override
	public Item asItem()
	{
		return item.value();
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
