package net.swedz.tesseract.neoforge.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class DefaultValueConfigHandler implements InvocationHandler
{
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		if(method.isDefault())
		{
			return InvocationHandler.invokeDefault(proxy, method, args);
		}
		throw new UnsupportedOperationException("Cannot invoke non-default method");
	}
}
