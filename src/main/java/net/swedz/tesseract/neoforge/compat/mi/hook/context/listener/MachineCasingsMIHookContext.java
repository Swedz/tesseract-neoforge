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
	public MachineCasing register(String id, String englishName, BiConsumer<MachineCasing, MachineCasingModelsMIHookDatagenProvider> model)
	{
		MachineCasing casing = MachineCasings.create(MIHookTracker.id(id), englishName);
		MIHookTracker.addMachineCasingModel((provider) -> model.accept(casing, provider));
		return casing;
	}
	
	public MachineCasing registerImitateBlock(String id, Supplier<? extends Block> block)
	{
		MachineCasing casing = MachineCasings.createBlockImitation(MIHookTracker.id(id), block);
		MIHookTracker.addMachineCasingModel((provider) -> provider.imitateBlock(casing, block.get()));
		return casing;
	}
	
	public MachineCasing registerCubeBottomTop(String id, String englishName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top)
	{
		return this.register(id, englishName, (casing, provider) -> provider.cubeBottomTop(casing, side, bottom, top));
	}
	
	public MachineCasing registerCubeAll(String id, String englishName, ResourceLocation side)
	{
		return this.register(id, englishName, (casing, provider) -> provider.cubeAll(casing, side));
	}
}
