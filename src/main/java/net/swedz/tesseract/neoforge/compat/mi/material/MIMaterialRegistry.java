package net.swedz.tesseract.neoforge.compat.mi.material;

import aztech.modern_industrialization.MI;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.material.MaterialRegistry;
import net.swedz.tesseract.neoforge.registry.holder.BlockHolder;
import net.swedz.tesseract.neoforge.registry.holder.ItemHolder;

public final class MIMaterialRegistry extends MaterialRegistry
{
	public static MaterialRegistry get()
	{
		return new MIMaterialRegistry();
	}
	
	@Override
	public String modId()
	{
		return MI.ID;
	}
	
	@Override
	public DeferredRegister.Blocks blockRegistry()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DeferredRegister<BlockEntityType<?>> blockEntityRegistry()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public DeferredRegister.Items itemRegistry()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void onBlockRegister(BlockHolder holder)
	{
	}
	
	@Override
	public void onBlockEntityRegister(BlockEntityType<?> type)
	{
	}
	
	@Override
	public void onItemRegister(ItemHolder holder)
	{
	}
}
