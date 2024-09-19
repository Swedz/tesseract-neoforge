package net.swedz.tesseract.neoforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.item.ExtraAttributeTooltipsHandler;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackExtraAttributeTooltipsMixin
{
	@Unique
	private void maybeAddHeader(Consumer<Component> tooltipAdder, EquipmentSlotGroup equipmentSlotGroup, MutableBoolean headerFlag)
	{
		if(headerFlag.isTrue())
		{
			tooltipAdder.accept(CommonComponents.EMPTY);
			tooltipAdder.accept(Component.translatable("item.modifiers." + equipmentSlotGroup.getSerializedName()).withStyle(ChatFormatting.GRAY));
			headerFlag.setFalse();
		}
	}
	
	@Inject(
			method = "addAttributeTooltips",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V"
			)
	)
	private void addAttributeTooltipsPre(Consumer<Component> tooltipAdder, Player player,
										 CallbackInfo callback,
										 @Local EquipmentSlotGroup equipmentSlotGroup,
										 @Local MutableBoolean headerFlag)
	{
		ItemStack stack = (ItemStack) (Object) this;
		if(stack.getItem() instanceof ExtraAttributeTooltipsHandler handler)
		{
			handler.appendAttributeTooltipsPre(stack, equipmentSlotGroup, (component) ->
			{
				this.maybeAddHeader(tooltipAdder, equipmentSlotGroup, headerFlag);
				tooltipAdder.accept(component);
			});
		}
	}
	
	@Inject(
			method = "addAttributeTooltips",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V",
					shift = At.Shift.AFTER
			)
	)
	private void addAttributeTooltipsPost(Consumer<Component> tooltipAdder, Player player,
										  CallbackInfo callback,
										  @Local EquipmentSlotGroup equipmentSlotGroup,
										  @Local MutableBoolean headerFlag)
	{
		ItemStack stack = (ItemStack) (Object) this;
		if(stack.getItem() instanceof ExtraAttributeTooltipsHandler handler)
		{
			handler.appendAttributeTooltipsPost(stack, equipmentSlotGroup, (component) ->
			{
				this.maybeAddHeader(tooltipAdder, equipmentSlotGroup, headerFlag);
				tooltipAdder.accept(component);
			});
		}
	}
}
