package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.models.MachineCasing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.compat.mi.api.ComponentStackHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(
		value = MachineCasing.class,
		remap = false
)
public interface MachineCasingAccessor extends ComponentStackHolder
{
	@Invoker("<init>")
	static MachineCasing init(ResourceLocation key, Supplier<? extends Block> imitatedBlock)
	{
		throw new UnsupportedOperationException();
	}
}
