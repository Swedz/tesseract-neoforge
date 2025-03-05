package net.swedz.tesseract.neoforge.event;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;

public class TreeGrowthEvent extends BlockEvent
{
	private final List<BlockPos> positions;
	
	public TreeGrowthEvent(LevelAccessor level, BlockPos pos, BlockState state, List<BlockPos> positions)
	{
		super(level, pos.immutable(), state);
		
		this.positions = Lists.newArrayList();
		for(var blockPos : positions)
		{
			this.positions.add(blockPos.immutable());
		}
	}
	
	public List<BlockPos> getPositions()
	{
		return new ArrayList<>(positions);
	}
}
