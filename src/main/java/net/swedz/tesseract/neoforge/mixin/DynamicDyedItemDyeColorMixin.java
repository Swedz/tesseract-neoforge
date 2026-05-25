package net.swedz.tesseract.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.swedz.tesseract.neoforge.item.DynamicDyedItem;
import net.swedz.tesseract.neoforge.item.DynamicDyedItemTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DyedItemColor.class)
public class DynamicDyedItemDyeColorMixin
{
	@WrapOperation(
			method = "applyDyes(Lnet/minecraft/world/item/component/DyedItemColor;Ljava/util/List;)Lnet/minecraft/world/item/component/DyedItemColor;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColor()I"
			)
	)
	private static int getDyeColor(
			DyeColor dyeColor,
			Operation<Integer> original
	)
	{
		var dyeingStack = DynamicDyedItemTracker.get();
		return dyeingStack != null && dyeingStack.getItem() instanceof DynamicDyedItem item ?
				item.getDyeColor(dyeColor) :
				original.call(dyeColor);
	}
	
	@Inject(
			method = "applyDyes(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;)Lnet/minecraft/world/item/ItemStack;",
			at = @At("HEAD")
	)
	private static void beforeApplyDyes(
			ItemStack itemStack,
			List<DyeColor> dyes,
			CallbackInfoReturnable<ItemStack> callback
	)
	{
		DynamicDyedItemTracker.set(itemStack);
	}
	
	@Inject(
			method = "applyDyes(Lnet/minecraft/world/item/component/DyedItemColor;Ljava/util/List;)Lnet/minecraft/world/item/component/DyedItemColor;",
			at = @At("RETURN")
	)
	private static void afterApplyDyes(
			DyedItemColor currentDye,
			List<DyeColor> dyes,
			CallbackInfoReturnable<DyedItemColor> callback
	)
	{
		DynamicDyedItemTracker.set(null);
	}
	
	@ModifyVariable(
			method = "getOrDefault",
			at = @At("HEAD"),
			argsOnly = true
	)
	private static int getDefaultDyeColor(
			int defaultValue,
			ItemStack stack
	)
	{
		if(defaultValue == DyedItemColor.LEATHER_COLOR)
		{
			return stack.getItem() instanceof DynamicDyedItem item ?
					item.getDefaultDyeColor() :
					defaultValue;
		}
		return defaultValue;
	}
}
