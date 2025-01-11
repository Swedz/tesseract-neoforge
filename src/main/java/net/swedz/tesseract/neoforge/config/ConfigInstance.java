package net.swedz.tesseract.neoforge.config;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public record ConfigInstance<C>(Class<C> configClass, C config, ConfigHandler handler)
{
	public ConfigInstance<C> register(ModContainer container, ModConfig.Type type)
	{
		container.registerConfig(type, handler.spec());
		return this;
	}
	
	public ConfigInstance<C> load()
	{
		handler.loadValues(configClass, config);
		return this;
	}
	
	public ConfigInstance<C> listenToLoad(IEventBus bus)
	{
		bus.addListener(FMLCommonSetupEvent.class, (__) -> this.load());
		return this;
	}
}
