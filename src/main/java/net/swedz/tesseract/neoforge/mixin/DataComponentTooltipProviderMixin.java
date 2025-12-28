package net.swedz.tesseract.neoforge.mixin;

import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.swedz.tesseract.neoforge.item.component.DataComponentTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class DataComponentTooltipProviderMixin
{
	@Inject(
			method = "addDetailsToTooltip",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/core/component/DataComponents;LORE:Lnet/minecraft/core/component/DataComponentType;"
			)
	)
	private void appendDataComponentTooltips(Item.TooltipContext context,
											 TooltipDisplay tooltipDisplay,
											 Player player,
											 TooltipFlag flag,
											 Consumer<Component> tooltipAdder,
											 CallbackInfo ci)
	{
		ItemStack stack = (ItemStack) (Object) this;
		for(TypedDataComponent<?> component : stack.getComponents())
		{
			Object value = component.value();
			if(value instanceof DataComponentTooltipProvider tooltipProvider)
			{
				tooltipProvider.addToTooltip(context, tooltipAdder::accept, flag);
			}
		}
	}
}
