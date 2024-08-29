package net.swedz.tesseract.neoforge.proxy.builtin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.proxy.ProxyEntrypoint;
import net.swedz.tesseract.neoforge.proxy.ProxyEnvironment;

import java.util.Optional;

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
	public Optional<Player> findUserWithItem(EquipmentSlot slot, ItemStack stack)
	{
		if(Minecraft.getInstance().isSameThread())
		{
			for(AbstractClientPlayer player : Minecraft.getInstance().level.players())
			{
				if(player.getItemBySlot(slot) == stack)
				{
					return Optional.of(player);
				}
			}
			return Optional.empty();
		}
		else
		{
			return super.findUserWithItem(slot, stack);
		}
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
