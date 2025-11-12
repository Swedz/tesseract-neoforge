package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;
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
import java.util.function.Supplier;

public final class LangHandler implements InvocationHandler
{
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
				NamingConventionHelper.fromCamelCaseToSnakeCase(method);
		return prefix + key;
	}
	
	private Supplier<Style> getStyle(WithStyle annotationStyle)
	{
		if(annotationStyle != null)
		{
			var style = manager.getStyle(annotationStyle.value());
			if(style == null)
			{
				throw new UndefinedStyleException(annotationStyle.value());
			}
			return style;
		}
		return manager.getStyle("default");
	}
	
	private Supplier<Parser<?>>[] getParsers(Method method)
	{
		Supplier<Parser<?>>[] parsers = new Supplier[method.getParameterCount()];
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
					parser = () -> Parser.OBJECT;
				}
				else
				{
					throw new UndefinedParserException(parserKey);
				}
			}
			
			if(param.isAnnotationPresent(WithStyle.class))
			{
				var annotationStyle = param.getAnnotation(WithStyle.class);
				final Supplier<Parser> finalParser = parser::get;
				parser = () -> (value) ->
				{
					var style = this.getStyle(annotationStyle);
					return finalParser.get().parse(value).copy().withStyle(style == null ? null : style.get());
				};
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
					var style = this.getStyle(method.getAnnotation(WithStyle.class));
					var parsers = this.getParsers(method);
					var entry = new LangEntry(key, annotation.text().length == 0 ? null : annotation.text()[0], style, parsers);
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
				return InvocationHandler.invokeDefault(proxy, method, args);
			}
			catch (Throwable ex)
			{
				throw new RuntimeException(ex);
			}
		}
		return value.toComponent(args);
	}
}
