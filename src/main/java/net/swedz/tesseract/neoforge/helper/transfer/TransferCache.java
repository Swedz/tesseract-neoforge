package net.swedz.tesseract.neoforge.helper.transfer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class TransferCache<H>
{
	protected final Supplier<H> sourceHandler;
	
	protected final InputOutputDirectionalBlockCapabilityCache<H> cache;
	
	protected TransferCache(BlockCapability<H, Direction> capability, Supplier<H> sourceHandler)
	{
		Objects.requireNonNull(sourceHandler);
		this.sourceHandler = sourceHandler;
		this.cache = new InputOutputDirectionalBlockCapabilityCache<>(capability);
	}
	
	public H sourceHandler()
	{
		return sourceHandler.get();
	}
	
	public abstract boolean autoExtract(Level level, BlockPos pos, Direction direction);
	
	public abstract boolean autoInsert(Level level, BlockPos pos, Direction direction);
}
