package net.swedz.tesseract.neoforge.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.swedz.tesseract.api.Transcoder;

public final class MojangCodecTranscoder<D, E> implements Transcoder<D, E>
{
	private final Codec<D>      codec;
	private final DynamicOps<E> ops;
	
	public MojangCodecTranscoder(Codec<D> codec, DynamicOps<E> ops)
	{
		this.codec = codec;
		this.ops = ops;
	}
	
	@Override
	public D decode(E encoded)
	{
		return codec.parse(ops, encoded)
				.getOrThrow((error) -> new IllegalArgumentException("Could not decode %s: %s".formatted(encoded, error)));
	}
	
	@Override
	public E encode(D value)
	{
		return codec.encodeStart(ops, value)
				.getOrThrow((error) -> new IllegalArgumentException("Could not encode %s: %s".formatted(value, error)));
	}
}
