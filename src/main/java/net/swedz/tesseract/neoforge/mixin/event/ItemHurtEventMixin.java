package net.swedz.tesseract.neoforge.mixin.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.swedz.tesseract.neoforge.event.ItemHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemHurtEventMixin
{
	@Inject(
			method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private void hurtAndBreak(int amount, ServerLevel level, LivingEntity entity, Consumer<Item> destroyedCallback,
							  CallbackInfo callback)
	{
		ItemStack stack = (ItemStack) (Object) this;
		ItemHurtEvent event = new ItemHurtEvent(entity, stack, amount);
		NeoForge.EVENT_BUS.post(event);
		if(event.isCanceled())
		{
			callback.cancel();
		}
	}
}
