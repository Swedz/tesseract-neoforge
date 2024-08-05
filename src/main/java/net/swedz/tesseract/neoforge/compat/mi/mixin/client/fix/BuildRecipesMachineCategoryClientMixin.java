package net.swedz.tesseract.neoforge.compat.mi.mixin.client.fix;

import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.viewer.usage.MachineCategory;
import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.function.Consumer;

@Mixin(
		value = MachineCategory.class,
		remap = false
)
public class BuildRecipesMachineCategoryClientMixin
{
	@Shadow
	@Final
	private MachineCategoryParams params;
	
	@Inject(
			method = "buildRecipes",
			at = @At("HEAD"),
			cancellable = true
	)
	private void buildRecipes(RecipeManager recipeManager, RegistryAccess registryAccess,
							  Consumer<RecipeHolder<MachineRecipe>> consumer,
							  CallbackInfo callback)
	{
		callback.cancel();
		
		MIMachineRecipeTypes.getRecipeTypes().stream()
				.flatMap((type) -> type.getRecipes(Minecraft.getInstance().level).stream())
				.filter((recipe) -> params.recipePredicate.test(recipe.value()))
				.sorted(Comparator.comparing(RecipeHolder::id))
				.forEach(consumer);
	}
}
