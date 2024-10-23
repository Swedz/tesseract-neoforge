package net.swedz.tesseract.neoforge.material.part;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.swedz.tesseract.neoforge.helper.TagHelper;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public final class MaterialPart implements MaterialPropertyHolder.Mutable
{
	private final ResourceLocation id;
	private final String           englishName;
	
	private boolean isBlock = false;
	
	private MaterialPartFormatter idFormatter;
	private MaterialPartFormatter englishNameFormatter;
	
	private MaterialPartBlockFactory blockFactory = MaterialPartBlockFactory.of((c, bp) -> new Block(bp), (c, b, ip) -> new BlockItem(b, ip));
	private MaterialPartItemFactory  itemFactory  = Item::new;
	
	private final List<MaterialPartAction<ItemHolder<? extends Item>>>            itemActions  = Lists.newArrayList();
	private final List<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockActions = Lists.newArrayList();
	
	private final MaterialPropertyMap propertyOverrides = new MaterialPropertyMap();
	
	private final List<MaterialPartAction<ItemHolder<? extends Item>>>            itemAfterActions  = Lists.newArrayList();
	private final List<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockAfterActions = Lists.newArrayList();
	
	public MaterialPart(ResourceLocation id, String englishName)
	{
		this.id = id;
		this.englishName = englishName;
		this.formattingDefault();
	}
	
	private MaterialPart copy(MaterialPartFactory creator)
	{
		MaterialPart copy = creator.create(this.id, this.englishName);
		copy.isBlock = this.isBlock;
		copy.idFormatter = this.idFormatter;
		copy.englishNameFormatter = this.englishNameFormatter;
		copy.itemActions.addAll(this.itemActions);
		copy.blockActions.addAll(this.blockActions);
		copy.propertyOverrides.putAll(this.propertyOverrides);
		copy.itemAfterActions.addAll(this.itemAfterActions);
		copy.blockAfterActions.addAll(this.blockAfterActions);
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
		MaterialPart copy = this.copy();
		copy.isBlock = true;
		return copy;
	}
	
	public MaterialPart formatting(MaterialPartFormatter idFormatter, MaterialPartFormatter englishNameFormatter)
	{
		MaterialPart copy = this.copy();
		copy.idFormatter = idFormatter;
		copy.englishNameFormatter = englishNameFormatter;
		return copy;
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
	
	public MaterialPart blockFactory(MaterialPartBlockFactory blockFactory)
	{
		MaterialPart copy = this.copy();
		copy.blockFactory = blockFactory;
		return copy;
	}
	
	public MaterialPart blockFactory(MaterialPartBlockBlockFactory block,
									 MaterialPartBlockItemFactory item)
	{
		return this.blockFactory(MaterialPartBlockFactory.of(block, item));
	}
	
	public MaterialPart itemFactory(MaterialPartItemFactory itemFactory)
	{
		MaterialPart copy = this.copy();
		copy.itemFactory = itemFactory;
		return copy;
	}
	
	public MaterialPart item(MaterialPartAction<ItemHolder<? extends Item>> action)
	{
		MaterialPart copy = this.copy();
		copy.itemActions.add(action);
		return copy;
	}
	
	public MaterialPart itemProperty(Consumer<Item.Properties> action)
	{
		return this.item((c, h) -> h.withProperties(action));
	}
	
	public MaterialPart itemTag(Collection<TagKey<Item>> tags)
	{
		return this.item((c, h) -> h.tag(tags));
	}
	
	@SuppressWarnings("unchecked")
	public MaterialPart itemTag(TagKey<Item>... tags)
	{
		return this.itemTag(Arrays.asList(tags));
	}
	
	public MaterialPart itemModel(Function<ItemHolder<? extends Item>, Consumer<ItemModelBuilder>> modelBuilder)
	{
		return this.item((c, h) -> h.withModel(modelBuilder::apply));
	}
	
	public MaterialPart block(MaterialPartAction<BlockWithItemHolder<Block, BlockItem>> action)
	{
		MaterialPart copy = this.asBlock();
		copy.blockActions.add(action);
		return copy;
	}
	
	public MaterialPart blockProperty(Consumer<BlockBehaviour.Properties> action)
	{
		return this.block((c, h) -> h.withProperties(action));
	}
	
	public MaterialPart blockTag(Collection<TagKey<Block>> tags)
	{
		return this.block((c, h) -> h.tag(tags));
	}
	
	@SuppressWarnings("unchecked")
	public MaterialPart blockTag(TagKey<Block>... tag)
	{
		return this.blockTag(Arrays.asList(tag));
	}
	
	public MaterialPart blockModel(Function<BlockHolder<Block>, Consumer<BlockStateProvider>> modelBuilder)
	{
		return this.block((c, h) -> h.withModel(modelBuilder));
	}
	
	public MaterialPart blockLoot(Function<BlockHolder<Block>, Function<BlockLootSubProvider, LootTable.Builder>> lootBuilder)
	{
		return this.block((c, h) -> h.withLootTable(lootBuilder));
	}
	
	public MaterialPart itemAndBlockTag(Collection<TagKey<Item>> tags)
	{
		MaterialPart copy = this.itemTag(tags);
		copy = copy.blockTag(tags.stream().map((t) -> TagHelper.convert(t, BuiltInRegistries.BLOCK)).toList());
		return copy;
	}
	
	@SuppressWarnings("unchecked")
	public MaterialPart itemAndBlockTag(TagKey<Item>... tags)
	{
		return this.itemAndBlockTag(Arrays.asList(tags));
	}
	
	public MaterialPropertyMap properties()
	{
		return propertyOverrides.copy();
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return propertyOverrides.has(property);
	}
	
	@Override
	public <T> MaterialPart set(MaterialProperty<T> property, T value)
	{
		MaterialPart copy = this.copy();
		copy.propertyOverrides.set(property, value);
		return copy;
	}
	
	@Override
	public <T> MaterialPart setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		MaterialPart copy = this.copy();
		copy.propertyOverrides.setOptional(property, value);
		return copy;
	}
	
	@Override
	public <T> T get(MaterialProperty<T> property)
	{
		return propertyOverrides.get(property);
	}
	
	public MaterialPart itemAfter(MaterialPartAction<ItemHolder<? extends Item>> action)
	{
		MaterialPart copy = this.copy();
		copy.itemAfterActions.add(action);
		return copy;
	}
	
	public MaterialPart blockAfter(MaterialPartAction<BlockWithItemHolder<Block, BlockItem>> action)
	{
		MaterialPart copy = this.asBlock();
		copy.blockAfterActions.add(action);
		return copy;
	}
	
	public RegisteredMaterialPart register(MaterialRegistry registry, Material material)
	{
		ResourceLocation id = registry.id(this.formatId(material));
		String englishName = this.formatEnglishName(material);
		
		MaterialPartRegisterContext context = new MaterialPartRegisterContext(registry, material, this);
		
		ItemHolder<?> item;
		RegisteredMaterialPart registered;
		if(isBlock)
		{
			BlockWithItemHolder<Block, BlockItem> block = new BlockWithItemHolder<>(
					id, englishName,
					registry.blockRegistry(), (p) -> blockFactory.createBlock(context, p),
					registry.itemRegistry(), (b, p) -> blockFactory.createItem(context, b, p)
			);
			item = block.item();
			registered = RegisteredMaterialPart.existingBlock(block);
			
			blockActions.forEach((a) -> a.apply(context, block));
			context.properties().apply(block);
			
			block.register();
			blockAfterActions.forEach((a) -> a.apply(context, block));
			registry.onBlockRegister(block);
		}
		else
		{
			item = new ItemHolder<>(id, englishName, registry.itemRegistry(), (p) -> itemFactory.create(p));
			registered = RegisteredMaterialPart.existingItem(item);
		}
		
		itemActions.forEach((a) -> a.apply(context, item));
		context.properties().apply(item);
		
		item.register();
		registry.onItemRegister(item);
		itemAfterActions.forEach((a) -> a.apply(context, item));
		
		return registered;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof MaterialPart other && id.equals(other.id());
	}
	
	@Override
	public int hashCode()
	{
		return id.hashCode();
	}
}
