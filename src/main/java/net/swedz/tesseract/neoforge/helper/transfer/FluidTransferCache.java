package net.swedz.tesseract.neoforge.helper.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.function.Supplier;

public class FluidTransferCache extends TransferCache<IFluidHandler>
{
	public FluidTransferCache(BlockCapability<IFluidHandler, Direction> capability, Supplier<IFluidHandler> sourceHandler)
	{
		super(capability, sourceHandler);
	}
	
	public FluidTransferCache(Supplier<IFluidHandler> sourceHandler)
	{
		this(Capabilities.FluidHandler.BLOCK, sourceHandler);
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		IFluidHandler target = cache.output(level, pos, direction);
		if(target != null)
		{
			return !FluidUtil.tryFluidTransfer(target, this.sourceHandler(), Integer.MAX_VALUE, true).isEmpty();
		}
		return false;
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		IFluidHandler target = cache.input(level, pos, direction);
		if(target != null)
		{
			return !FluidUtil.tryFluidTransfer(this.sourceHandler(), target, Integer.MAX_VALUE, true).isEmpty();
		}
		return false;
	}
}
