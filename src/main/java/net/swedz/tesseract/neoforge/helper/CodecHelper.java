package net.swedz.tesseract.neoforge.helper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class CodecHelper
{
	public static <R> Codec<R> forRegistry(Registry<R> registry)
	{
		return registry.byNameCodec();
	}
	
	public static <R> StreamCodec<ByteBuf, R> forRegistryStream(Registry<R> registry)
	{
		return ResourceLocation.STREAM_CODEC.map(registry::get, registry::getKey);
	}
	
	public static <E extends Enum<E>> Codec<E> forEnum(Class<E> enumClass, Supplier<E[]> values, boolean uppercase)
	{
		return Codec.STRING.flatXmap(
				(value) ->
				{
					for(E entry : values.get())
					{
						if((uppercase ?
								entry.toString().toUpperCase(Locale.ROOT).equals(value) :
								entry.toString().toLowerCase(Locale.ROOT).equals(value)))
						{
							return DataResult.success(entry);
						}
					}
					return DataResult.error(() -> "No value exists for %s".formatted(value));
				},
				(entry) -> DataResult.success(uppercase ? entry.toString().toUpperCase(Locale.ROOT) : entry.toString().toLowerCase(Locale.ROOT))
		);
	}
	
	public static <E extends Enum<E>> Codec<E> forEnum(Class<E> enumClass, Supplier<E[]> values)
	{
		return forEnum(enumClass, values, true);
	}
	
	public static <E extends Enum<E>> Codec<E> forEnum(Class<E> enumClass)
	{
		return forEnum(enumClass, enumClass::getEnumConstants);
	}
	
	public static <E extends Enum<E>> Codec<E> forLowercaseEnum(Class<E> enumClass, Supplier<E[]> values)
	{
		return forEnum(enumClass, values, false);
	}
	
	public static <E extends Enum<E>> Codec<E> forLowercaseEnum(Class<E> enumClass)
	{
		return forLowercaseEnum(enumClass, enumClass::getEnumConstants);
	}
	
	public static <E extends Enum<E>> StreamCodec<ByteBuf, E> forEnumStream(Class<E> enumClass, Supplier<E[]> values, boolean uppercase)
	{
		return ByteBufCodecs.STRING_UTF8.map(
				(value) ->
				{
					for(E entry : values.get())
					{
						if((uppercase ?
								entry.toString().toUpperCase(Locale.ROOT).equals(value) :
								entry.toString().toLowerCase(Locale.ROOT).equals(value)))
						{
							return entry;
						}
					}
					throw new NoSuchElementException("No value exists for %s".formatted(value));
				},
				(entry) -> uppercase ? entry.toString().toUpperCase(Locale.ROOT) : entry.toString().toLowerCase(Locale.ROOT)
		);
	}
	
	public static <E extends Enum<E>> StreamCodec<ByteBuf, E> forEnumStream(Class<E> enumClass, Supplier<E[]> values)
	{
		return forEnumStream(enumClass, values, true);
	}
	
	public static <E extends Enum<E>> StreamCodec<ByteBuf, E> forEnumStream(Class<E> enumClass)
	{
		return forEnumStream(enumClass, enumClass::getEnumConstants);
	}
	
	public static <E extends Enum<E>> StreamCodec<ByteBuf, E> forLowercaseEnumStream(Class<E> enumClass, Supplier<E[]> values)
	{
		return forEnumStream(enumClass, values, false);
	}
	
	public static <E extends Enum<E>> StreamCodec<ByteBuf, E> forLowercaseEnumStream(Class<E> enumClass)
	{
		return forLowercaseEnumStream(enumClass, enumClass::getEnumConstants);
	}
}
