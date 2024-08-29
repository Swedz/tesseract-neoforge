package net.swedz.tesseract.neoforge.proxy.builtin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.swedz.tesseract.neoforge.proxy.Proxy;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;

import java.util.Optional;

@ProxyEntrypoint
public class TesseractProxy implements Proxy
{
	private MinecraftServer server;
	
	@Override
	public void init()
	{
		NeoForge.EVENT_BUS.addListener(ServerAboutToStartEvent.class, (event) -> server = event.getServer());
		NeoForge.EVENT_BUS.addListener(ServerStoppedEvent.class, (event) -> server = null);
	}
	
	public final boolean hasServer()
	{
		return server != null;
	}
	
	public final MinecraftServer getServer()
	{
		return server;
	}
	
	public Optional<Player> findUserWithItem(EquipmentSlot slot, ItemStack stack)
	{
		if(this.hasServer())
		{
			for(ServerPlayer player : server.getPlayerList().getPlayers())
			{
				if(player.getItemBySlot(slot) == stack)
				{
					return Optional.of(player);
				}
			}
		}
		return Optional.empty();
	}
	
	public boolean isClient()
	{
		return false;
	}
	
	public Player getClientPlayer()
	{
		throw new UnsupportedOperationException("Client player is not available on the server!");
	}
	
	public boolean hasControlDown()
	{
		return false;
	}
	
	public boolean hasShiftDown()
	{
		return false;
	}
	
	public boolean hasAltDown()
	{
		return false;
	}
}
