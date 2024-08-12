package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(
		value = MIMachineRecipeTypes.class,
		remap = false
)
public interface MIMachineRecipeTypesAccessor
{
	@Accessor("recipeTypes")
	static List<MachineRecipeType> getRecipeTypes()
	{
		throw new UnsupportedOperationException();
	}
}
