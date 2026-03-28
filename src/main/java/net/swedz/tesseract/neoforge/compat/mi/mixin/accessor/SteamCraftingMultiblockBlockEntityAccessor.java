package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.machines.blockentities.multiblocks.SteamCraftingMultiblockBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = SteamCraftingMultiblockBlockEntity.class,
		remap = false
)
public interface SteamCraftingMultiblockBlockEntityAccessor
{
	@Accessor("steelTier")
	boolean isSteelTier();
}
