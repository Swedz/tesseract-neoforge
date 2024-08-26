package net.swedz.tesseract.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.swedz.tesseract.neoforge.item.DynamicDyedItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DyedItemColor.class)
public class DynamicDyedItemApplyDyeColorMixin
{
	@WrapOperation(
			method = "applyDyes",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColor()I"
			)
	)
	private static int getDyeColor(DyeColor dyeColor,
								   Operation<Integer> original,
								   @Local(argsOnly = true) ItemStack stack)
	{
		return stack.getItem() instanceof DynamicDyedItem item ?
				item.getDyeColor(dyeColor) :
				original.call(dyeColor);
	}
}
