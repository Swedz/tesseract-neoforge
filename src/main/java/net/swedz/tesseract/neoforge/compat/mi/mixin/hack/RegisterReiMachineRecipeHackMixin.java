package net.swedz.tesseract.neoforge.compat.mi.mixin.hack;

import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(
		value = ReiMachineRecipes.class,
		remap = false
)
public class RegisterReiMachineRecipeHackMixin
{
	@ModifyArg(
			method = "registerRecipeCategoryForMachine(Ljava/lang/String;Ljava/lang/String;Laztech/modern_industrialization/compat/rei/machines/ReiMachineRecipes$MachineScreenPredicate;)V",
			at = @At(
					value = "INVOKE",
					target = "Laztech/modern_industrialization/compat/rei/machines/ReiMachineRecipes$ClickAreaCategory;<init>(Lnet/minecraft/resources/ResourceLocation;Laztech/modern_industrialization/compat/rei/machines/ReiMachineRecipes$MachineScreenPredicate;)V"
			)
	)
	private static ResourceLocation registerRecipeCategoryForMachine(ResourceLocation category)
	{
		if(MIHookTracker.isTracking())
		{
			return MIHookTracker.id(category.getPath());
		}
		return category;
	}
}
