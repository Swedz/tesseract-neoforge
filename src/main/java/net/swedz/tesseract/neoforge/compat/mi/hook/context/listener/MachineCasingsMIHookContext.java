package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.datagen.mi.client.MachineCasingModelsMIHookDatagenProvider;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class MachineCasingsMIHookContext implements MIHookContext
{
	public MachineCasing register(String id, BiConsumer<MachineCasing, MachineCasingModelsMIHookDatagenProvider> model)
	{
		MachineCasing casing = MachineCasings.create(MIHookTracker.id(id));
		MIHookTracker.addMachineCasingModel((provider) -> model.accept(casing, provider));
		return casing;
	}
	
	public MachineCasing registerImitateBlock(String id, Supplier<Block> block)
	{
		return this.register(id, (casing, provider) -> provider.imitateBlock(casing, block.get()));
	}
	
	public MachineCasing registerCubeBottomTop(String id, ResourceLocation side, ResourceLocation bottom, ResourceLocation top)
	{
		return this.register(id, (casing, provider) -> provider.cubeBottomTop(casing, side, bottom, top));
	}
	
	public MachineCasing registerCubeAll(String id, ResourceLocation side)
	{
		return this.register(id, (casing, provider) -> provider.cubeAll(casing, side));
	}
}
