package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.util.TransferHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * This is a copy of the auto extract/insert methods in {@link aztech.modern_industrialization.inventory.MIInventory}.
 * It saves on having to have duplicate code when you are using {@link IItemHandler}s and/or {@link IFluidHandler}s
 * instead of {@link aztech.modern_industrialization.inventory.MIItemStorage}s and
 * {@link aztech.modern_industrialization.inventory.MIFluidStorage}s while still caching the output direction and
 * making use of the very handy {@link TransferHelper} methods.
 */
public final class TransferCache
{
	private final Optional<Supplier<IItemHandler>>  itemHandler;
	private final Optional<Supplier<IFluidHandler>> fluidHandler;
	
	private BlockCapabilityCache<IItemHandler, Direction> outputCache;
	
	private TransferCache(Optional<Supplier<IItemHandler>> itemHandler,
						  Optional<Supplier<IFluidHandler>> fluidHandler)
	{
		Objects.requireNonNull(itemHandler);
		Objects.requireNonNull(fluidHandler);
		this.itemHandler = itemHandler;
		this.fluidHandler = fluidHandler;
	}
	
	public static TransferCache of(Supplier<IItemHandler> itemHandler,
								   Supplier<IFluidHandler> fluidHandler)
	{
		return new TransferCache(Optional.of(itemHandler), Optional.of(fluidHandler));
	}
	
	public static TransferCache ofItem(Supplier<IItemHandler> itemHandler)
	{
		return new TransferCache(Optional.of(itemHandler), Optional.empty());
	}
	
	public static TransferCache ofFluid(Supplier<IFluidHandler> fluidHandler)
	{
		return new TransferCache(Optional.empty(), Optional.of(fluidHandler));
	}
	
	public boolean hasItemHandler()
	{
		return itemHandler.isPresent();
	}
	
	public IItemHandler itemHandler()
	{
		return itemHandler.orElseThrow().get();
	}
	
	public boolean hasFluidHandler()
	{
		return fluidHandler.isPresent();
	}
	
	public IFluidHandler fluidHandler()
	{
		return fluidHandler.orElseThrow().get();
	}
	
	public void autoExtractItems(Level world, BlockPos pos, Direction direction)
	{
		if(!this.hasItemHandler())
		{
			return;
		}
		
		boolean updateCache = outputCache == null || outputCache.context() != direction.getOpposite();
		
		if(updateCache)
		{
			outputCache = BlockCapabilityCache.create(
					Capabilities.ItemHandler.BLOCK, (ServerLevel) world, pos.relative(direction),
					direction.getOpposite()
			);
		}
		
		IItemHandler target = outputCache.getCapability();
		if(target != null)
		{
			TransferHelper.moveAll(this.itemHandler(), target, true);
		}
	}
	
	public void autoExtractFluids(Level world, BlockPos pos, Direction direction)
	{
		if(!this.hasFluidHandler())
		{
			return;
		}
		
		IFluidHandler target = world.getCapability(Capabilities.FluidHandler.BLOCK, pos.relative(direction), direction.getOpposite());
		if(target != null)
		{
			FluidUtil.tryFluidTransfer(target, this.fluidHandler(), Integer.MAX_VALUE, true);
		}
	}
	
	public void autoInsertItems(Level world, BlockPos pos, Direction direction)
	{
		if(!this.hasItemHandler())
		{
			return;
		}
		
		IItemHandler target = world.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction), direction.getOpposite());
		if(target != null)
		{
			TransferHelper.moveAll(target, this.itemHandler(), false);
		}
	}
	
	public void autoInsertFluids(Level world, BlockPos pos, Direction direction)
	{
		if(!this.hasFluidHandler())
		{
			return;
		}
		
		IFluidHandler target = world.getCapability(Capabilities.FluidHandler.BLOCK, pos.relative(direction), direction.getOpposite());
		if(target != null)
		{
			FluidUtil.tryFluidTransfer(this.fluidHandler(), target, Integer.MAX_VALUE, true);
		}
	}
}
