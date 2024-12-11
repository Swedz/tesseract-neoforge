package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.components.OverdriveComponent;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.compat.mi.api.ComponentStackHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = OverdriveComponent.class,
		remap = false
)
public interface OverdriveComponentAccessor extends ComponentStackHolder
{
	@Accessor("overdriveModule")
	ItemStack getStack();
	
	@Accessor("overdriveModule")
	void setStack(ItemStack stack);
}
