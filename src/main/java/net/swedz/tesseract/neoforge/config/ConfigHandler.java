package net.swedz.tesseract.neoforge.config;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.config.annotation.ConfigKey;
import net.swedz.tesseract.neoforge.config.annotation.SubSection;
import net.swedz.tesseract.neoforge.config.exception.IllegalConfigOptionException;
import net.swedz.tesseract.neoforge.helper.NamingConventionHelper;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyHandler;
import net.swedz.tesseract.neoforge.serialization.TomlOps;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigHandler extends InterfaceProxyHandler<ConfigEntry>
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
			var returnType = method.getReturnType();
			
			if(returnType == void.class)
			{
				if(method.getParameterCount() != 1)
				{
					throw new IllegalConfigOptionException(method.getName() + " doesn't have exactly 1 parameter");
				}
				var parameterType = method.getParameterTypes()[0];
				if(manager.codecs().has(parameterType))
				{
					var codec = (Codec) manager.codecs().get(parameterType);
					return Optional.of(new ConfigEntry((Consumer<Object>) (value) ->
					{
						ModConfigSpec.ConfigValue configValue = spec.getValues().get(path);
						Object encoded = codec.encodeStart(TomlOps.INSTANCE, value).getOrThrow();
						configValue.set(encoded);
						configValue.save();
					}));
				}
				return Optional.of(new ConfigEntry((Consumer<Object>) (value) ->
				{
					ModConfigSpec.ConfigValue configValue = spec.getValues().get(path);
					configValue.set(value);
					configValue.save();
				}));
			}
			
			Object value;
			if(method.isAnnotationPresent(SubSection.class))
			{
				value = manager.build(returnType, new ConfigManagerArg(Optional.of(spec), path)).load();
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
