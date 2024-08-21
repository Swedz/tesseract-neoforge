package net.swedz.tesseract.neoforge.proxy.builtin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;
import net.swedz.tesseract.neoforge.proxy.ProxyEnvironment;

@ProxyEntrypoint(environment = ProxyEnvironment.CLIENT)
public class TesseractClientProxy extends TesseractProxy
{
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public Player getClientPlayer()
	{
		return Minecraft.getInstance().player;
	}
	
	@Override
	public boolean hasControlDown()
	{
		return Screen.hasControlDown();
	}
	
	@Override
	public boolean hasShiftDown()
	{
		return Screen.hasShiftDown();
	}
	
	@Override
	public boolean hasAltDown()
	{
		return Screen.hasAltDown();
	}
}
