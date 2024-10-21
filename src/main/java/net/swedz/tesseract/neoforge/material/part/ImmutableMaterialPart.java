package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ImmutableMaterialPart extends MaterialPart
{
	public ImmutableMaterialPart(ResourceLocation id, String englishName)
	{
		super(id, englishName);
	}
	
	@Override
	public MaterialPart asBlock()
	{
		return this.copy().asBlock();
	}
	
	@Override
	public MaterialPart formatting(MaterialPartFormatter idFormatter, MaterialPartFormatter englishNameFormatter)
	{
		return this.copy().formatting(idFormatter, englishNameFormatter);
	}
	
	@Override
	public MaterialPart formattingDefault()
	{
		return this.copy().formattingDefault();
	}
	
	@Override
	public MaterialPart formattingMaterialOnly(MaterialPartFormatter.OnlyMaterial idFormatter, MaterialPartFormatter.OnlyMaterial englishNameFormatter)
	{
		return this.copy().formattingMaterialOnly(idFormatter, englishNameFormatter);
	}
	
	@Override
	public MaterialPart formattingMaterialOnly()
	{
		return this.copy().formattingMaterialOnly();
	}
	
	@Override
	public MaterialPart item(MaterialPartExtraRegister<ItemHolder<? extends Item>> action)
	{
		return this.copy().item(action);
	}
	
	@Override
	public MaterialPart itemProperty(Consumer<Item.Properties> action)
	{
		return this.copy().itemProperty(action);
	}
	
	@Override
	public MaterialPart itemTag(Collection<TagKey<Item>> tags)
	{
		return this.copy().itemTag(tags);
	}
	
	@Override
	public MaterialPart itemTag(TagKey<Item> tag)
	{
		return this.copy().itemTag(tag);
	}
	
	@Override
	public MaterialPart itemModel(Function<ItemHolder<? extends Item>, Consumer<ItemModelBuilder>> modelBuilder)
	{
		return this.copy().itemModel(modelBuilder);
	}
	
	@Override
	public MaterialPart block(MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>> action)
	{
		return this.copy().block(action);
	}
	
	@Override
	public MaterialPart blockProperty(Consumer<BlockBehaviour.Properties> action)
	{
		return this.copy().blockProperty(action);
	}
	
	@Override
	public MaterialPart blockTag(Collection<TagKey<Block>> tags)
	{
		return this.copy().blockTag(tags);
	}
	
	@Override
	public MaterialPart blockTag(TagKey<Block> tag)
	{
		return this.copy().blockTag(tag);
	}
	
	@Override
	public MaterialPart blockModel(Function<BlockHolder<Block>, Consumer<BlockStateProvider>> modelBuilder)
	{
		return this.copy().blockModel(modelBuilder);
	}
	
	@Override
	public MaterialPart blockLoot(Function<BlockHolder<Block>, Function<BlockLootSubProvider, LootTable.Builder>> lootBuilder)
	{
		return this.copy().blockLoot(lootBuilder);
	}
	
	@Override
	public <T> MaterialPart set(MaterialProperty<T> property, T value)
	{
		return this.copy().set(property, value);
	}
	
	@Override
	public <T> MaterialPart setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		return this.copy().setOptional(property, value);
	}
}
