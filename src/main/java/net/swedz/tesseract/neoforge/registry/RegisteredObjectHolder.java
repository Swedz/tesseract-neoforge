package net.swedz.tesseract.neoforge.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.swedz.tesseract.neoforge.api.MCIdentifiable;
import net.swedz.tesseract.neoforge.api.MCIdentifier;
import net.swedz.tesseract.neoforge.capabilities.CapabilitiesListeners;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class RegisteredObjectHolder<Thing, ActualThing extends Thing, Self extends RegisteredObjectHolder<Thing, ActualThing, Self>> implements MCIdentifiable, Supplier<ActualThing>
{
	protected final MCIdentifier identifier;
	
	protected final Set<TagKey<Thing>> tags = Sets.newHashSet();
	
	protected final List<Consumer<? super ActualThing>> registrationListeners = Lists.newArrayList();
	
	private boolean locked;
	
	public RegisteredObjectHolder(ResourceLocation location, String englishName)
	{
		this.identifier = new MCIdentifier(location, englishName);
	}
	
	protected final Self self()
	{
		return (Self) this;
	}
	
	@Override
	public final MCIdentifier identifier()
	{
		return identifier;
	}
	
	public Set<TagKey<Thing>> tags()
	{
		return Set.copyOf(tags);
	}
	
	@SafeVarargs
	public final Self tag(TagKey<Thing>... tags)
	{
		Collections.addAll(this.tags, tags);
		return this.self();
	}
	
	public final Self tag(List<TagKey<Thing>> tags)
	{
		this.tags.addAll(tags);
		return this.self();
	}
	
	public void triggerRegistrationListener()
	{
		registrationListeners.forEach((listener) -> listener.accept(this.get()));
	}
	
	public Self withRegistrationListener(Consumer<? super ActualThing> listener)
	{
		registrationListeners.add(listener);
		return this.self();
	}
	
	public Self withCapabilities(BiConsumer<? super ActualThing, RegisterCapabilitiesEvent> listener)
	{
		return this.withRegistrationListener((thing) ->
				CapabilitiesListeners.register(identifier.modId(), (event) -> listener.accept(thing, event)));
	}
	
	public final boolean isLocked()
	{
		return locked;
	}
	
	public final void guaranteeUnlocked()
	{
		if(locked)
		{
			throw new IllegalStateException("The holder is already locked");
		}
	}
	
	public final void lock()
	{
		locked = true;
	}
	
	public abstract Self register();
}
