package net.swedz.tesseract.neoforge.helper.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.swedz.tesseract.neoforge.helper.TransferHelper;

import java.util.function.Supplier;

public class FluidTransferCache extends TransferCache<ResourceHandler<FluidResource>>
{
	public FluidTransferCache(BlockCapability<ResourceHandler<FluidResource>, Direction> capability, Supplier<ResourceHandler<FluidResource>> sourceHandler)
	{
		super(capability, sourceHandler);
	}
	
	public FluidTransferCache(Supplier<ResourceHandler<FluidResource>> sourceHandler)
	{
		this(Capabilities.Fluid.BLOCK, sourceHandler);
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		var target = cache.output(level, pos, direction);
		if(target != null)
		{
			return !TransferHelper.tryFluidTransfer(IFluidHandler.of(this.sourceHandler()), IFluidHandler.of(target), Integer.MAX_VALUE, false).isEmpty();
		}
		return false;
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		var target = cache.input(level, pos, direction);
		if(target != null)
		{
			return !TransferHelper.tryFluidTransfer(IFluidHandler.of(target), IFluidHandler.of(this.sourceHandler()), Integer.MAX_VALUE, false).isEmpty();
		}
		return false;
	}
}
