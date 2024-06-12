package net.swedz.tesseract.neoforge.localizedlistener;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class LocalizedListeners
{
	private LocalizedListenerMultiMap listeners = new LocalizedListenerMultiMap();
	
	public final void init()
	{
		NeoForge.EVENT_BUS.addListener(ServerStoppedEvent.class, (__) ->
		{
			if(listeners.size() != 0)
			{
				listeners = new LocalizedListenerMultiMap();
			}
		});
		
		this.initListeners();
	}
	
	protected abstract void initListeners();
	
	public <E extends Event> LocalizedListener<E> register(Level level, ChunkPos chunk, Class<E> listenerClass, LocalizedListener<E> listener)
	{
		listeners.add(level, chunk, listenerClass, listener);
		return listener;
	}
	
	public <E extends Event> LocalizedListener<E> register(Level level, Iterable<ChunkPos> chunks, Class<E> listenerClass, LocalizedListener<E> listener)
	{
		for(ChunkPos chunk : chunks)
		{
			register(level, chunk, listenerClass, listener);
		}
		return listener;
	}
	
	public <E extends Event> void unregister(Level level, ChunkPos chunk, Class<E> listenerClass, LocalizedListener<E> listener)
	{
		listeners.remove(level, chunk, listenerClass, listener);
	}
	
	public <E extends Event> void unregister(Level level, Iterable<ChunkPos> chunks, Class<E> listenerClass, LocalizedListener<E> listener)
	{
		for(ChunkPos chunk : chunks)
		{
			unregister(level, chunk, listenerClass, listener);
		}
	}
	
	protected <E extends Event> void withListener(EventPriority priority, Class<E> listenerClass, Predicate<E> condition, Function<E, Level> level, Function<E, ChunkPos> chunk)
	{
		NeoForge.EVENT_BUS.addListener(priority, listenerClass, (event) ->
		{
			if(condition.test(event))
			{
				Set<LocalizedListener<E>> listenerInstances = listeners.get(level.apply(event), chunk.apply(event), listenerClass);
				if(listenerInstances != null)
				{
					for(LocalizedListener<E> listener : listenerInstances)
					{
						listener.on(event);
					}
				}
			}
		});
	}
	
	protected <E extends Event> void withListener(Class<E> listenerClass, Predicate<E> condition, Function<E, Level> level, Function<E, ChunkPos> chunk)
	{
		this.withListener(EventPriority.NORMAL, listenerClass, condition, level, chunk);
	}
}
