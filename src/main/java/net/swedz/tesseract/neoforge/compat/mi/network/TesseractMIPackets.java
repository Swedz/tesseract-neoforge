package net.swedz.tesseract.neoforge.compat.mi.network;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.network.packets.UpdateMachineConfigurationPanelPacket;

import java.util.Map;
import java.util.Set;

public final class TesseractMIPackets
{
	public static final class Registry
	{
		private static final Set<PacketRegistration<TesseractMIBasePacket>>                                               PACKET_REGISTRATIONS = Sets.newHashSet();
		private static final Map<Class<? extends TesseractMIBasePacket>, CustomPacketPayload.Type<TesseractMIBasePacket>> PACKET_TYPES         = Maps.newHashMap();
		
		private record PacketRegistration<P extends TesseractMIBasePacket>(
				CustomPacketPayload.Type<P> packetType,
				Class<P> packetClass,
				StreamCodec<? super RegistryFriendlyByteBuf, P> packetCodec
		)
		{
		}
		
		private static void init(RegisterPayloadHandlersEvent event)
		{
			PayloadRegistrar registrar = event.registrar(Tesseract.ID);
			for(PacketRegistration<TesseractMIBasePacket> packetRegistration : PACKET_REGISTRATIONS)
			{
				registrar.playBidirectional(packetRegistration.packetType(), packetRegistration.packetCodec(), (packet, context) ->
						packet.handle(new TesseractMIBasePacket.Context(packetRegistration.packetClass(), context)));
			}
		}
	}
	
	public static void init(RegisterPayloadHandlersEvent event)
	{
		Registry.init(event);
	}
	
	static
	{
		create("configure_machine", UpdateMachineConfigurationPanelPacket.class, UpdateMachineConfigurationPanelPacket.STREAM_CODEC);
	}
	
	public static CustomPacketPayload.Type<TesseractMIBasePacket> getType(Class<? extends TesseractMIBasePacket> packetClass)
	{
		return Registry.PACKET_TYPES.get(packetClass);
	}
	
	public static <P extends TesseractMIBasePacket> void create(String path, Class<P> packetClass, StreamCodec<? super RegistryFriendlyByteBuf, P> packetCodec)
	{
		CustomPacketPayload.Type type = new CustomPacketPayload.Type<>(Tesseract.id(path));
		Registry.PACKET_REGISTRATIONS.add(new Registry.PacketRegistration<>(type, packetClass, packetCodec));
		Registry.PACKET_TYPES.put(packetClass, type);
	}
}
