package net.swedz.tesseract.neoforge.event.treegrowth;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.attribute.EnvironmentAttributeReader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.LevelTickAccess;
import net.swedz.tesseract.neoforge.api.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class TrackingWorldGenLevel implements WorldGenLevel
{
	private final WorldGenLevel level;
	
	private final Consumer<BlockPos> onSetBlock;
	
	private final Set<BlockPos> modifiedBlocks = Sets.newConcurrentHashSet();
	
	public TrackingWorldGenLevel(WorldGenLevel level, Consumer<BlockPos> onSetBlock)
	{
		Assert.noneNull(level, onSetBlock);
		this.level = level;
		this.onSetBlock = onSetBlock;
	}
	
	public TrackingWorldGenLevel(WorldGenLevel level)
	{
		this(level, (pos) ->
		{
		});
	}
	
	public Set<BlockPos> getModifiedBlockPositions()
	{
		return Collections.unmodifiableSet(modifiedBlocks);
	}
	
	@Override
	public long getSeed()
	{
		return level.getSeed();
	}
	
	@Override
	public ServerLevel getLevel()
	{
		return level.getLevel();
	}
	
	@Override
	public long nextSubTickCount()
	{
		return level.nextSubTickCount();
	}
	
	@Override
	public LevelTickAccess<Block> getBlockTicks()
	{
		return level.getBlockTicks();
	}
	
	@Override
	public LevelTickAccess<Fluid> getFluidTicks()
	{
		return level.getFluidTicks();
	}
	
	@Override
	public LevelData getLevelData()
	{
		return level.getLevelData();
	}
	
	@Override
	public DifficultyInstance getCurrentDifficultyAt(BlockPos pos)
	{
		return level.getCurrentDifficultyAt(pos);
	}
	
	@Override
	public MinecraftServer getServer()
	{
		return level.getServer();
	}
	
	@Override
	public ChunkSource getChunkSource()
	{
		return level.getChunkSource();
	}
	
	@Override
	public RandomSource getRandom()
	{
		return level.getRandom();
	}
	
	@Override
	public void playSound(Entity entity, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch)
	{
		level.playSound(entity, blockPos, soundEvent, soundSource, volume, pitch);
	}
	
	@Override
	public void addParticle(ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		level.addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
	}
	
	@Override
	public void levelEvent(Entity entity, int type, BlockPos blockPos, int data)
	{
		level.levelEvent(entity, type, blockPos, data);
	}
	
	@Override
	public void gameEvent(Holder<GameEvent> gameEvent, Vec3 pos, GameEvent.Context context)
	{
		level.gameEvent(gameEvent, pos, context);
	}
	
	@Override
	public float getShade(Direction direction, boolean shade)
	{
		return level.getShade(direction, shade);
	}
	
	@Override
	public LevelLightEngine getLightEngine()
	{
		return level.getLightEngine();
	}
	
	@Override
	public WorldBorder getWorldBorder()
	{
		return level.getWorldBorder();
	}
	
	@Override
	public BlockEntity getBlockEntity(BlockPos pos)
	{
		return level.getBlockEntity(pos);
	}
	
	@Override
	public BlockState getBlockState(BlockPos pos)
	{
		return level.getBlockState(pos);
	}
	
	@Override
	public FluidState getFluidState(BlockPos pos)
	{
		return level.getFluidState(pos);
	}
	
	@Override
	public List<Entity> getEntities(Entity entity, AABB area, Predicate<? super Entity> predicate)
	{
		return level.getEntities(entity, area);
	}
	
	@Override
	public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entityTypeTest, AABB bounds, Predicate<? super T> predicate)
	{
		return level.getEntities(entityTypeTest, bounds, predicate);
	}
	
	@Override
	public List<? extends Player> players()
	{
		return level.players();
	}
	
	@Override
	public ChunkAccess getChunk(int x, int z, ChunkStatus chunkStatus, boolean requireChunk)
	{
		return level.getChunk(x, z, chunkStatus, requireChunk);
	}
	
	@Override
	public int getHeight(Heightmap.Types heightmapType, int x, int z)
	{
		return level.getHeight(heightmapType, x, z);
	}
	
	@Override
	public int getSkyDarken()
	{
		return level.getSkyDarken();
	}
	
	@Override
	public BiomeManager getBiomeManager()
	{
		return level.getBiomeManager();
	}
	
	@Override
	public Holder<Biome> getUncachedNoiseBiome(int x, int y, int z)
	{
		return level.getUncachedNoiseBiome(x, y, z);
	}
	
	@Override
	public boolean isClientSide()
	{
		return level.isClientSide();
	}
	
	@Override
	public int getSeaLevel()
	{
		return level.getSeaLevel();
	}
	
	@Override
	public DimensionType dimensionType()
	{
		return level.dimensionType();
	}
	
	@Override
	public RegistryAccess registryAccess()
	{
		return level.registryAccess();
	}
	
	@Override
	public FeatureFlagSet enabledFeatures()
	{
		return level.enabledFeatures();
	}
	
	@Override
	public EnvironmentAttributeReader environmentAttributes()
	{
		return level.environmentAttributes();
	}
	
	@Override
	public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> state)
	{
		return level.isStateAtPosition(pos, state);
	}
	
	@Override
	public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate)
	{
		return level.isFluidAtPosition(pos, predicate);
	}
	
	@Override
	public boolean setBlock(BlockPos pos, BlockState state, int flags, int recursionLeft)
	{
		if(level.setBlock(pos, state, flags, recursionLeft))
		{
			var immutablePos = pos.immutable();
			modifiedBlocks.add(immutablePos);
			onSetBlock.accept(immutablePos);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeBlock(BlockPos pos, boolean isMoving)
	{
		return level.removeBlock(pos, isMoving);
	}
	
	@Override
	public boolean destroyBlock(BlockPos pos, boolean dropBlock, Entity entity, int recursionLeft)
	{
		return level.destroyBlock(pos, dropBlock, entity, recursionLeft);
	}
}
