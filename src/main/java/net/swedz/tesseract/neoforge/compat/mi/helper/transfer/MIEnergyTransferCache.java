package net.swedz.tesseract.neoforge.compat.mi.helper.transfer;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import com.mojang.logging.LogUtils;
import dev.technici4n.grandpower.api.EnergyStorageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.swedz.tesseract.neoforge.helper.transfer.TransferCache;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class MIEnergyTransferCache extends TransferCache<MIEnergyStorage>
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public MIEnergyTransferCache(BlockCapability<MIEnergyStorage, Direction> capability, Supplier<MIEnergyStorage> sourceHandler)
	{
		super(capability, sourceHandler);
	}
	
	public MIEnergyTransferCache(Supplier<MIEnergyStorage> sourceHandler)
	{
		this(EnergyApi.SIDED, sourceHandler);
	}
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, CableTier cableTier, long maxAmount)
	{
		MIEnergyStorage target = cache.output(level, pos, direction);
		if(target != null && (cableTier == null || target.canConnect(cableTier)))
		{
			return EnergyStorageUtil.move(this.sourceHandler(), target, maxAmount) > 0;
		}
		return false;
	}
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, CableTier cableTier)
	{
		return this.autoExtract(level, pos, direction, cableTier, Long.MAX_VALUE);
	}
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, long maxAmount)
	{
		return this.autoExtract(level, pos, direction, null, maxAmount);
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		return this.autoExtract(level, pos, direction, null, Long.MAX_VALUE);
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, CableTier cableTier, long maxAmount)
	{
		MIEnergyStorage target = cache.input(level, pos, direction);
		if(target != null && target.canConnect(cableTier))
		{
			return EnergyStorageUtil.move(target, this.sourceHandler(), maxAmount) > 0;
		}
		return false;
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, CableTier cableTier)
	{
		return this.autoInsert(level, pos, direction, cableTier, Long.MAX_VALUE);
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, long maxAmount)
	{
		return this.autoInsert(level, pos, direction, null, maxAmount);
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		return this.autoInsert(level, pos, direction, null, Long.MAX_VALUE);
	}
}
