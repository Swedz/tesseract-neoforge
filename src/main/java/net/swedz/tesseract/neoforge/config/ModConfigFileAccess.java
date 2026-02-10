package net.swedz.tesseract.neoforge.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.config.ConfigFileAccess;
import net.swedz.tesseract.config.DefaultValueConfigHandler;
import net.swedz.tesseract.config.annotation.ConfigComment;
import net.swedz.tesseract.config.annotation.ConfigKey;
import net.swedz.tesseract.config.annotation.ConfigOrder;
import net.swedz.tesseract.config.annotation.Range;
import net.swedz.tesseract.config.annotation.SubSection;
import net.swedz.tesseract.config.exception.IllegalConfigOptionException;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ModConfigFileAccess implements ConfigFileAccess<Object>
{
	private final ModConfigCodecMap codecs = new ModConfigCodecMap();
	
	private final ModContainer   container;
	private final ModConfig.Type type;
	private final String         fileName;
	
	private boolean includeDefaultValueComments;
	
	private ModConfigSpec spec;
	
	public ModConfigFileAccess(ModContainer container, ModConfig.Type type, String fileName)
	{
		this.container = container;
		this.type = type;
		this.fileName = fileName;
	}
	
	public ModConfigFileAccess(ModContainer container, ModConfig.Type type)
	{
		this(container, type, null);
	}
	
	public ModConfigFileAccess includeDefaultValueComments()
	{
		includeDefaultValueComments = true;
		return this;
	}
	
	@Override
	public ModConfigCodecMap codecs()
	{
		return codecs;
	}
	
	private void buildConfig(
			ModConfigSpec.Builder builder,
			Class<?> configClass
	) throws Throwable
	{
		Assert.noneNull(builder, configClass);
		
		var proxy = Proxy.newProxyInstance(configClass.getClassLoader(), new Class[]{configClass}, new DefaultValueConfigHandler());
		
		List<Method> methods = Lists.newArrayList(configClass.getMethods());
		methods.sort((a, b) ->
					 {
						 if(a.isAnnotationPresent(ConfigOrder.class) && b.isAnnotationPresent(ConfigOrder.class))
						 {
							 var orderA = a.getAnnotation(ConfigOrder.class).value();
							 var orderB = b.getAnnotation(ConfigOrder.class).value();
							 return Integer.compare(orderA, orderB);
						 }
						 return 0;
					 });
		
		Set<String> keys = Sets.newHashSet();
		
		for(var method : methods)
		{
			if(method.isAnnotationPresent(ConfigKey.class))
			{
				if(method.getParameterCount() != 0)
				{
					throw new IllegalConfigOptionException("Cannot have config method with parameters");
				}
				
				if(method.isAnnotationPresent(ConfigComment.class))
				{
					String[] comments = method.getAnnotation(ConfigComment.class).value();
					builder.comment(comments);
				}
				
				String key = method.getAnnotation(ConfigKey.class).value();
				if(key.isEmpty())
				{
					key = NamingConventionHelper.fromCamelCaseToSnakeCase(method);
				}
				Class type = method.getReturnType();
				
				if(!keys.add(key))
				{
					throw new IllegalConfigOptionException("Duplicate config key: %s".formatted(key));
				}
				
				if(method.isAnnotationPresent(SubSection.class))
				{
					builder.push(key);
					this.buildConfig(builder, type);
					builder.pop();
					continue;
				}
				
				if(method.isDefault())
				{
					var defaultValue = InvocationHandler.invokeDefault(proxy, method);
					this.defineValue(method, builder, key, defaultValue);
				}
				else
				{
					throw new IllegalConfigOptionException("Cannot retrieve default value from method %s".formatted(method.getName()));
				}
			}
		}
	}
	
	private static final Map<Class<? extends Annotation>, Class<? extends Number>> RANGE_NUMBERS = Map.of(
			Range.Integer.class, Integer.class,
			Range.Double.class, Double.class,
			Range.Long.class, Long.class
	);
	
	private static boolean maybeDefineRange(ModConfigSpec.Builder builder, String key, Object value, Method method)
	{
		for(var entry : RANGE_NUMBERS.entrySet())
		{
			Class<? extends Annotation> annotation = entry.getKey();
			Class<? extends Number> number = entry.getValue();
			if(maybeDefineRange(builder, key, value, method, annotation, number))
			{
				return true;
			}
		}
		return false;
	}
	
	private static <A extends Annotation, N extends Number> boolean maybeDefineRange(ModConfigSpec.Builder builder, String key, Object value, Method method, Class<A> annotation, Class<N> numberType)
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
				case Range.Integer range -> builder.defineInRange(key, (java.lang.Integer) value, range.min(), range.max());
				case Range.Double range -> builder.defineInRange(key, (java.lang.Double) value, range.min(), range.max());
				case Range.Long range -> builder.defineInRange(key, (java.lang.Long) value, range.min(), range.max());
				default ->
						throw new IllegalConfigOptionException("Unsupported numeric range annotation: %s".formatted(annotationInstance.annotationType().getName()));
			}
			return true;
		}
		return false;
	}
	
	private void defineValue(
			Method method,
			ModConfigSpec.Builder builder,
			String key,
			Object defaultValue
	)
	{
		Class type = method.getReturnType();
		
		Object value;
		if(codecs.has(type))
		{
			var codec = codecs.get(type);
			value = codec.encode(defaultValue);
			builder.define(key, value, (currentValue) ->
			{
				try
				{
					codec.decode(currentValue);
					return true;
				}
				catch(Exception ex)
				{
					return false;
				}
			});
		}
		else
		{
			if(includeDefaultValueComments)
			{
				builder.comment("Default: " + defaultValue);
			}
			value = defaultValue;
			if(maybeDefineRange(builder, key, value, method))
			{
				return;
			}
			if(value instanceof Enum enumValue)
			{
				builder.defineEnum(key, enumValue);
				return;
			}
			builder.define(key, value);
		}
	}
	
	@Override
	public void load(Class<?> proxyClass)
	{
		try
		{
			var builder = new ModConfigSpec.Builder();
			this.buildConfig(builder, proxyClass);
			spec = builder.build();
		}
		catch(Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		
		if(fileName == null)
		{
			container.registerConfig(type, spec);
		}
		else
		{
			container.registerConfig(type, spec, fileName);
		}
	}
	
	@Override
	public Object get(Class<?> type, String path)
	{
		Assert.notNull(spec, "Config file has not yet been loaded", IllegalStateException::new);
		
		ModConfigSpec.ConfigValue<?> configValue = spec.getValues().get(path);
		var value = configValue.get();
		if(codecs.has(type))
		{
			var codec = codecs.get(type);
			return codec.decode(value);
		}
		return value;
	}
}
