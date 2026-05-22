package net.swedz.tesseract.neoforge.item;

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
import net.swedz.tesseract.neoforge.helper.CodecHelper;

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
		return new ItemStack(item, count, components);
	}
}
