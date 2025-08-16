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
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

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
	
	@SafeVarargs
	public final void registerHatches(String id, String englishName, String overlayFolder, MachineCasing casing,
									  HatchFactory factory,
									  Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		for(int i = 0; i < 2; i++)
		{
			boolean input = i == 0;
			String machineId = "%s_%s_hatch".formatted(id, input ? "input" : "output");
			String machineEnglishName = "%s %s Hatch".formatted(englishName, input ? "Input" : "Output");
			HackedMachineRegistrationHelper.registerMachine(hook, machineEnglishName, machineId, (bep) -> factory.create(bep, input, hook.id(machineId)), extraRegistrators);
			HackedMachineRegistrationHelper.addMachineModel(hook, machineId, casing, overlayFolder, true, false, true, false);
		}
	}
	
	public void registerItemHatches(String id, String englishName, MachineCasing casing, int rows, int columns, int xStart, int yStart)
	{
		this.registerHatches(id + "_item", englishName + " Item", "hatch_item", casing, (bep, input, machineId) ->
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
		}, MachineBlockEntity::registerItemApi);
	}
	
	public void registerFluidHatches(String id, String englishName, MachineCasing casing, int bucketCapacity)
	{
		this.registerHatches(id + "_fluid", englishName + " Fluid", "hatch_fluid", casing, (bep, input, machineId) ->
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
		}, MachineBlockEntity::registerFluidApi);
	}
	
	public void registerEnergyHatches(CableTier tier)
	{
		this.registerHatches(
				tier.name + "_energy", tier.shortEnglishName + " Energy",
				"hatch_energy", tier.casing,
				(bep, input, machineId) -> new EnergyHatch(bep, new MachineGuiParameters.Builder(machineId, false).build(), input, tier),
				EnergyHatch::registerEnergyApi
		);
	}
}
