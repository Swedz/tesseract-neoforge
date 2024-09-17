package net.swedz.tesseract.neoforge.proxy;

import java.util.EnumSet;

public record LoadedProxy(
		Class<? extends Proxy> key,
		Proxy proxy,
		int priority,
		EnumSet<ProxyEnvironment> environments
)
{
}
