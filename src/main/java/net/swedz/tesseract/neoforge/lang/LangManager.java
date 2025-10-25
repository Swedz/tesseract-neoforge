package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.function.Supplier;

public final class LangManager
{
	private final String modId;
	
	private final Map<String, Supplier<Style>>        styles  = Maps.newHashMap();
	private final Map<ParserKey, Supplier<Parser<?>>> parsers = Maps.newHashMap();
	
	private record ParserKey(String key, Class<?> paramClass)
	{
	}
	
	public LangManager(String modId)
	{
		this.modId = modId;
		
		this.style(() -> Style.EMPTY);
		
		this.parser(Component.class, () -> Parser.COMPONENT);
	}
	
	String modId()
	{
		return modId;
	}
	
	public LangManager style(String key, Supplier<Style> style)
	{
		Assert.noneNull(key, style);
		styles.put(key, style);
		return this;
	}
	
	public LangManager style(Supplier<Style> style)
	{
		return this.style("default", style);
	}
	
	Supplier<Style> getStyle(String key)
	{
		Assert.notNull(key);
		return styles.get(key);
	}
	
	public <T> LangManager parser(String key, Class<T> paramClass, Supplier<Parser<T>> parser)
	{
		Assert.noneNull(key, paramClass, parser);
		parsers.put(new ParserKey(key, paramClass), parser::get);
		return this;
	}
	
	public <T> LangManager parser(Class<T> paramClass, Supplier<Parser<T>> parser)
	{
		return this.parser("default", paramClass, parser);
	}
	
	Supplier<Parser<?>> getParser(String key, Class<?> paramClass)
	{
		Assert.noneNull(key, paramClass);
		return parsers.get(new ParserKey(key, paramClass));
	}
	
	public <L> LangInstance<L> build(Class<L> langClass)
	{
		try
		{
			var handler = new LangHandler(this);
			var proxy = (L) Proxy.newProxyInstance(langClass.getClassLoader(), new Class[]{langClass}, handler);
			return new LangInstance<>(langClass, proxy, handler);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
