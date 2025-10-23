package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.tooltip.TextLine;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.function.Function;

public final class LangManager
{
	private final String modId;
	
	private final Map<Class<?>, TextLineReference<?>> supportedTextLines = Maps.newHashMap();
	
	public LangManager(String modId)
	{
		this.modId = modId;
		
		supportedTextLines.put(TextLineReference.DEFAULT.textLineClass(), TextLineReference.DEFAULT);
	}
	
	String modId()
	{
		return modId;
	}
	
	public <T extends TextLine> LangManager withTextLines(Class<T> textLineClass,
														  Function<String, T> textLineFactory)
	{
		supportedTextLines.put(textLineClass, new TextLineReference<>(textLineClass, textLineFactory));
		return this;
	}
	
	private TextLineReference<?> getTextLine(Class<?> textLineClass)
	{
		return supportedTextLines.get(textLineClass);
	}
	
	boolean isValidTextLine(Class<?> textLineClass)
	{
		return this.getTextLine(textLineClass) != null;
	}
	
	TextLine createTextLine(Class<?> type, String key)
	{
		var reference = this.getTextLine(type);
		Assert.notNull(reference, "Unsupported LangKey method return type %s".formatted(type), IllegalStateException::new);
		return reference.textLineFactory().apply(key);
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
