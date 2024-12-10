package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import java.util.Collection;

public final class MaterialPartRegisterContext implements MaterialPropertyHolder, MaterialPartHolder
{
	private final ResourceLocation id;
	private final String           englishName;
	
	private final MaterialRegistry    registry;
	private final Material            material;
	private final MaterialPart        part;
	private final MaterialPropertyMap properties;
	
	private final MaterialPartBlockFactory blockFactory;
	private final MaterialPartItemFactory  itemFactory;
	
	private final Collection<MaterialPartAction<ItemHolder<? extends Item>>>            itemActions;
	private final Collection<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockActions;
	private final Collection<MaterialPartAction<ItemHolder<? extends Item>>>            itemAfterActions;
	private final Collection<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockAfterActions;
	
	public MaterialPartRegisterContext(
			ResourceLocation id, String englishName,
			
			MaterialRegistry registry, Material material, MaterialPart part, MaterialPropertyMap properties,
			
			MaterialPartBlockFactory blockFactory,
			MaterialPartItemFactory itemFactory,
			
			Collection<MaterialPartAction<ItemHolder<? extends Item>>> itemActions,
			Collection<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockActions,
			Collection<MaterialPartAction<ItemHolder<? extends Item>>> itemAfterActions,
			Collection<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockAfterActions
	)
	{
		this.id = id;
		this.englishName = englishName;
		
		this.registry = registry;
		this.material = material;
		this.part = part;
		this.properties = properties;
		
		this.blockFactory = blockFactory;
		this.itemFactory = itemFactory;
		
		this.itemActions = itemActions;
		this.blockActions = blockActions;
		this.itemAfterActions = itemAfterActions;
		this.blockAfterActions = blockAfterActions;
	}
	
	public MaterialPartRegisterContext(ResourceLocation id, String englishName,
									   
									   MaterialRegistry registry, Material material, MaterialPart part,
									   
									   MaterialPartBlockFactory blockFactory,
									   MaterialPartItemFactory itemFactory,
									   
									   Collection<MaterialPartAction<ItemHolder<? extends Item>>> itemActions,
									   Collection<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockActions,
									   Collection<MaterialPartAction<ItemHolder<? extends Item>>> itemAfterActions,
									   Collection<MaterialPartAction<BlockWithItemHolder<Block, BlockItem>>> blockAfterActions)
	{
		this(
				id, englishName,
				registry, material, part, material.properties(part),
				blockFactory, itemFactory,
				itemActions, blockActions, itemAfterActions, blockAfterActions
		);
	}
	
	public ResourceLocation id()
	{
		return id;
	}
	
	public String englishName()
	{
		return englishName;
	}
	
	public MaterialRegistry registry()
	{
		return registry;
	}
	
	public Material material()
	{
		return material;
	}
	
	public MaterialPart part()
	{
		return part;
	}
	
	public MaterialPropertyMap properties()
	{
		return properties;
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return properties.has(property);
	}
	
	@Override
	public <T> T get(MaterialProperty<T> property)
	{
		return properties.get(property);
	}
	
	@Override
	public boolean has(MaterialPart part)
	{
		return material.has(part);
	}
	
	@Override
	public RegisteredMaterialPart get(MaterialPart part)
	{
		return material.get(part);
	}
	
	public MaterialPartBlockFactory blockFactory()
	{
		return blockFactory;
	}
	
	public MaterialPartItemFactory itemFactory()
	{
		return itemFactory;
	}
	
	public void register(BlockWithItemHolder<Block, BlockItem> block)
	{
		ItemHolder<?> item = block.item();
		
		properties.apply(block);
		properties.apply(item);
		blockActions.forEach((a) -> a.apply(this, block));
		itemActions.forEach((a) -> a.apply(this, item));
		
		block.register();
		blockAfterActions.forEach((a) -> a.apply(this, block));
		itemAfterActions.forEach((a) -> a.apply(this, item));
		registry.onBlockRegister(block);
		registry.onItemRegister(item);
	}
	
	public void register(BlockEntityType<?> blockEntity)
	{
		registry.onBlockEntityRegister(blockEntity);
	}
	
	public void register(ItemHolder<? extends Item> item)
	{
		properties.apply(item);
		itemActions.forEach((a) -> a.apply(this, item));
		
		item.register();
		itemAfterActions.forEach((a) -> a.apply(this, item));
		registry.onItemRegister(item);
	}
}
