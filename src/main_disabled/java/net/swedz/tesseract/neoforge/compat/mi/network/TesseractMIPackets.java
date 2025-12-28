package net.swedz.tesseract.neoforge.compat.mi.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.network.packet.UpdateMachineConfigurationPanelPacket;
import net.swedz.tesseract.neoforge.packet.PacketRegistry;

public final class TesseractMIPackets
{
	private static final PacketRegistry<TesseractMICustomPacket> REGISTRY = PacketRegistry.create(Tesseract.ID);
	
	public static CustomPacketPayload.Type<TesseractMICustomPacket> getType(Class<? extends TesseractMICustomPacket> packetClass)
	{
		return REGISTRY.getType(packetClass);
	}
	
	public static void init(RegisterPayloadHandlersEvent event)
	{
		REGISTRY.registerAll(event);
	}
	
	static
	{
		create("configure_machine", UpdateMachineConfigurationPanelPacket.class, UpdateMachineConfigurationPanelPacket.STREAM_CODEC);
	}
	
	private static <P extends TesseractMICustomPacket> void create(String id, Class<P> packetClass, StreamCodec<? super RegistryFriendlyByteBuf, P> packetCodec)
	{
		REGISTRY.create(id, packetClass, packetCodec);
	}
}
