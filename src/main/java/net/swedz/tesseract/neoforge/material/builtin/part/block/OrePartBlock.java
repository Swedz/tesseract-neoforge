package net.swedz.tesseract.neoforge.material.builtin.part.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.swedz.tesseract.neoforge.material.builtin.property.OrePartDrops;
import net.swedz.tesseract.neoforge.material.part.MaterialPartBlockFactory;

import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public class OrePartBlock extends Block
{
	public static MaterialPartBlockFactory factory()
	{
		return MaterialPartBlockFactory.of(
				(c, p) -> new OrePartBlock(p, c.getOrThrow(ORE_DROP_PART)),
				(c, b, p) -> new BlockItem(b, p)
		);
	}
	
	protected final OrePartDrops drops;
	
	public OrePartBlock(Properties properties, OrePartDrops drops)
	{
		super(properties);
		this.drops = drops;
	}
	
	@Override
	public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool)
	{
		return drops.experience().sample(level.getRandom());
	}
}
