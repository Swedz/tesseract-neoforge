package net.swedz.tesseract.neoforge.packet;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.swedz.tesseract.neoforge.proxy.ProxyManager;
import net.swedz.tesseract.neoforge.proxy.builtin.TesseractProxy;

public record PacketContext(Class<? extends CustomPacket> packetClass, IPayloadContext handle)
{
	public boolean isClientbound()
	{
		return handle.flow().isClientbound();
	}
	
	public void assetClientbound()
	{
		if(!this.isClientbound())
		{
			throw new IllegalStateException("Cannot handle packet on server: %s".formatted(packetClass));
		}
	}
	
	public void assertServerbound()
	{
		if(this.isClientbound())
		{
			throw new IllegalStateException("Cannot handle packet on client: %s".formatted(packetClass));
		}
	}
	
	public Player getPlayer()
	{
		return this.isClientbound() ? ProxyManager.get(TesseractProxy.class).getClientPlayer() : handle.player();
	}
}
