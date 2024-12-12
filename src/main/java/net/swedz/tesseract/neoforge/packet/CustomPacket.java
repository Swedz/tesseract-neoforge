package net.swedz.tesseract.neoforge.packet;

import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Predicate;

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
	
	default int broadcastToClients(ServerLevel level, Predicate<ServerPlayer> predicate)
	{
		int sent = 0;
		for(ServerPlayer player : level.players())
		{
			if(predicate.test(player))
			{
				this.sendToClient(player);
				sent++;
			}
		}
		return sent;
	}
	
	default int broadcastToClients(ServerLevel level)
	{
		return this.broadcastToClients(level, (player) -> true);
	}
	
	default int broadcastToClients(ServerLevel level, Position origin, double distance)
	{
		return this.broadcastToClients(level, (player) -> player.blockPosition().closerToCenterThan(origin, distance));
	}
	
	default int broadcastToClients(ServerLevel level, Vec3i origin, double distance)
	{
		return this.broadcastToClients(level, (player) -> player.blockPosition().closerThan(origin, distance));
	}
	
	Type<? extends CustomPacket> type();
}
