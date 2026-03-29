package net.swedz.tesseract.neoforge.helper.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.swedz.tesseract.neoforge.helper.TransferHelper;

import java.util.function.Supplier;

public class ItemTransferCache extends TransferCache<ResourceHandler<ItemResource>>
{
	public ItemTransferCache(BlockCapability<ResourceHandler<ItemResource>, Direction> capability, Supplier<ResourceHandler<ItemResource>> sourceHandler)
	{
		super(capability, sourceHandler);
	}
	
	public ItemTransferCache(Supplier<ResourceHandler<ItemResource>> sourceHandler)
	{
		this(Capabilities.Item.BLOCK, sourceHandler);
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		var target = cache.output(level, pos, direction);
		if(target != null)
		{
			return !TransferHelper.moveAll(IItemHandler.of(this.sourceHandler()), IItemHandler.of(target), true).isEmpty();
		}
		return false;
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		var target = cache.input(level, pos, direction);
		if(target != null)
		{
			return !TransferHelper.moveAll(IItemHandler.of(target), IItemHandler.of(this.sourceHandler()), true).isEmpty();
		}
		return false;
	}
}
