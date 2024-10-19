package net.swedz.tesseract.neoforge.material.part;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.material.Material;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;
import net.swedz.tesseract.neoforge.registry.RegisteredObjectHolder;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

import static net.swedz.tesseract.neoforge.material.property.MaterialProperties.*;

public abstract class MaterialPart<H extends RegisteredObjectHolder>
{
	protected final String id, englishName;
	
	protected final Formatter formatterId, formatterEnglishName;
	
	public MaterialPart(String id, String englishName,
						Formatter formatterId, Formatter formatterEnglishName)
	{
		this.id = id;
		this.englishName = englishName;
		this.formatterId = formatterId;
		this.formatterEnglishName = formatterEnglishName;
	}
	
	public MaterialPart(MaterialPart inherit)
	{
		this(inherit.id, inherit.englishName, inherit.formatterId, inherit.formatterEnglishName);
	}
	
	public String id()
	{
		return id;
	}
	
	public String id(Material material)
	{
		return formatterId.format(material.id(), id);
	}
	
	protected String englishName()
	{
		return englishName;
	}
	
	protected String englishName(Material material)
	{
		return formatterEnglishName.format(material.englishName(), englishName);
	}
	
	public MaterialPropertyMap getPropertyOverrides()
	{
		return new MaterialPropertyMap();
	}
	
	protected final MaterialPropertyMap getPropertyOverrides(Material material)
	{
		MaterialPropertyMap properties = material.properties().copy();
		properties.putAll(this.getPropertyOverrides());
		return properties;
	}
	
	public abstract H register(MaterialRegistry registry, Material material);
	
	public final MaterialPart<H> withoutSuffix()
	{
		return new MaterialPart<>(id, englishName, (m, p) -> m, (m, p) -> m)
		{
			@Override
			public H register(MaterialRegistry registry, Material material)
			{
				return MaterialPart.this.register(registry, material);
			}
		};
	}
	
	public final MaterialPart<H> with(ExtraRegister<H> action)
	{
		return new MaterialPart<>(this)
		{
			@Override
			public H register(MaterialRegistry registry, Material material)
			{
				H holder = MaterialPart.this.register(registry, material);
				action.accept(registry, material, holder);
				return holder;
			}
		};
	}
	
	public interface ExtraRegister<H extends RegisteredObjectHolder>
	{
		void accept(MaterialRegistry registry, Material material, H holder);
	}
	
	public final MaterialPart<H> withProperties(PropertyOverridesRegister<H> action)
	{
		return new MaterialPart<>(this)
		{
			@Override
			public MaterialPropertyMap getPropertyOverrides()
			{
				MaterialPropertyMap overrides = super.getPropertyOverrides();
				overrides.putAll(action.get());
				return overrides;
			}
			
			@Override
			public H register(MaterialRegistry registry, Material material)
			{
				return MaterialPart.this.register(registry, material);
			}
		};
	}
	
	public interface PropertyOverridesRegister<H extends RegisteredObjectHolder>
	{
		MaterialPropertyMap get();
	}
	
	public final <T> MaterialPart<H> withProperty(MaterialProperty<T> property, T value)
	{
		return this.withProperties(() -> new MaterialPropertyMap().set(property, value));
	}
	
	public static MaterialPart<ItemHolder<Item>> item(String id, String englishName, Formatter formatterId, Formatter formatterEnglishName)
	{
		return new MaterialPart<>(id, englishName, formatterId, formatterEnglishName)
		{
			@Override
			public ItemHolder<Item> register(MaterialRegistry registry, Material material)
			{
				ResourceLocation itemId = registry.id(this.id(material));
				String itemName = this.englishName(material);
				
				var holder = new ItemHolder<>(itemId, itemName, registry.itemRegistry(), Item::new);
				
				MaterialPropertyMap properties = this.getPropertyOverrides(material);
				properties.applyItemTags(holder);
				
				registry.onItemRegister(holder);
				
				return holder;
			}
		};
	}
	
	public static MaterialPart<ItemHolder<Item>> item(String id, String englishName)
	{
		return item(id, englishName, "%s_%s"::formatted, "%s %s"::formatted);
	}
	
	public static MaterialPart<BlockWithItemHolder<Block, BlockItem>> block(String id, String englishName, Formatter formatterId, Formatter formatterEnglishName)
	{
		return new MaterialPart<>(id, englishName, formatterId, formatterEnglishName)
		{
			@Override
			public BlockWithItemHolder<Block, BlockItem> register(MaterialRegistry registry, Material material)
			{
				ResourceLocation itemId = registry.id(this.id(material));
				String itemName = this.englishName(material);
				
				var holder = new BlockWithItemHolder<>(
						itemId, itemName,
						registry.blockRegistry(),
						(p) ->
						{
							if(material.get(REQUIRES_CORRECT_TOOL_FOR_DROPS))
							{
								p = p.requiresCorrectToolForDrops();
							}
							return new Block(p
									.explosionResistance(material.get(BLAST_RESISTANCE))
									.destroyTime(material.get(HARDNESS)));
						},
						registry.itemRegistry(),
						BlockItem::new
				);
				
				MaterialPropertyMap properties = this.getPropertyOverrides(material);
				properties.applyBlockTags(holder);
				properties.applyItemTags(holder.item());
				
				registry.onBlockRegister(holder);
				
				return holder;
			}
		};
	}
	
	public static MaterialPart<BlockWithItemHolder<Block, BlockItem>> block(String id, String englishName)
	{
		return block(id, englishName, "%s_%s"::formatted, "%s %s"::formatted);
	}
	
	public interface Formatter
	{
		String format(String material, String part);
	}
}
