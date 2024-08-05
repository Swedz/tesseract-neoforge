package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(
		value = MIMachineRecipeTypes.class,
		remap = false
)
public interface MIMachineRecipeTypesAccessor
{
	@Invoker("create")
	static MachineRecipeType create(String name, Function<ResourceLocation, MachineRecipeType> ctor)
	{
		throw new UnsupportedOperationException();
	}
}
