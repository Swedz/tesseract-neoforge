package net.swedz.tesseract.neoforge.compat.mi.mixin.hack;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

@Mixin(
		value = MIMachineRecipeTypes.class,
		remap = false
)
public class RegisterMachineRecipeTypeHackMixin
{
	@Shadow
	@Final
	private static List<MachineRecipeType> recipeTypes;
	
	@Inject(
			method = "create(Ljava/lang/String;Ljava/util/function/Function;)Laztech/modern_industrialization/machines/recipe/MachineRecipeType;",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void create(String name, Function<ResourceLocation, MachineRecipeType> creator,
							   CallbackInfoReturnable<MachineRecipeType> callback)
	{
		if(MIHookTracker.isTracking())
		{
			MIHookRegistry registry = MIHooks.getRegistry(MIHookTracker.getTrackingModId());
			
			MachineRecipeType type = creator.apply(MIHookTracker.id(name));
			registry.recipeSerializerRegistry().register(name, () -> type);
			registry.recipeTypeRegistry().register(name, () -> type);
			registry.onMachineRecipeTypeRegister(type);
			recipeTypes.add(type);
			callback.setReturnValue(type);
		}
	}
}
