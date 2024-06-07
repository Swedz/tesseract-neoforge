package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.datagen.client.mi.MachineCasingModelsMIHookDatagenProvider;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class MachineCasingsMIHookContext implements MIHookContext
{
	public MachineCasing register(String name, BiConsumer<MachineCasing, MachineCasingModelsMIHookDatagenProvider> model)
	{
		MachineCasing casing = MachineCasings.create(name);
		MIHookTracker.addMachineCasingModel(name, (provider) -> model.accept(casing, provider));
		return casing;
	}
	
	public MachineCasing registerImitateBlock(String name, Supplier<Block> block)
	{
		return this.register(name, (casing, provider) -> provider.imitateBlock(casing, block.get()));
	}
	
	public MachineCasing registerCubeBottomTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top)
	{
		return this.register(name, (casing, provider) -> provider.cubeBottomTop(casing, side, bottom, top));
	}
	
	public MachineCasing registerCubeAll(String name, ResourceLocation side)
	{
		return this.register(name, (casing, provider) -> provider.cubeAll(casing, side));
	}
}
