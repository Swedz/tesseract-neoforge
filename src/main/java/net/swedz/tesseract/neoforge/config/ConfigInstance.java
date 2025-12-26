package net.swedz.tesseract.neoforge.config;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.swedz.tesseract.neoforge.interfaceproxy.InterfaceProxyInstance;

public record ConfigInstance<C>(
		Class<C> proxyClass,
		C proxy,
		ConfigHandler handler
) implements InterfaceProxyInstance<C, ConfigHandler>
{
	public C config()
	{
		return proxy;
	}
	
	public ConfigInstance<C> register(ModContainer container, ModConfig.Type type)
	{
		container.registerConfig(type, handler.spec());
		return this;
	}
	
	@Override
	public ConfigInstance<C> load()
	{
		InterfaceProxyInstance.super.load();
		return this;
	}
	
	public ConfigInstance<C> listenToLoad(IEventBus bus)
	{
		bus.addListener(FMLCommonSetupEvent.class, (__) -> this.load());
		return this;
	}
}
