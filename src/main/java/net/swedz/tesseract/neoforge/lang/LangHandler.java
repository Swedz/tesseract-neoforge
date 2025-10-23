package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.lang.annotation.LangKey;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

public final class LangHandler implements InvocationHandler
{
	private static final Pattern METHOD_PATTERN = Pattern.compile("([A-Z][a-z]+)|([a-z]+)|([0-9]+)|([A-Z]+(?![a-z]))");
	
	private final LangManager manager;
	
	private Map<String, String> values = Map.of();
	
	public LangHandler(LangManager manager)
	{
		this.manager = manager;
	}
	
	private static String createLangKey(String modId, Method method, LangKey annotation)
	{
		if(!annotation.value().isEmpty())
		{
			return annotation.value();
		}
		String key;
		if(!annotation.key().isEmpty())
		{
			key = annotation.key();
		}
		else
		{
			String methodName = method.getName();
			
			StringBuilder generated = new StringBuilder();
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
			key = generated.toString().toLowerCase();
		}
		return "text.%s.%s".formatted(modId, key);
	}
	
	void loadValues(Class<?> langClass, Object proxy)
	{
		Map<String, String> values = Maps.newHashMap();
		
		for(var method : langClass.getMethods())
		{
			if(method.isAnnotationPresent(LangKey.class))
			{
				if(manager.isValidTextLine(method.getReturnType()))
				{
					var annotation = method.getAnnotation(LangKey.class);
					String key = createLangKey(manager.modId(), method, annotation);
					Tesseract.LOGGER.info("loaded method {}: {}", method.getName(), key);
					if(values.put(method.getName(), key) != null)
					{
						throw new IllegalStateException("Method with name %s already exists.".formatted(method.getName()));
					}
				}
				else
				{
					throw new IllegalStateException("Method with name %s does not have a supported return type %s".formatted(method.getName(), method.getReturnType()));
				}
			}
		}
		
		this.values = Collections.unmodifiableMap(values);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		var key = values.get(method.getName());
		var line = manager.createTextLine(method.getReturnType(), key);
		if(args != null)
		{
			for(var arg : args)
			{
				line.arg(arg);
			}
		}
		return line;
	}
}
