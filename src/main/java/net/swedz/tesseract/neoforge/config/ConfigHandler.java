package net.swedz.tesseract.neoforge.config;

import com.google.common.collect.Maps;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.config.annotation.ConfigKey;
import net.swedz.tesseract.neoforge.config.annotation.SubSection;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;
import net.swedz.tesseract.neoforge.serialization.TomlOps;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public final class ConfigHandler implements InvocationHandler
{
	private final ConfigManager manager;
	private final ModConfigSpec spec;
	private final String        path;
	
	private Map<String, Object> values = Maps.newHashMap();
	
	public ConfigHandler(ConfigManager manager, ModConfigSpec spec, String path)
	{
		this.manager = manager;
		this.spec = spec;
		this.path = path;
	}
	
	ModConfigSpec spec()
	{
		return spec;
	}
	
	private String path(String key)
	{
		return (path.isEmpty() ? "" : (path + ".")) + key;
	}
	
	private Supplier<Object> loadValue(Object proxy, Method method, String path)
	{
		ModConfigSpec.ConfigValue<?> configValue = spec.getValues().get(path);
		if(manager.codecs().has(method.getReturnType()))
		{
			var codec = manager.codecs().get(method.getReturnType());
			return () -> codec.parse(TomlOps.INSTANCE, configValue.get()).mapOrElse(
					(result) -> result,
					(error) ->
					{
						Tesseract.LOGGER.error("Failed to parse input value: {}", error.message());
						try
						{
							return InvocationHandler.invokeDefault(proxy, method);
						}
						catch (Throwable ex)
						{
							throw new RuntimeException(ex);
						}
					}
			);
		}
		return configValue::get;
	}
	
	void loadValues(Class<?> configClass, Object proxy)
	{
		Map<String, Object> values = Maps.newHashMap();
		
		for(var method : configClass.getMethods())
		{
			if(method.isAnnotationPresent(ConfigKey.class))
			{
				String key = method.getAnnotation(ConfigKey.class).value();
				if(key.isEmpty())
				{
					key = NamingConventionHelper.fromCamelCaseToSnakeCase(method);
				}
				String path = this.path(key);
				var type = method.getReturnType();
				
				Object value;
				if(method.isAnnotationPresent(SubSection.class))
				{
					value = manager.build(spec, type, path).load();
				}
				else
				{
					value = this.loadValue(proxy, method, path);
				}
				values.put(method.getName(), value);
			}
		}
		
		this.values = Collections.unmodifiableMap(values);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		var value = values.get(method.getName());
		if(value instanceof ConfigInstance<?> config)
		{
			return config.config();
		}
		else if(value instanceof Supplier<?> supplier)
		{
			return supplier.get();
		}
		return value;
	}
}
