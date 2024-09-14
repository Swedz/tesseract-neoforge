package net.swedz.tesseract.neoforge.helper.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.swedz.tesseract.neoforge.helper.TransferHelper;

import java.util.function.Supplier;

public class ItemTransferCache extends TransferCache<IItemHandler>
{
	public ItemTransferCache(BlockCapability<IItemHandler, Direction> capability, Supplier<IItemHandler> sourceHandler)
	{
		super(capability, sourceHandler);
	}
	
	public ItemTransferCache(Supplier<IItemHandler> sourceHandler)
	{
		this(Capabilities.ItemHandler.BLOCK, sourceHandler);
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		IItemHandler target = cache.output(level, pos, direction);
		if(target != null)
		{
			return !TransferHelper.moveAll(this.sourceHandler(), target, true).isEmpty();
		}
		return false;
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		IItemHandler target = cache.input(level, pos, direction);
		if(target != null)
		{
			return !TransferHelper.moveAll(target, this.sourceHandler(), true).isEmpty();
		}
		return false;
	}
}
