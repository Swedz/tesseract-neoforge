package net.swedz.tesseract.neoforge.registry;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public final class AccessibleBlockLootSubProvider
{
	private final BlockLootSubProvider provider;
	
	public AccessibleBlockLootSubProvider(BlockLootSubProvider provider)
	{
		this.provider = provider;
	}
	
	public LootItemCondition.Builder hasSilkTouch()
	{
		return provider.hasSilkTouch();
	}
	
	public LootItemCondition.Builder doesNotHaveSilkTouch()
	{
		return provider.doesNotHaveSilkTouch();
	}
	
	public LootItemCondition.Builder hasShearsOrSilkTouch()
	{
		return provider.hasShearsOrSilkTouch();
	}
	
	public LootItemCondition.Builder doesNotHaveShearsOrSilkTouch()
	{
		return provider.doesNotHaveShearsOrSilkTouch();
	}
	
	public <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike item, FunctionUserBuilder<T> functionBuilder)
	{
		return provider.applyExplosionDecay(item, functionBuilder);
	}
	
	public <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike item, ConditionUserBuilder<T> conditionBuilder)
	{
		return provider.applyExplosionCondition(item, conditionBuilder);
	}
	
	public LootTable.Builder createSilkTouchDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder)
	{
		return provider.createSilkTouchDispatchTable(block, builder);
	}
	
	public LootTable.Builder createShearsDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder)
	{
		return provider.createShearsDispatchTable(block, builder);
	}
	
	public LootTable.Builder createSilkTouchOrShearsDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder)
	{
		return provider.createSilkTouchOrShearsDispatchTable(block, builder);
	}
	
	public LootTable.Builder createSingleItemTableWithSilkTouch(Block block, ItemLike item)
	{
		return provider.createSingleItemTableWithSilkTouch(block, item);
	}
	
	public LootTable.Builder createSingleItemTable(ItemLike item, NumberProvider count)
	{
		return provider.createSingleItemTable(item, count);
	}
	
	public LootTable.Builder createSingleItemTableWithSilkTouch(Block block, ItemLike item, NumberProvider count)
	{
		return provider.createSingleItemTableWithSilkTouch(block, item, count);
	}
	
	public LootTable.Builder createSilkTouchOnlyTable(ItemLike item)
	{
		return provider.createSilkTouchOnlyTable(item);
	}
	
	public LootTable.Builder createPotFlowerItemTable(ItemLike item)
	{
		return provider.createPotFlowerItemTable(item);
	}
	
	public LootTable.Builder createSlabItemTable(Block block)
	{
		return provider.createSlabItemTable(block);
	}
	
	public <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block block, Property<T> property, T value)
	{
		return provider.createSinglePropConditionTable(block, property, value);
	}
	
	public LootTable.Builder createNameableBlockEntityTable(Block block)
	{
		return provider.createNameableBlockEntityTable(block);
	}
	
	public LootTable.Builder createShulkerBoxDrop(Block block)
	{
		return provider.createShulkerBoxDrop(block);
	}
	
	public LootTable.Builder createCopperOreDrops(Block block)
	{
		return provider.createCopperOreDrops(block);
	}
	
	public LootTable.Builder createLapisOreDrops(Block block)
	{
		return provider.createLapisOreDrops(block);
	}
	
	public LootTable.Builder createRedstoneOreDrops(Block block)
	{
		return provider.createRedstoneOreDrops(block);
	}
	
	public LootTable.Builder createBannerDrop(Block block)
	{
		return provider.createBannerDrop(block);
	}
	
	public LootTable.Builder createBeeNestDrop(Block block)
	{
		return provider.createBeeNestDrop(block);
	}
	
	public LootTable.Builder createBeeHiveDrop(Block block)
	{
		return provider.createBeeHiveDrop(block);
	}
	
	public LootTable.Builder createCaveVinesDrop(Block block)
	{
		return provider.createCaveVinesDrop(block);
	}
	
	public LootTable.Builder createOreDrop(Block block, Item item)
	{
		return provider.createOreDrop(block, item);
	}
	
	public LootTable.Builder createMushroomBlockDrop(Block block, ItemLike item)
	{
		return provider.createMushroomBlockDrop(block, item);
	}
	
	public LootTable.Builder createGrassDrops(Block block)
	{
		return provider.createGrassDrops(block);
	}
	
	public LootTable.Builder createMultifaceBlockDrops(Block block, LootItemCondition.Builder builder)
	{
		return provider.createMultifaceBlockDrops(block, builder);
	}
	
	public LootTable.Builder createLeavesDrops(Block leavesBlock, Block saplingBlock, float... chances)
	{
		return provider.createLeavesDrops(leavesBlock, saplingBlock, chances);
	}
	
	public LootTable.Builder createOakLeavesDrops(Block oakLeavesBlock, Block saplingBlock, float... chances)
	{
		return provider.createOakLeavesDrops(oakLeavesBlock, saplingBlock, chances);
	}
	
	public LootTable.Builder createMangroveLeavesDrops(Block block)
	{
		return provider.createMangroveLeavesDrops(block);
	}
	
	public LootTable.Builder createCropDrops(Block cropBlock, Item grownCropItem, Item seedsItem, LootItemCondition.Builder dropGrownCropCondition)
	{
		return provider.createCropDrops(cropBlock, grownCropItem, seedsItem, dropGrownCropCondition);
	}
	
	public LootTable.Builder createDoublePlantShearsDrop(Block sheared)
	{
		return provider.createDoublePlantShearsDrop(sheared);
	}
	
	public LootTable.Builder createDoublePlantWithSeedDrops(Block block, Block sheared)
	{
		return provider.createDoublePlantWithSeedDrops(block, sheared);
	}
	
	public LootTable.Builder createCandleDrops(Block candleBlock)
	{
		return provider.createCandleDrops(candleBlock);
	}
	
	public LootTable.Builder createPetalsDrops(Block petalBlock)
	{
		return provider.createPetalsDrops(petalBlock);
	}
	
	public LootTable.Builder createDoorTable(Block doorBlock)
	{
		return provider.createDoorTable(doorBlock);
	}
	
	public LootTable.Builder createSingleItemTable(ItemLike item)
	{
		return provider.createSingleItemTable(item);
	}
	
	public LootTable.Builder createStemDrops(Block block, Item item)
	{
		return provider.createStemDrops(block, item);
	}
	
	public LootTable.Builder createAttachedStemDrops(Block block, Item item)
	{
		return provider.createAttachedStemDrops(block, item);
	}
	
	public LootTable.Builder createSelfDropDispatchTable(Block block, LootItemCondition.Builder conditionBuilder, LootPoolEntryContainer.Builder<?> alternativeBuilder)
	{
		return BlockLootSubProvider.createSelfDropDispatchTable(block, conditionBuilder, alternativeBuilder);
	}
	
	public LootTable.Builder createShearsOnlyDrop(ItemLike item)
	{
		return BlockLootSubProvider.createShearsOnlyDrop(item);
	}
	
	public LootTable.Builder createCandleCakeDrops(Block candleCakeBlock)
	{
		return BlockLootSubProvider.createCandleCakeDrops(candleCakeBlock);
	}
	
	public LootTable.Builder noDrop()
	{
		return BlockLootSubProvider.noDrop();
	}
}
