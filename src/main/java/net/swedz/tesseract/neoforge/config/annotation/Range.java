package net.swedz.tesseract.neoforge.config.annotation;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.swedz.tesseract.neoforge.config.IllegalConfigOptionException;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Map;

public final class Range
{
	private static final Map<Class<? extends Annotation>, Class<? extends Number>> NUMBERS = Map.of(
			Integer.class, java.lang.Integer.class,
			Double.class, java.lang.Double.class,
			Long.class, java.lang.Long.class
	);
	
	@ApiStatus.Internal
	public static boolean maybeDefine(ModConfigSpec.Builder builder, String key, Object value, Method method)
	{
		for(var entry : NUMBERS.entrySet())
		{
			Class<? extends Annotation> annotation = entry.getKey();
			Class<? extends Number> number = entry.getValue();
			if(maybeDefine(builder, key, value, method, annotation, number))
			{
				return true;
			}
		}
		return false;
	}
	
	private static <A extends Annotation, N extends Number> boolean maybeDefine(ModConfigSpec.Builder builder, String key, Object value, Method method, Class<A> annotation, Class<N> numberType)
	{
		if(method.isAnnotationPresent(annotation))
		{
			if(!numberType.isAssignableFrom(value.getClass()))
			{
				throw new IllegalConfigOptionException("Type of %s is not a fitting numeric but has a range annotation".formatted(key));
			}
			Annotation annotationInstance = method.getAnnotation(annotation);
			switch (annotationInstance)
			{
				case Integer range -> builder.defineInRange(key, (java.lang.Integer) value, range.min(), range.max());
				case Double range -> builder.defineInRange(key, (java.lang.Double) value, range.min(), range.max());
				case Long range -> builder.defineInRange(key, (java.lang.Long) value, range.min(), range.max());
				default ->
						throw new IllegalConfigOptionException("Unsupported numeric range annotation: %s".formatted(annotationInstance.annotationType().getName()));
			}
			return true;
		}
		return false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Integer
	{
		int min();
		
		int max();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Double
	{
		double min();
		
		double max();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Long
	{
		long min();
		
		long max();
	}
}
