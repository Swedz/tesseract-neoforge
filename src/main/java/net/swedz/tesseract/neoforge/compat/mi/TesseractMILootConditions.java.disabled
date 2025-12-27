package net.swedz.tesseract.neoforge.compat.mi;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.loot.itempredicate.SimpleEnergyItemHasChargePredicate;

import java.util.function.Supplier;

public final class TesseractMILootConditions
{
	public static final class ItemSubPredicates
	{
		private static final DeferredRegister<ItemSubPredicate.Type<?>> ITEM_SUB_PREDICATE_TYPES = DeferredRegister.create(Registries.ITEM_SUB_PREDICATE_TYPE, Tesseract.ID);
		
		public static final Supplier<ItemSubPredicate.Type<SimpleEnergyItemHasChargePredicate>> SIMPLE_ENERGY_ITEM_HAS_CHARGE = create("simple_energy_item_has_charge", SimpleEnergyItemHasChargePredicate.CODEC);
		
		private static <T extends ItemSubPredicate> Supplier<ItemSubPredicate.Type<T>> create(String name, Codec<T> codec)
		{
			return ITEM_SUB_PREDICATE_TYPES.register(name, () -> new ItemSubPredicate.Type<>(codec));
		}
	}
	
	public static void init(IEventBus bus)
	{
		ItemSubPredicates.ITEM_SUB_PREDICATE_TYPES.register(bus);
	}
}
