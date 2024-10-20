package net.swedz.tesseract.neoforge.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.compat.mi.material.part.MIMaterialParts;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.registry.RegisteredObjectHolder;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

public abstract class MaterialRegistry
{
	public abstract String modId();
	
	public final ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(this.modId(), path);
	}
	
	public abstract DeferredRegister.Blocks blockRegistry();
	
	public abstract DeferredRegister<BlockEntityType<?>> blockEntityRegistry();
	
	public abstract DeferredRegister.Items itemRegistry();
	
	public abstract void onBlockRegister(BlockHolder holder);
	
	public abstract void onBlockEntityRegister(BlockEntityType<?> type);
	
	public abstract void onItemRegister(ItemHolder holder);
	
	public final <H extends RegisteredObjectHolder> H create(Material material, MaterialPart<H> part)
	{
		H holder = part.register(this, material);
		/*
		 * TODO this cannot be added like this because .add() expects the item/block to already be registered..........
		 */
		//material.add(part);
		return holder;
	}
}
