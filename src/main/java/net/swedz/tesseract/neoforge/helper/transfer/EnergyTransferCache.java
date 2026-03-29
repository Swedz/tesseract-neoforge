package net.swedz.tesseract.neoforge.helper.transfer;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.swedz.tesseract.neoforge.helper.TransferHelper;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class EnergyTransferCache extends TransferCache<EnergyHandler>
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public EnergyTransferCache(BlockCapability<EnergyHandler, Direction> capability, Supplier<EnergyHandler> sourceHandler)
	{
		super(capability, sourceHandler);
	}
	
	public EnergyTransferCache(Supplier<EnergyHandler> sourceHandler)
	{
		this(Capabilities.Energy.BLOCK, sourceHandler);
	}
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, int maxAmount)
	{
		var target = cache.output(level, pos, direction);
		if(target != null)
		{
			return TransferHelper.move(IEnergyStorage.of(this.sourceHandler()), IEnergyStorage.of(target), maxAmount) > 0;
		}
		return false;
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		return this.autoExtract(level, pos, direction, Integer.MAX_VALUE);
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, int maxAmount)
	{
		var target = cache.input(level, pos, direction);
		if(target != null)
		{
			return TransferHelper.move(IEnergyStorage.of(target), IEnergyStorage.of(this.sourceHandler()), maxAmount) > 0;
		}
		return false;
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		return this.autoInsert(level, pos, direction, Integer.MAX_VALUE);
	}
}
