package net.swedz.tesseract.neoforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.swedz.tesseract.neoforge.item.component.DataComponentTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class DataComponentTooltipProviderMixin
{
	@Inject(
			method = "getTooltipLines",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/core/component/DataComponents;LORE:Lnet/minecraft/core/component/DataComponentType;"
			)
	)
	private void appendDataComponentTooltips(Item.TooltipContext context, Player player, TooltipFlag flag,
											 CallbackInfoReturnable<List<Component>> callback,
											 @Local(name = "consumer") Consumer<Component> consumer)
	{
		ItemStack stack = (ItemStack) (Object) this;
		for(TypedDataComponent<?> component : stack.getComponents())
		{
			Object value = component.value();
			if(value instanceof DataComponentTooltipProvider tooltipProvider)
			{
				tooltipProvider.addToTooltip(context, consumer::accept, flag);
			}
		}
	}
}
