package net.swedz.tesseract.neoforge.helper.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

public final class InputOutputDirectionalBlockCapabilityCache<T>
{
	private final BlockCapability<T, Direction> capability;
	
	private BlockCapabilityCache<T, Direction> input;
	private BlockCapabilityCache<T, Direction> output;
	
	public InputOutputDirectionalBlockCapabilityCache(BlockCapability<T, Direction> capability)
	{
		this.capability = capability;
	}
	
	public BlockCapability<T, Direction> capability()
	{
		return capability;
	}
	
	private BlockCapabilityCache<T, Direction> update(BlockCapabilityCache<T, Direction> cache, Level level, BlockPos pos, Direction direction)
	{
		boolean updateCache = cache == null || cache.context() != direction.getOpposite();
		
		if(updateCache)
		{
			cache = BlockCapabilityCache.create(
					capability, (ServerLevel) level, pos.relative(direction),
					direction.getOpposite()
			);
		}
		
		return cache;
	}
	
	public T input(Level level, BlockPos pos, Direction direction)
	{
		input = this.update(input, level, pos, direction);
		return input.getCapability();
	}
	
	public T output(Level level, BlockPos pos, Direction direction)
	{
		output = this.update(output, level, pos, direction);
		return output.getCapability();
	}
}
