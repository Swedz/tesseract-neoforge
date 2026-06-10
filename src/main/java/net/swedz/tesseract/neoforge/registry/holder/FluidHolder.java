package net.swedz.tesseract.neoforge.registry.holder;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.api.FluidLike;
import net.swedz.tesseract.neoforge.registry.RegisteredObjectHolder;
import net.swedz.tesseract.neoforge.registry.SortOrder;
import net.swedz.tesseract.neoforge.registry.common.CommonCapabilities;
import net.swedz.tesseract.neoforge.registry.registerable.SimpleRegisterableWrapper;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FluidHolder<F extends Fluid, FT extends FluidType, FB extends Block, FBI extends BucketItem> extends RegisteredObjectHolder<Fluid, F, FluidHolder<F, FT, FB, FBI>> implements FluidLike
{
	private final SimpleRegisterableWrapper<Fluid, F>      registerableFluid;
	private final SimpleRegisterableWrapper<FluidType, FT> registerableFluidType;
	
	private final BlockHolder<FB> blockHolder;
	private final ItemHolder<FBI> bucketItemHolder;
	
	public FluidHolder(
			Identifier location,
			String englishName,
			DeferredRegister<Fluid> registerFluids,
			Function<FluidHolder<F, FT, FB, FBI>, F> creatorFluid,
			DeferredRegister<FluidType> registerFluidTypes,
			Function<FluidHolder<F, FT, FB, FBI>, FT> creatorFluidType,
			DeferredRegister.Blocks registerBlocks,
			BiFunction<FluidHolder<F, FT, FB, FBI>, BlockBehaviour.Properties, FB> creatorFluidBlock,
			DeferredRegister.Items registerItems,
			BiFunction<FluidHolder<F, FT, FB, FBI>, Item.Properties, FBI> creatorBucketItem,
			SortOrder bucketSortOrder)
	{
		super(location, englishName);
		this.registerableFluid = new SimpleRegisterableWrapper<>(registerFluids, () -> creatorFluid.apply(this));
		this.registerableFluidType = new SimpleRegisterableWrapper<>(registerFluidTypes, () -> creatorFluidType.apply(this));
		this.blockHolder = new BlockHolder<>(location, englishName, registerBlocks, (p) -> creatorFluidBlock.apply(this, p));
		this.bucketItemHolder = new ItemHolder<>(Identifier.fromNamespaceAndPath(location.getNamespace(), location.getPath() + "_bucket"), englishName + " Bucket", registerItems, (p) -> creatorBucketItem.apply(this, p))
				.sorted(bucketSortOrder)
				.withCapabilities(CommonCapabilities::bucketItem);
	}
	
	public SimpleRegisterableWrapper<Fluid, F> registerableFluid()
	{
		return registerableFluid;
	}
	
	public SimpleRegisterableWrapper<FluidType, FT> registerableFluidType()
	{
		return registerableFluidType;
	}
	
	public BlockHolder<FB> block()
	{
		return blockHolder;
	}
	
	public FluidHolder<F, FT, FB, FBI> block(Consumer<BlockHolder<FB>> consumer)
	{
		consumer.accept(blockHolder);
		return this;
	}
	
	public ItemHolder<FBI> bucketItem()
	{
		return bucketItemHolder;
	}
	
	public FluidHolder<F, FT, FB, FBI> bucketItem(Consumer<ItemHolder<FBI>> consumer)
	{
		consumer.accept(bucketItemHolder);
		return this;
	}
	
	@Override
	public FluidHolder<F, FT, FB, FBI> register()
	{
		this.guaranteeUnlocked();
		
		registerableFluid.registerSimple(identifier, DeferredRegister::register);
		registerableFluidType.registerSimple(identifier, DeferredRegister::register);
		blockHolder.register();
		bucketItemHolder.register();
		
		this.lock();
		return this;
	}
	
	@Override
	public F get()
	{
		return registerableFluid.getOrThrow();
	}
	
	@Override
	public Fluid asFluid()
	{
		return this.get();
	}
}
