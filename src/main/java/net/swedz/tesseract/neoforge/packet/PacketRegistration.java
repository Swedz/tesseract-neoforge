package net.swedz.tesseract.neoforge.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PacketRegistration<P extends CustomPacket>(
		CustomPacketPayload.Type<P> type,
		Class<P> clazz,
		StreamCodec<? super RegistryFriendlyByteBuf, P> codec
)
{
}
