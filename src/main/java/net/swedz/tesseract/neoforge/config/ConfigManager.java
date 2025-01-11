package net.swedz.tesseract.neoforge.config;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.config.annotation.ConfigComment;
import net.swedz.tesseract.neoforge.config.annotation.ConfigKey;
import net.swedz.tesseract.neoforge.config.annotation.ConfigOrder;
import net.swedz.tesseract.neoforge.config.annotation.SubSection;
import net.swedz.tesseract.neoforge.serialization.TomlOps;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public final class ConfigManager
{
	private final ConfigCodecMap codecs = new ConfigCodecMap();
	
	public ConfigCodecMap codecs()
	{
		return codecs;
	}
	
	<C> ConfigInstance<C> build(ModConfigSpec spec, Class<C> configClass, String path)
	{
		try
		{
			var handler = new ConfigHandler(this, spec, path);
			var proxy = (C) Proxy.newProxyInstance(configClass.getClassLoader(), new Class[]{configClass}, handler);
			
			return new ConfigInstance<>(configClass, proxy, handler);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	public <C> ConfigInstance<C> build(Class<C> configClass)
	{
		try
		{
			var builder = new ModConfigSpec.Builder();
			buildSpec(builder, configClass);
			var spec = builder.build();
			return this.build(spec, configClass, "");
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	private <C> void buildSpec(ModConfigSpec.Builder builder, Class<C> configClass) throws Throwable
	{
		Assert.noneNull(builder, configClass);
		
		var defaultValueProxy = Proxy.newProxyInstance(configClass.getClassLoader(), new Class[]{configClass}, new DefaultValueConfigHandler());
		
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
				Class type = method.getReturnType();
				
				if(method.isAnnotationPresent(SubSection.class))
				{
					builder.push(key);
					buildSpec(builder, type);
					builder.pop();
					continue;
				}
				
				if(method.isDefault())
				{
					var defaultValue = InvocationHandler.invokeDefault(defaultValueProxy, method);
					Object value;
					if(codecs.has(type))
					{
						var codec = codecs.get(type);
						value = codec.encodeStart(TomlOps.INSTANCE, defaultValue)
								.getOrThrow((error) -> new IllegalConfigOptionException("Unable to encode default value %s: %s".formatted(defaultValue, error)));
						builder.define(key, value, (currentValue) -> codec.parse(TomlOps.INSTANCE, currentValue).isSuccess());
					}
					else
					{
						value = defaultValue;
						builder.define(key, value);
					}
				}
				else
				{
					throw new IllegalConfigOptionException("Cannot retrieve default value from method %s".formatted(method.getName()));
				}
			}
		}
	}
}
