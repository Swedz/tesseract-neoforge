package net.swedz.tesseract.neoforge.packet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Map;
import java.util.Set;

public final class PacketRegistry<P extends CustomPacket>
{
	private final String namespace;
	
	private final Set<PacketRegistration<P>>                           registrations = Sets.newHashSet();
	private final Map<Class<? extends P>, CustomPacketPayload.Type<P>> types         = Maps.newHashMap();
	
	private boolean registered;
	
	private PacketRegistry(String namespace)
	{
		this.namespace = namespace;
	}
	
	public static <P extends CustomPacket> PacketRegistry<P> create(String namespace)
	{
		return new PacketRegistry<>(namespace);
	}
	
	public boolean hasBeenRegistered()
	{
		return registered;
	}
	
	public <I extends P> void create(String id, Class<I> packetClass, StreamCodec<? super RegistryFriendlyByteBuf, I> packetCodec)
	{
		if(registered)
		{
			throw new IllegalStateException("Registry has already been registered, new packets cannot be created anymore.");
		}
		
		CustomPacketPayload.Type type = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(namespace, id));
		registrations.add(new PacketRegistration<>(type, packetClass, packetCodec));
		types.put(packetClass, type);
	}
	
	public CustomPacketPayload.Type<P> getType(Class<? extends P> packetClass)
	{
		return types.get(packetClass);
	}
	
	public void registerAll(RegisterPayloadHandlersEvent event)
	{
		registered = true;
		
		PayloadRegistrar registrar = event.registrar(namespace);
		for(PacketRegistration<P> packetRegistration : registrations)
		{
			registrar.playBidirectional(packetRegistration.type(), packetRegistration.codec(), (packet, context) ->
					packet.handle(new PacketContext(packetRegistration.clazz(), context)));
		}
	}
}
