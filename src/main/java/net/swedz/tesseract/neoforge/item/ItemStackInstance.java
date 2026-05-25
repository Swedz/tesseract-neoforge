package net.swedz.tesseract.neoforge.item;

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
import net.swedz.tesseract.neoforge.helper.CodecHelper;

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
		return new ItemStack(item, count, components);
	}
	
	public ItemStackTemplate asTemplate()
	{
		return new ItemStackTemplate(item, count, components);
	}
}
