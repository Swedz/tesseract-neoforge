package net.swedz.tesseract.neoforge.compat.mi.helper.transfer;

import com.mojang.logging.LogUtils;
import dev.technici4n.grandpower.api.EnergyStorageUtil;
import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.swedz.tesseract.neoforge.helper.transfer.TransferCache;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class LongEnergyTransferCache extends TransferCache<ILongEnergyStorage>
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public LongEnergyTransferCache(BlockCapability<ILongEnergyStorage, Direction> capability, Supplier<ILongEnergyStorage> sourceHandler)
	{
		super(capability, sourceHandler);
	}
	
	public LongEnergyTransferCache(Supplier<ILongEnergyStorage> sourceHandler)
	{
		this(ILongEnergyStorage.BLOCK, sourceHandler);
	}
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, long maxAmount)
	{
		ILongEnergyStorage target = cache.output(level, pos, direction);
		if(target != null)
		{
			return EnergyStorageUtil.move(this.sourceHandler(), target, maxAmount) > 0;
		}
		return false;
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		return this.autoExtract(level, pos, direction, Long.MAX_VALUE);
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, long maxAmount)
	{
		ILongEnergyStorage target = cache.input(level, pos, direction);
		if(target != null)
		{
			return EnergyStorageUtil.move(target, this.sourceHandler(), maxAmount) > 0;
		}
		return false;
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		return this.autoInsert(level, pos, direction, Long.MAX_VALUE);
	}
}
