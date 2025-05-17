package net.swedz.tesseract.neoforge.mixin.event;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.tesseract.neoforge.event.PlayerInventoryChangeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public final class PlayerInventoryChangeEventMixin
{
	@Unique
	private ContainerListener playerInventoryChangeListener;
	
	@Inject(
			method = "<init>",
			at = @At("TAIL")
	)
	private void init(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation,
					  CallbackInfo callback)
	{
		var player = (ServerPlayer) (Object) this;
		
		playerInventoryChangeListener = new ContainerListener()
		{
			@Override
			public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack)
			{
				var slot = containerToSend.getSlot(dataSlotIndex);
				if(!(slot instanceof ResultSlot) &&
				   slot.container == player.getInventory())
				{
					NeoForge.EVENT_BUS.post(new PlayerInventoryChangeEvent(player, player.getInventory(), stack));
				}
			}
			
			@Override
			public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value)
			{
			}
		};
	}
	
	@Inject(
			method = "initMenu",
			at = @At("HEAD")
	)
	private void initMenu(AbstractContainerMenu openedMenu, CallbackInfo callback)
	{
		openedMenu.addSlotListener(playerInventoryChangeListener);
	}
}
