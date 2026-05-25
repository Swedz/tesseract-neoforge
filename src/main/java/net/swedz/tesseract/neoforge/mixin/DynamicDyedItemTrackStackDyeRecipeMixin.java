package net.swedz.tesseract.neoforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.DyeRecipe;
import net.swedz.tesseract.neoforge.item.DynamicDyedItemTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DyeRecipe.class)
public class DynamicDyedItemTrackStackDyeRecipeMixin
{
	@Inject(
			method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/component/DyedItemColor;applyDyes(Lnet/minecraft/world/item/component/DyedItemColor;Ljava/util/List;)Lnet/minecraft/world/item/component/DyedItemColor;"
			)
	)
	private void assemble(
			CraftingInput input,
			CallbackInfoReturnable<ItemStack> callback,
			@Local ItemStack targetStack
	)
	{
		DynamicDyedItemTracker.set(targetStack);
	}
}
