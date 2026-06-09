package net.swedz.tesseract.neoforge.registry.holder;

import aztech.modern_industrialization.definition.FluidLike;
import aztech.modern_industrialization.fluid.MIBucketItem;
import aztech.modern_industrialization.fluid.MIFluid;
import aztech.modern_industrialization.fluid.MIFluidBlock;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.registry.MIFluidProperties;
import net.swedz.tesseract.neoforge.registry.SortOrder;

import java.util.function.Consumer;

public class MIFluidHolder extends FluidHolder<MIFluid, FluidType, MIFluidBlock, MIBucketItem> implements FluidLike
{
	private final MIFluidProperties properties;
	
	public MIFluidHolder(
			Identifier location,
			String englishName,
			DeferredRegister<Fluid> registerFluids,
			DeferredRegister<FluidType> registerFluidTypes,
			DeferredRegister.Blocks registerBlocks,
			DeferredRegister.Items registerItems,
			SortOrder bucketSortOrder,
			MIFluidProperties properties
	)
	{
		super(
				location, englishName,
				registerFluids, (holder) -> new MIFluid(
						() -> holder.block().get(),
						() -> holder.bucketItem().get(),
						() -> holder.registerableFluidType().getOrThrow(),
						properties.color()
				),
				registerFluidTypes, (holder) ->
				{
					var fluidTypeProperties = FluidType.Properties.create()
							.descriptionId(holder.block().get().getDescriptionId());
					if(properties.isGas())
					{
						fluidTypeProperties.density(-1000);
					}
					return new FluidType(fluidTypeProperties);
				},
				registerBlocks, (__, p) -> new MIFluidBlock(p, properties.color()),
				registerItems, (holder, p) -> new MIBucketItem(holder.registerableFluid().getOrThrow(), properties.color(), p),
				bucketSortOrder
		);
		this.properties = properties;
		// TODO 26.1
		//this.block().withModel((holder) -> (generators) -> generators.simpleBlock(holder.get(), generators.models().getExistingFile(generators.blockTexture(Blocks.AIR))));
	}
	
	public MIFluidProperties properties()
	{
		return properties;
	}
	
	public MIFluidHolder block(Consumer<BlockHolder<MIFluidBlock>> consumer)
	{
		super.block(consumer);
		return this;
	}
	
	public MIFluidHolder bucketItem(Consumer<ItemHolder<MIBucketItem>> consumer)
	{
		super.bucketItem(consumer);
		return this;
	}
	
	@Override
	public MIFluidHolder register()
	{
		super.register();
		return this;
	}
}
