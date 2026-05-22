package net.swedz.tesseract.neoforge.config;

import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyEntry;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record ConfigEntry(
		Object value
) implements InterfaceProxyEntry<Object>
{
	@Override
	public Object resolve(Object[] args)
	{
		if(value instanceof ConfigInstance<?> config)
		{
			return config.config();
		}
		else if(value instanceof Supplier<?> supplier)
		{
			return supplier.get();
		}
		else if(value instanceof Consumer consumer)
		{
			consumer.accept(args[0]);
			return null;
		}
		return value;
	}
}
