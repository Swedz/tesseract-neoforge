package net.swedz.tesseract.neoforge.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public interface CustomPacket extends CustomPacketPayload
{
	void handle(PacketContext context);
	
	default void sendToServer()
	{
		PacketDistributor.sendToServer(this);
	}
	
	default void sendToClient(ServerPlayer player)
	{
		PacketDistributor.sendToPlayer(player, this);
	}
	
	Type<? extends CustomPacket> type();
}
