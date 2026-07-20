package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.mixin.accessor.MachineCasingAccessor;
import net.swedz.tesseract.neoforge.datagen.mi.client.MachineCasingModelsMIHookDatagenProvider;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class MachineCasingsMIHookContext extends MIHookContext
{
	public MachineCasingsMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	private MachineCasing create(ResourceLocation key, Supplier<? extends Block> imitatedBlock)
	{
		var casing = MachineCasingAccessor.init(key, imitatedBlock);
		hook.enqueue(() ->
		{
			if(MachineCasings.registeredCasings.containsKey(key))
			{
				throw new IllegalArgumentException("Duplicate machine casing definition: " + key);
			}
			MachineCasings.registeredCasings.put(key, casing);
		});
		return casing;
	}
	
	public MachineCasing register(String id, String englishName, BiConsumer<MachineCasing, MachineCasingModelsMIHookDatagenProvider> model)
	{
		var casing = create(hook.id(id), null);
		MIHookTracker.addMachineCasingModel(hook, (provider) -> model.accept(casing, provider));
		return casing;
	}
	
	public MachineCasing registerImitateBlock(String id, Supplier<? extends Block> block)
	{
		var casing = create(hook.id(id), block);
		MIHookTracker.addMachineCasingModel(hook, (provider) -> provider.imitateBlock(casing, block.get()));
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
