package net.swedz.tesseract.neoforge.compat.mi.network;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.swedz.tesseract.neoforge.TesseractMod;
import net.swedz.tesseract.neoforge.compat.mi.network.packets.UpdateMachineConfigurationPanelPacket;

import java.util.Map;
import java.util.Set;

public final class TesseractMIPackets
{
	public static final class Registry
	{
		private static final Set<PacketRegistration<?>>                                    PACKET_REGISTRATIONS = Sets.newHashSet();
		private static final Map<Class<? extends TesseractMIBasePacket>, ResourceLocation> PACKET_IDS           = Maps.newHashMap();
		
		private record PacketRegistration<P extends TesseractMIBasePacket>(
				ResourceLocation resourceLocation,
				Class<P> packetClass,
				FriendlyByteBuf.Reader<P> packetConstructor
		)
		{
		}
		
		private static void init(RegisterPayloadHandlerEvent event)
		{
			IPayloadRegistrar registrar = event.registrar(TesseractMod.ID);
			for(PacketRegistration<?> packetRegistration : PACKET_REGISTRATIONS)
			{
				registrar.play(packetRegistration.resourceLocation(), packetRegistration.packetConstructor(), (p, context) ->
						context.workHandler().execute(() ->
								p.handle(new TesseractMIBasePacket.Context(packetRegistration.packetClass(), context))));
			}
		}
	}
	
	public static void init(RegisterPayloadHandlerEvent event)
	{
		Registry.init(event);
	}
	
	static
	{
		create("configure_machine", UpdateMachineConfigurationPanelPacket.class, UpdateMachineConfigurationPanelPacket::new);
	}
	
	public static ResourceLocation getId(Class<? extends TesseractMIBasePacket> packetClass)
	{
		return Registry.PACKET_IDS.get(packetClass);
	}
	
	public static <P extends TesseractMIBasePacket> void create(String path, Class<P> packetClass, FriendlyByteBuf.Reader<P> packetConstructor)
	{
		Registry.PACKET_REGISTRATIONS.add(new Registry.PacketRegistration<>(TesseractMod.id(path), packetClass, packetConstructor));
		Registry.PACKET_IDS.put(packetClass, TesseractMod.id(path));
	}
}
