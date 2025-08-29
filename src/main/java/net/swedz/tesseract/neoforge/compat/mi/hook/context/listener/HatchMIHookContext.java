package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.hatches.EnergyHatch;
import aztech.modern_industrialization.machines.blockentities.hatches.FluidHatch;
import aztech.modern_industrialization.machines.blockentities.hatches.ItemHatch;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.multiblocks.HatchBlockEntity;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class HatchMIHookContext extends MIHookContext
{
	public HatchMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public interface HatchFactory
	{
		HatchBlockEntity create(BEP bep, boolean input, ResourceLocation machineId);
	}
	
	public interface HatchModification<T>
	{
		void apply(T value, boolean input);
	}

	@SafeVarargs
	public final void registerHatch(String id, String englishName, String overlayFolder, MachineCasing casing,
									Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
									Consumer<BlockBehaviour.Properties> overrideProperties,
									boolean defaultMineableTags,
									HatchFactory factory,
									boolean input,
									Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		HackedMachineRegistrationHelper.registerMachine(hook, englishName, id, modifyBlock, overrideProperties, defaultMineableTags, (bep) -> factory.create(bep, input, hook.id(id)), extraRegistrators);
		HackedMachineRegistrationHelper.addMachineModel(hook, id, casing, overlayFolder, true, false, true, false);
	}
	
	@SafeVarargs
	public final void registerHatch(String id, String englishName, String overlayFolder, MachineCasing casing,
									Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
									Consumer<BlockBehaviour.Properties> overrideProperties,
									boolean defaultMineableTags,
									HatchFactory factory,
									Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		registerHatch(id, englishName, overlayFolder, casing, modifyBlock, overrideProperties, defaultMineableTags, factory, false, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerHatch(String id, String englishName, String overlayFolder, MachineCasing casing,
									Consumer<BlockWithItemHolder<?, ?>> modifyBlock,
									Consumer<BlockBehaviour.Properties> overrideProperties,
									HatchFactory factory,
									Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerHatch(id, englishName, overlayFolder, casing, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerHatch(String id, String englishName, String overlayFolder, MachineCasing casing,
									HatchFactory factory,
									Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerHatch(id, englishName, overlayFolder, casing, null, null, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerHatches(String id, String englishName, String overlayFolder, MachineCasing casing,
									  HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
									  HatchModification<BlockBehaviour.Properties> overrideProperties,
									  boolean defaultMineableTags,
									  HatchFactory factory,
									  Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		for(int i = 0; i < 2; i++)
		{
			boolean input = i == 0;
			String machineId = "%s_%s_hatch".formatted(id, input ? "input" : "output");
			String machineEnglishName = "%s %s Hatch".formatted(englishName, input ? "Input" : "Output");
			this.registerHatch(
					machineId, machineEnglishName, overlayFolder, casing,
					modifyBlock != null ? (holder) -> modifyBlock.apply(holder, input) : null,
					overrideProperties != null ? (properties) -> overrideProperties.apply(properties, input) : null,
					defaultMineableTags, factory, input, extraRegistrators
			);
		}
	}
	
	@SafeVarargs
	public final void registerHatches(String id, String englishName, String overlayFolder, MachineCasing casing,
									  HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
									  HatchModification<BlockBehaviour.Properties> overrideProperties,
									  HatchFactory factory,
									  Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		registerHatches(id, englishName, overlayFolder, casing, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerHatches(String id, String englishName, String overlayFolder, MachineCasing casing,
									  HatchFactory factory,
									  Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		registerHatches(id, englishName, overlayFolder, casing, null, null, factory, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerItemHatches(String id, String englishName, MachineCasing casing, int rows, int columns, int xStart, int yStart,
										  HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
										  HatchModification<BlockBehaviour.Properties> overrideProperties,
										  boolean defaultMineableTags,
										  Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerHatches(id + "_item", englishName + " Item", "hatch_item", casing, modifyBlock, overrideProperties, defaultMineableTags, (bep, input, machineId) ->
		{
			List<ConfigurableItemStack> itemStacks = Lists.newArrayList();
			for(int slot = 0; slot < rows * columns; slot++)
			{
				if(input)
				{
					itemStacks.add(ConfigurableItemStack.standardInputSlot());
				}
				else
				{
					itemStacks.add(ConfigurableItemStack.standardOutputSlot());
				}
			}
			var inventory = new MIInventory(
					itemStacks,
					Collections.emptyList(),
					new SlotPositions.Builder().addSlots(xStart, yStart, columns, rows).build(),
					SlotPositions.empty()
			);
			return new ItemHatch(bep, new MachineGuiParameters.Builder(machineId, true).build(), input, !id.equals("bronze"), inventory);
		}, ArrayUtils.add(extraRegistrators, MachineBlockEntity::registerItemApi));
	}
	
	@SafeVarargs
	public final void registerItemHatches(String id, String englishName, MachineCasing casing, int rows, int columns, int xStart, int yStart,
										  HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
										  HatchModification<BlockBehaviour.Properties> overrideProperties,
										  Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerItemHatches(id, englishName, casing, rows, columns, xStart, yStart, modifyBlock, overrideProperties, true, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerItemHatches(String id, String englishName, MachineCasing casing, int rows, int columns, int xStart, int yStart,
										  Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerItemHatches(id, englishName, casing, rows, columns, xStart, yStart, null, null, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerFluidHatches(String id, String englishName, MachineCasing casing, int bucketCapacity,
										   HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
										   HatchModification<BlockBehaviour.Properties> overrideProperties,
										   boolean defaultMineableTags,
										   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerHatches(id + "_fluid", englishName + " Fluid", "hatch_fluid", casing, modifyBlock, overrideProperties, defaultMineableTags, (bep, input, machineId) ->
		{
			List<ConfigurableFluidStack> fluidStacks = Collections.singletonList(input ?
					ConfigurableFluidStack.standardInputSlot(bucketCapacity * 1000L) :
					ConfigurableFluidStack.standardOutputSlot(bucketCapacity * 1000L));
			var inventory = new MIInventory(
					Collections.emptyList(),
					fluidStacks,
					SlotPositions.empty(),
					new SlotPositions.Builder().addSlot(80, 40).build()
			);
			return new FluidHatch(bep, new MachineGuiParameters.Builder(machineId, true).build(), input, !id.equals("bronze"), inventory);
		}, ArrayUtils.add(extraRegistrators, MachineBlockEntity::registerFluidApi));
	}
	
	@SafeVarargs
	public final void registerFluidHatches(String id, String englishName, MachineCasing casing, int bucketCapacity,
										   HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
										   HatchModification<BlockBehaviour.Properties> overrideProperties,
										   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerFluidHatches(id, englishName, casing, bucketCapacity, modifyBlock, overrideProperties, true, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerFluidHatches(String id, String englishName, MachineCasing casing, int bucketCapacity,
										   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerFluidHatches(id, englishName, casing, bucketCapacity, null, null, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerEnergyHatches(CableTier tier,
											HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
											HatchModification<BlockBehaviour.Properties> overrideProperties,
											boolean defaultMineableTags,
											Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerHatches(
				tier.name + "_energy", tier.shortEnglishName + " Energy",
				"hatch_energy", tier.casing,
				modifyBlock, overrideProperties, defaultMineableTags,
				(bep, input, machineId) -> new EnergyHatch(bep, new MachineGuiParameters.Builder(machineId, false).build(), input, tier),
				ArrayUtils.add(extraRegistrators, EnergyHatch::registerEnergyApi)
		);
	}
	
	@SafeVarargs
	public final void registerEnergyHatches(CableTier tier,
											HatchModification<BlockWithItemHolder<?, ?>> modifyBlock,
											HatchModification<BlockBehaviour.Properties> overrideProperties,
											Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerEnergyHatches(tier, modifyBlock, overrideProperties, true, extraRegistrators);
	}
	
	@SafeVarargs
	public final void registerEnergyHatches(CableTier tier,
											Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		this.registerEnergyHatches(tier, null, null, extraRegistrators);
	}
}
