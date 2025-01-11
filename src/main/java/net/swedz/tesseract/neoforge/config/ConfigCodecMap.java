package net.swedz.tesseract.neoforge.config;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.swedz.tesseract.neoforge.api.Assert;

import java.util.Map;

public final class ConfigCodecMap
{
	private final Map<Class<?>, Codec<?>> codecs = Maps.newHashMap();
	
	public <T> ConfigCodecMap register(Class<T> type, Codec<T> codec)
	{
		Assert.noneNull(type, codec);
		
		codecs.put(type, codec);
		
		return this;
	}
	
	public <T> boolean has(Class<T> type)
	{
		Assert.notNull(type);
		
		return codecs.containsKey(type);
	}
	
	public <T> Codec<T> get(Class<T> type)
	{
		Assert.notNull(type);
		Assert.that(this.has(type), "No codec registered for type %s".formatted(type.getName()));
		
		return (Codec<T>) codecs.get(type);
	}
}
