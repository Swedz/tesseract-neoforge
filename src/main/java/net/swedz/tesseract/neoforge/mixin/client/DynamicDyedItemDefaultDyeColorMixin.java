package net.swedz.tesseract.neoforge.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.swedz.tesseract.neoforge.item.DynamicDyedItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HumanoidArmorLayer.class)
public class DynamicDyedItemDefaultDyeColorMixin
{
	@ModifyConstant(
			method = "renderArmorPiece",
			constant = @Constant(intValue = DyedItemColor.LEATHER_COLOR)
	)
	private int getDefaultDyeColor(int leatherColor,
								   @Local ItemStack stack)
	{
		return stack.getItem() instanceof DynamicDyedItem item ?
				item.getDefaultDyeColor() :
				leatherColor;
	}
}
