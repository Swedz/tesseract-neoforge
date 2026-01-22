package net.swedz.tesseract.neoforge.interfaceproxy;

import com.google.common.collect.Maps;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class InterfaceProxyHandler<E extends InterfaceProxyEntry<?>> implements InvocationHandler
{
	private Map<String, E> values = Map.of();
	
	public Optional<Comparator<E>> sorter()
	{
		return Optional.empty();
	}
	
	protected abstract Optional<E> generate(Class<?> proxyClass, Object proxy, Method method);
	
	public final List<E> entries()
	{
		var stream = values.values().stream();
		stream = this.sorter()
				.map(stream::sorted)
				.orElse(stream);
		return stream.toList();
	}
	
	final void loadValues(Class<?> proxyClass, Object proxy)
	{
		Map<String, E> values = Maps.newHashMap();
		
		for(var method : proxyClass.getMethods())
		{
			var methodSignature = method.toGenericString();
			this.generate(proxyClass, proxy, method).ifPresent((entry) ->
			{
				if(values.put(methodSignature, entry) != null)
				{
					throw new IllegalStateException("Method with signature %s already exists.".formatted(methodSignature));
				}
			});
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
		return value.resolve(args);
	}
}
