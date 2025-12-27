package net.swedz.tesseract.neoforge.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.config.annotation.ConfigKey;
import net.swedz.tesseract.neoforge.config.annotation.SubSection;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyHandler;
import net.swedz.tesseract.neoforge.serialization.TomlOps;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;

public final class ConfigHandler extends InterfaceProxyHandler<Object, ConfigEntry>
{
	private final ConfigManager manager;
	private final ModConfigSpec spec;
	private final String        path;
	
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
	
	String path()
	{
		return path;
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
	
	@Override
	protected Optional<ConfigEntry> generate(Class<?> proxyClass, Object proxy, Method method)
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
				value = manager.build(type, new ConfigManagerArg(Optional.of(spec), path)).load();
			}
			else
			{
				value = this.loadValue(proxy, method, path);
			}
			return Optional.of(new ConfigEntry(value));
		}
		return Optional.empty();
	}
}
