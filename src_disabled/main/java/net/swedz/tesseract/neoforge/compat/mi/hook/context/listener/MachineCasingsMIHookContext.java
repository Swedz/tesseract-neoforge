package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.api.function.TriConsumer;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.datagen.mi.client.MachineCasingModelsMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.model.ModelGenerators;

import java.util.function.Supplier;

public final class MachineCasingsMIHookContext extends MIHookContext
{
	public MachineCasingsMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public MachineCasing register(String id, String englishName, TriConsumer<MachineCasing, MachineCasingModelsMIHookDatagenProvider, ModelGenerators> model)
	{
		MachineCasing casing = MachineCasings.create(hook.id(id), englishName);
		MIHookTracker.addMachineCasingModel(hook, (provider, generators) -> model.accept(casing, provider, generators));
		return casing;
	}
	
	public MachineCasing registerImitateBlock(String id, Supplier<? extends Block> block)
	{
		MachineCasing casing = MachineCasings.createBlockImitation(hook.id(id), block);
		MIHookTracker.addMachineCasingModel(hook, (provider, generators) -> provider.imitateBlock(casing, block.get()));
		return casing;
	}
	
	public MachineCasing registerCubeBottomTop(String id, String englishName, Identifier side, Identifier bottom, Identifier top)
	{
		return this.register(id, englishName, (casing, provider, generators) -> provider.cubeBottomTop(generators.block(), casing, side, bottom, top));
	}
	
	public MachineCasing registerCubeAll(String id, String englishName, Identifier side)
	{
		return this.register(id, englishName, (casing, provider, generators) -> provider.cubeAll(generators.block(), casing, side));
	}
}
