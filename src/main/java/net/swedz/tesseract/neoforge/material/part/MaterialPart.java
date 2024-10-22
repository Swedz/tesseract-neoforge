package net.swedz.tesseract.neoforge.material.part;

import com.google.common.collect.Lists;
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
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class MaterialPart implements MaterialPropertyHolder
{
	protected final ResourceLocation id;
	protected final String           englishName;
	
	protected boolean isBlock = false;
	
	protected MaterialPartFormatter idFormatter;
	protected MaterialPartFormatter englishNameFormatter;
	
	protected final List<MaterialPartExtraRegister<ItemHolder<? extends Item>>>            itemActions  = Lists.newArrayList();
	protected final List<MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>>> blockActions = Lists.newArrayList();
	
	protected final MaterialPropertyMap propertyOverrides = new MaterialPropertyMap();
	
	public MaterialPart(ResourceLocation id, String englishName)
	{
		this.id = id;
		this.englishName = englishName;
		this.formattingDefault();
	}
	
	protected <P extends MaterialPart> P copy(BiFunction<ResourceLocation, String, P> creator)
	{
		P copy = creator.apply(id, englishName);
		copy.isBlock = this.isBlock;
		copy.idFormatter = this.idFormatter;
		copy.englishNameFormatter = this.englishNameFormatter;
		copy.itemActions.addAll(this.itemActions);
		copy.blockActions.addAll(this.blockActions);
		copy.propertyOverrides.putAll(this.propertyOverrides);
		return copy;
	}
	
	public MaterialPart copy()
	{
		return this.copy(MaterialPart::new);
	}
	
	public MaterialPart as(String namespace)
	{
		return this.copy((id, name) -> new MaterialPart(ResourceLocation.fromNamespaceAndPath(namespace, id.getPath()), name));
	}
	
	public ImmutableMaterialPart immutable()
	{
		return this.copy(ImmutableMaterialPart::new);
	}
	
	public ResourceLocation id()
	{
		return id;
	}
	
	public String englishName()
	{
		return englishName;
	}
	
	public boolean isBlock()
	{
		return isBlock;
	}
	
	public MaterialPart asBlock()
	{
		isBlock = true;
		return this;
	}
	
	public MaterialPart formatting(MaterialPartFormatter idFormatter, MaterialPartFormatter englishNameFormatter)
	{
		this.idFormatter = idFormatter;
		this.englishNameFormatter = englishNameFormatter;
		return this;
	}
	
	public MaterialPart formattingDefault()
	{
		return this.formatting("%s_%s"::formatted, "%s %s"::formatted);
	}
	
	public MaterialPart formattingMaterialOnly(MaterialPartFormatter.OnlyMaterial idFormatter, MaterialPartFormatter.OnlyMaterial englishNameFormatter)
	{
		return this.formatting(idFormatter, englishNameFormatter);
	}
	
	public MaterialPart formattingMaterialOnly()
	{
		return this.formattingMaterialOnly((m) -> m, (m) -> m);
	}
	
	public String formatId(Material material)
	{
		return idFormatter.format(material.id().getPath(), this.id().getPath());
	}
	
	public String formatEnglishName(Material material)
	{
		return englishNameFormatter.format(material.englishName(), this.englishName());
	}
	
	public MaterialPart item(MaterialPartExtraRegister<ItemHolder<? extends Item>> action)
	{
		itemActions.add(action);
		return this;
	}
	
	public MaterialPart itemProperty(Consumer<Item.Properties> action)
	{
		return this.item((r, m, h) -> h.withProperties(action));
	}
	
	public MaterialPart itemTag(Collection<TagKey<Item>> tags)
	{
		return this.item((r, m, h) -> h.tag(tags));
	}
	
	public MaterialPart itemTag(TagKey<Item> tag)
	{
		return this.itemTag(List.of(tag));
	}
	
	public MaterialPart itemModel(Function<ItemHolder<? extends Item>, Consumer<ItemModelBuilder>> modelBuilder)
	{
		return this.item((r, m, h) -> h.withModel(modelBuilder::apply));
	}
	
	public MaterialPart block(MaterialPartExtraRegister<BlockWithItemHolder<Block, BlockItem>> action)
	{
		isBlock = true;
		blockActions.add(action);
		return this;
	}
	
	public MaterialPart blockProperty(Consumer<BlockBehaviour.Properties> action)
	{
		return this.block((r, m, h) -> h.withProperties(action));
	}
	
	public MaterialPart blockTag(Collection<TagKey<Block>> tags)
	{
		return this.block((r, m, h) -> h.tag(tags));
	}
	
	public MaterialPart blockTag(TagKey<Block> tag)
	{
		return this.blockTag(List.of(tag));
	}
	
	public MaterialPart blockModel(Function<BlockHolder<Block>, Consumer<BlockStateProvider>> modelBuilder)
	{
		return this.block((r, m, h) -> h.withModel(modelBuilder));
	}
	
	public MaterialPart blockLoot(Function<BlockHolder<Block>, Function<BlockLootSubProvider, LootTable.Builder>> lootBuilder)
	{
		return this.block((r, m, h) -> h.withLootTable(lootBuilder));
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return propertyOverrides.has(property);
	}
	
	@Override
	public <T> MaterialPart set(MaterialProperty<T> property, T value)
	{
		propertyOverrides.set(property, value);
		return this;
	}
	
	@Override
	public <T> MaterialPart setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		propertyOverrides.setOptional(property, value);
		return this;
	}
	
	@Override
	public <T> T get(MaterialProperty<T> property)
	{
		return propertyOverrides.get(property);
	}
	
	public RegisteredMaterialPart register(MaterialRegistry registry, Material material)
	{
		ResourceLocation id = registry.id(this.formatId(material));
		String englishName = this.formatEnglishName(material);
		
		ItemHolder<?> item;
		RegisteredMaterialPart registered;
		if(isBlock)
		{
			BlockWithItemHolder<Block, BlockItem> block = new BlockWithItemHolder<>(
					id, englishName,
					registry.blockRegistry(), Block::new,
					registry.itemRegistry(), BlockItem::new
			);
			item = block.item();
			registered = RegisteredMaterialPart.existingBlock(block);
			
			blockActions.forEach((a) -> a.apply(registry, material, block));
			registry.onBlockRegister(block);
			
			block.register();
		}
		else
		{
			item = new ItemHolder<>(id, englishName, registry.itemRegistry(), Item::new);
			registered = RegisteredMaterialPart.existingItem(item);
		}
		
		itemActions.forEach((a) -> a.apply(registry, material, item));
		registry.onItemRegister(item);
		
		item.register();
		
		return registered;
	}
}
