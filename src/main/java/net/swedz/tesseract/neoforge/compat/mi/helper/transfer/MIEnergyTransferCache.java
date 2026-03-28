package net.swedz.tesseract.neoforge.compat.mi.helper.transfer;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
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
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, CableTier cableTier, int maxAmount)
	{
		MIEnergyStorage target = cache.output(level, pos, direction);
		if(target != null && (cableTier == null || target.canConnect(cableTier)))
		{
			return EnergyHandlerUtil.move(this.sourceHandler(), target, maxAmount, null) > 0;
		}
		return false;
	}
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, CableTier cableTier)
	{
		return this.autoExtract(level, pos, direction, cableTier, Integer.MAX_VALUE);
	}
	
	public boolean autoExtract(Level level, BlockPos pos, Direction direction, int maxAmount)
	{
		return this.autoExtract(level, pos, direction, null, maxAmount);
	}
	
	@Override
	public boolean autoExtract(Level level, BlockPos pos, Direction direction)
	{
		return this.autoExtract(level, pos, direction, null, Integer.MAX_VALUE);
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, CableTier cableTier, int maxAmount)
	{
		MIEnergyStorage target = cache.input(level, pos, direction);
		if(target != null && target.canConnect(cableTier))
		{
			return EnergyHandlerUtil.move(target, this.sourceHandler(), maxAmount, null) > 0;
		}
		return false;
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, CableTier cableTier)
	{
		return this.autoInsert(level, pos, direction, cableTier, Integer.MAX_VALUE);
	}
	
	public boolean autoInsert(Level level, BlockPos pos, Direction direction, int maxAmount)
	{
		return this.autoInsert(level, pos, direction, null, maxAmount);
	}
	
	@Override
	public boolean autoInsert(Level level, BlockPos pos, Direction direction)
	{
		return this.autoInsert(level, pos, direction, null, Integer.MAX_VALUE);
	}
}
