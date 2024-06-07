package net.swedz.tesseract.neoforge.compat.mi.mixin.hack;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.init.MachineRegistrationHelper;
import aztech.modern_industrialization.util.MobSpawning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.swedz.tesseract.neoforge.compat.mi.hack.FakedMachineModelBuilder;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.registry.CommonModelBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(
		value = MachineRegistrationHelper.class,
		remap = false
)
public class RegisterMachineHackMixin
{
	@Inject(
			method = "registerMachine",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void registerMachine(String englishName, String id,
										Function<BEP, MachineBlockEntity> factory,
										Consumer<BlockEntityType<?>>[] extraRegistrators,
										CallbackInfoReturnable<Supplier<BlockEntityType<?>>> callback)
	{
		if(MIHookTracker.isTracking())
		{
			ResourceLocation fullId = MIHookTracker.id(id);
			MIHookRegistry registry = MIHooks.getRegistry(MIHookTracker.getTrackingModId());
			
			AtomicReference<BlockEntityType<?>> bet = new AtomicReference<>();
			BiFunction<BlockPos, BlockState, MachineBlockEntity> ctor = (pos, state) -> factory.apply(new BEP(bet.get(), pos, state));
			
			BlockWithItemHolder<?, ?> blockHolder = new BlockWithItemHolder<>(
					fullId, englishName,
					registry.blockRegistry(), (p) -> new MachineBlock(ctor, p),
					registry.itemRegistry(), BlockItem::new
			);
			blockHolder.item().sorted(registry.sortOrderMachines());
			blockHolder
					.withProperties((p) -> p
							.mapColor(MapColor.METAL)
							.destroyTime(4)
							.requiresCorrectToolForDrops()
							.isValidSpawn(MobSpawning.NO_SPAWN))
					.withModel((holder) -> (provider) ->
					{
						MIHookTracker.MachineModelProperties machineModelProperties = MIHookTracker.getMachineModel(fullId);
						if(machineModelProperties == null)
						{
							CommonModelBuilders.blockstateOnly(holder).accept(provider);
							return;
						}
						provider.simpleBlockWithItem(BuiltInRegistries.BLOCK.get(fullId), provider.models()
								.getBuilder(id)
								.customLoader((bmb, exFile) -> new FakedMachineModelBuilder<>(machineModelProperties, bmb, exFile))
								.end());
					})
					.register();
			
			registry.onBlockRegister(blockHolder);
			registry.onItemRegister(blockHolder.item());
			
			callback.setReturnValue(registry.blockEntityRegistry().register(id, () ->
			{
				Block block = blockHolder.get();
				
				bet.set(BlockEntityType.Builder.of(ctor::apply, block).build(null));
				
				for(Consumer<BlockEntityType<?>> extraRegistrator : extraRegistrators)
				{
					extraRegistrator.accept(bet.get());
				}
				
				registry.onBlockEntityRegister(bet.get());
				
				return bet.get();
			}));
		}
	}
}
