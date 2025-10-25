package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;
import net.swedz.tesseract.neoforge.lang.annotation.LangKeyPattern;
import net.swedz.tesseract.neoforge.lang.annotation.Parsed;
import net.swedz.tesseract.neoforge.lang.annotation.WithStyle;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedParserException;
import net.swedz.tesseract.neoforge.lang.exception.UndefinedStyleException;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;

public final class LangHandler implements InvocationHandler
{
	private static final Pattern METHOD_PATTERN = Pattern.compile("([A-Z][a-z]+)|([a-z]+)|([0-9]+)|([A-Z]+(?![a-z]))");
	
	private final LangManager manager;
	
	private Map<String, LangEntry> values = Map.of();
	
	public LangHandler(LangManager manager)
	{
		this.manager = manager;
	}
	
	public Collection<LangEntry> entries()
	{
		return values.values().stream()
				.sorted(Comparator.comparing(LangEntry::key))
				.toList();
	}
	
	private static String generateLangKey(String methodName)
	{
		var generated = new StringBuilder();
		var matcher = METHOD_PATTERN.matcher(methodName);
		int lastEnd = 0;
		while(matcher.find())
		{
			int start = matcher.start();
			int end = matcher.end();
			// Append any non-matched characters
			if(lastEnd < start)
			{
				generated.append(methodName, lastEnd, start);
			}
			if(!generated.isEmpty())
			{
				generated.append('_');
			}
			generated.append(methodName, start, end);
			lastEnd = end;
		}
		return generated.toString().toLowerCase();
	}
	
	private String createLangKey(Class<?> langClass, Method method)
	{
		var annotation = method.getAnnotation(LangKey.class);
		Assert.notNull(annotation);
		
		if(!annotation.value().isEmpty())
		{
			return annotation.value().replace("{}", manager.modId());
		}
		
		var prefix = langClass.isAnnotationPresent(LangKeyPattern.class) ?
				langClass.getAnnotation(LangKeyPattern.class).value() :
				"text.{}.";
		prefix = prefix.replace("{}", manager.modId());
		
		var key = !annotation.key().isEmpty() ?
				annotation.key() :
				generateLangKey(method.getName());
		return prefix + key;
	}
	
	private Style getStyle(Method method)
	{
		if(method.isAnnotationPresent(WithStyle.class))
		{
			var annotationStyle = method.getAnnotation(WithStyle.class);
			var style = manager.getStyle(annotationStyle.value());
			if(style == null)
			{
				throw new UndefinedStyleException(annotationStyle.value());
			}
			return style;
		}
		return manager.getStyle("default");
	}
	
	private Parser<?>[] getParsers(Method method)
	{
		Parser<?>[] parsers = new Parser<?>[method.getParameterCount()];
		for(int index = 0; index < method.getParameterCount(); index++)
		{
			var param = method.getParameters()[index];
			var paramType = param.getType();
			String parserKey;
			if(param.isAnnotationPresent(Parsed.class))
			{
				var annotationParsed = param.getAnnotation(Parsed.class);
				parserKey = annotationParsed.value();
			}
			else
			{
				parserKey = "default";
			}
			var parser = manager.getParser(parserKey, paramType);
			if(parser == null)
			{
				if(parserKey.equals("default"))
				{
					parser = Parser.OBJECT;
				}
				else
				{
					throw new UndefinedParserException(parserKey);
				}
			}
			parsers[index] = parser;
		}
		return parsers;
	}
	
	void loadValues(Class<?> langClass, Object proxy)
	{
		Map<String, LangEntry> values = Maps.newHashMap();
		
		for(var method : langClass.getMethods())
		{
			if(method.isAnnotationPresent(LangKey.class))
			{
				var annotation = method.getAnnotation(LangKey.class);
				var methodSignature = method.toGenericString();
				if(method.getReturnType().equals(MutableComponent.class))
				{
					var key = this.createLangKey(langClass, method);
					var style = this.getStyle(method);
					var parsers = this.getParsers(method);
					var entry = new LangEntry(key, annotation.text(), style, parsers);
					if(values.put(methodSignature, entry) != null)
					{
						throw new IllegalStateException("Method with signature %s already exists.".formatted(methodSignature));
					}
				}
				else
				{
					throw new IllegalStateException("Method %s does not return MutableComponent".formatted(methodSignature));
				}
			}
		}
		
		this.values = Collections.unmodifiableMap(values);
	}
	
	private static final Method METHOD_EQUALS, METHOD_HASHCODE, METHOD_TOSTRING;
	
	static
	{
		try
		{
			METHOD_EQUALS = Object.class.getDeclaredMethod("equals", Object.class);
			METHOD_HASHCODE = Object.class.getDeclaredMethod("hashCode");
			METHOD_TOSTRING = Object.class.getDeclaredMethod("toString");
		}
		catch (NoSuchMethodException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if(method.equals(METHOD_EQUALS))
		{
			return proxy == args[0];
		}
		else if(method.equals(METHOD_HASHCODE))
		{
			return System.identityHashCode(proxy);
		}
		else if(method.equals(METHOD_TOSTRING))
		{
			return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy));
		}
		
		var value = values.get(method.toGenericString());
		if(value == null)
		{
			try
			{
				return InvocationHandler.invokeDefault(proxy, method);
			}
			catch (Throwable ex)
			{
				throw new RuntimeException(ex);
			}
		}
		return value.toComponent(args);
	}
}
