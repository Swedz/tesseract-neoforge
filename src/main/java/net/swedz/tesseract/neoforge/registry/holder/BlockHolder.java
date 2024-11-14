package net.swedz.tesseract.neoforge.registry.holder;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.registry.AccessibleBlockLootSubProvider;
import net.swedz.tesseract.neoforge.registry.ModeledRegisteredObjectHolder;
import net.swedz.tesseract.neoforge.registry.registerable.BlockRegisterableWrapper;

import java.util.function.Consumer;
import java.util.function.Function;

public class BlockHolder<BlockType extends Block> extends ModeledRegisteredObjectHolder<Block, BlockType, BlockStateProvider, BlockHolder<BlockType>>
{
	protected final BlockRegisterableWrapper<BlockType> registerableBlock;
	
	protected Function<AccessibleBlockLootSubProvider, LootTable.Builder> lootTableBuilder;
	
	public BlockHolder(ResourceLocation location, String englishName,
					   DeferredRegister.Blocks registerBlocks, Function<BlockBehaviour.Properties, BlockType> blockCreator)
	{
		super(location, englishName);
		this.registerableBlock = new BlockRegisterableWrapper<>(registerBlocks, BlockBehaviour.Properties.of(), blockCreator);
	}
	
	public BlockRegisterableWrapper<BlockType> registerableBlock()
	{
		return registerableBlock;
	}
	
	public BlockHolder<BlockType> withProperties(Consumer<BlockBehaviour.Properties> action)
	{
		registerableBlock.withProperties(action);
		return this.self();
	}
	
	public BlockHolder<BlockType> withLootTable(Function<BlockHolder<BlockType>, Function<AccessibleBlockLootSubProvider, LootTable.Builder>> builder)
	{
		lootTableBuilder = builder.apply(this.self());
		return this.self();
	}
	
	public boolean hasLootTable()
	{
		return lootTableBuilder != null;
	}
	
	public Function<AccessibleBlockLootSubProvider, LootTable.Builder> getLootTableBuilder()
	{
		return lootTableBuilder;
	}
	
	public LootTable.Builder buildLootTable(BlockLootSubProvider provider)
	{
		if(!this.hasLootTable())
		{
			throw new IllegalStateException("Cannot build loot table without a loot table builder");
		}
		return lootTableBuilder.apply(new AccessibleBlockLootSubProvider(provider));
	}
	
	@Override
	public BlockHolder<BlockType> register()
	{
		this.guaranteeUnlocked();
		
		registerableBlock.register(identifier, DeferredRegister.Blocks::registerBlock);
		
		this.lock();
		return this.self();
	}
	
	@Override
	public BlockType get()
	{
		return registerableBlock.getOrThrow();
	}
}
