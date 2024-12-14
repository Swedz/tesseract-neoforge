package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.components.UpgradeComponent;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.compat.mi.api.ComponentStackHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = UpgradeComponent.class,
		remap = false
)
public interface UpgradeComponentAccessor extends ComponentStackHolder
{
	@Accessor("itemStack")
	ItemStack getStack();
	
	@Accessor("itemStack")
	void setStack(ItemStack stack);
}
