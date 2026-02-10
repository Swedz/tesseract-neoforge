package net.swedz.tesseract.neoforge.config;

import com.mojang.serialization.Codec;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.config.ConfigTranscoderMap;
import net.swedz.tesseract.neoforge.api.MojangCodecTranscoder;
import net.swedz.tesseract.neoforge.serialization.TomlOps;

public final class ModConfigCodecMap extends ConfigTranscoderMap<Object>
{
	public <T> ModConfigCodecMap register(Class<T> type, Codec<T> codec)
	{
		Assert.noneNull(type, codec);
		
		this.register(type, new MojangCodecTranscoder<>(codec, TomlOps.INSTANCE));
		return this;
	}
}
