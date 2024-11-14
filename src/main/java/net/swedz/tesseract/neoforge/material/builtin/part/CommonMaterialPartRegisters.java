package net.swedz.tesseract.neoforge.material.builtin.part;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.material.builtin.property.OrePartDrops;
import net.swedz.tesseract.neoforge.material.part.MaterialPartAction;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public final class CommonMaterialPartRegisters
{
	public static MaterialPartAction<ItemHolder<? extends Item>> itemTagCommon(String path)
	{
		return (context, holder) -> holder.tag(TagHelper.itemCommonWithChild(path, context.material().id().getPath()));
	}
	
	public static MaterialPartAction<BlockWithItemHolder<Block, BlockItem>> blockItemTagCommon(String path)
	{
		return (context, holder) -> holder.item().tag(TagHelper.itemCommonWithChild(path, context.material().id().getPath()));
	}
	
	public static MaterialPartAction<BlockWithItemHolder<Block, BlockItem>> oreDrop()
	{
		return (context, h) -> h.withLootTable((holder) ->
		{
			OrePartDrops drops = context.getOrThrow(ORE_DROP_PART);
			if(drops == null || drops.drop() == null)
			{
				throw new IllegalArgumentException("Could not find ore drop part");
			}
			Item drop = context.material().get(drops.drop()).asItem();
			Block block = holder.get();
			return (provider) ->
			{
				HolderLookup.RegistryLookup<Enchantment> lookup = provider.registries().lookupOrThrow(Registries.ENCHANTMENT);
				return provider.createSilkTouchDispatchTable(block, provider.applyExplosionDecay(
						block,
						LootItem.lootTableItem(drop)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(drops.drops().getMinValue(), drops.drops().getMaxValue())))
								.apply(ApplyBonusCount.addUniformBonusCount(lookup.getOrThrow(Enchantments.FORTUNE)))
				));
			};
		});
	}
}
