package net.swedz.tesseract.neoforge.lang;

import com.google.common.collect.Maps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.lang.parser.ParserProvider;
import net.swedz.tesseract.neoforge.lang.style.StyleProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class LangContext
{
	private final Map<Class<?>, Annotation[]> annotations;
	
	private final Function<String, StyleProvider>                 styleGetter;
	private final BiFunction<String, Class<?>, ParserProvider<?>> parserGetter;
	
	private LangContext(Map<Class<?>, Annotation[]> annotations,
						Function<String, StyleProvider> styleGetter,
						BiFunction<String, Class<?>, ParserProvider<?>> parserGetter)
	{
		this.annotations = Collections.unmodifiableMap(annotations);
		this.styleGetter = styleGetter;
		this.parserGetter = parserGetter;
	}
	
	public static LangContext of(AnnotatedElement element,
								 Function<String, StyleProvider> styleGetter,
								 BiFunction<String, Class<?>, ParserProvider<?>> parserGetter)
	{
		Map<Class<?>, Annotation[]> annotations = Maps.newHashMap();
		
		for(var annotation : element.getDeclaredAnnotations())
		{
			var type = annotation.annotationType();
			var current = annotations.get(type);
			if(current == null)
			{
				annotations.put(type, new Annotation[]{annotation});
			}
			else
			{
				var copy = Arrays.copyOf(current, current.length + 1);
				copy[copy.length] = annotation;
				annotations.put(type, copy);
			}
		}
		
		return new LangContext(annotations, styleGetter, parserGetter);
	}
	
	public boolean hasAnnotation(Class<?> type)
	{
		return annotations.containsKey(type);
	}
	
	public <A extends Annotation> A[] getAnnotations(Class<A> type)
	{
		var present = annotations.get(type);
		return present == null ?
				(A[]) new Object[0] :
				(A[]) Arrays.copyOf(present, present.length);
	}
	
	public <A extends Annotation> A getAnnotation(Class<A> type)
	{
		var present = this.getAnnotations(type);
		return present.length == 0 ? null : present[0];
	}
	
	public Style style(String key)
	{
		return styleGetter.apply(key).get(this);
	}
	
	public Style style()
	{
		return this.style("default");
	}
	
	public <T> Component parse(String key, Class<T> type, T value)
	{
		var parser = (ParserProvider<T>) parserGetter.apply(key, type);
		Assert.notNull(parser, "Could not find parser for key " + key + " and type " + type, IllegalArgumentException::new);
		return parser.parse(this, value);
	}
	
	public <T> Component parse(Class<T> type, T value)
	{
		return this.parse("default", type, value);
	}
}
