package net.swedz.tesseract.neoforge.compat.mi.loot.itempredicate;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.item.SimpleEnergyItem;

public record SimpleEnergyItemHasChargePredicate() implements ItemSubPredicate
{
	public static final Codec<SimpleEnergyItemHasChargePredicate> CODEC = Codec.unit(SimpleEnergyItemHasChargePredicate::new);
	
	@Override
	public boolean matches(ItemStack stack)
	{
		return stack != null &&
			   stack.getItem() instanceof SimpleEnergyItem item &&
			   item.getStoredEnergy(stack) > 0;
	}
}
