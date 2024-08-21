package net.swedz.tesseract.neoforge.proxy.old;

import net.minecraft.world.entity.player.Player;
import net.swedz.tesseract.neoforge.proxy.Proxy;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;

@ProxyEntrypoint
public class TesseractProxy implements Proxy
{
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
