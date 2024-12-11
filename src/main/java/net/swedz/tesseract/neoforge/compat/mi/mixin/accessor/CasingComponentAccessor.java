package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.components.CasingComponent;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.compat.mi.api.ComponentStackHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(
		value = CasingComponent.class,
		remap = false
)
public interface CasingComponentAccessor extends ComponentStackHolder
{
	@Accessor("casingStack")
	ItemStack getStack();
	
	@Invoker("setCasingStack")
	void setStack(ItemStack stack);
}
