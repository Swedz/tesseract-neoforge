package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;
import aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.MachineBuilder;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.SingleBlockCraftingMachineBuilder;

import java.util.function.Consumer;

public final class SingleBlockCraftingMachinesMIHookContext extends MIHookContext
{
	public SingleBlockCraftingMachinesMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public SingleBlockCraftingMachineBuilder builder(String name, String englishName, MachineRecipeType recipeType)
	{
		return MachineBuilder.singleBlockCrafting(hook, name, englishName, recipeType);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String machine, MachineRecipeType type,
						 int itemInputCount, int itemOutputCount, int fluidInputCount, int fluidOutputCount,
						 Consumer<MachineGuiParameters.Builder> guiParams,
						 ProgressBar.Parameters progressBarParams,
						 RecipeEfficiencyBar.Parameters efficiencyBarParams,
						 EnergyBar.Parameters energyBarParams,
						 Consumer<SlotPositions.Builder> itemPositions,
						 Consumer<SlotPositions.Builder> fluidPositions,
						 boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
						 int tiers,
						 int ioBucketCapacity)
	{
		this.register(
				englishName, machine, type,
				itemInputCount, itemOutputCount, fluidInputCount, fluidOutputCount,
				guiParams, progressBarParams, efficiencyBarParams, energyBarParams,
				itemPositions, fluidPositions,
				frontOverlay, topOverlay, sideOverlay,
				tiers, ioBucketCapacity,
				new SingleBlockCraftingMachines.Config()
		);
	}
	
	@Deprecated(forRemoval = true)
	public void register(String englishName, String machine, MachineRecipeType type,
						 int itemInputCount, int itemOutputCount, int fluidInputCount, int fluidOutputCount,
						 Consumer<MachineGuiParameters.Builder> guiParams,
						 ProgressBar.Parameters progressBarParams,
						 RecipeEfficiencyBar.Parameters efficiencyBarParams,
						 EnergyBar.Parameters energyBarParams,
						 Consumer<SlotPositions.Builder> itemPositions,
						 Consumer<SlotPositions.Builder> fluidPositions,
						 boolean frontOverlay, boolean topOverlay, boolean sideOverlay,
						 int tiers,
						 int ioBucketCapacity,
						 SingleBlockCraftingMachines.Config extraConfig)
	{
		HackedMachineRegistrationHelper.registerMachineTiers(
				hook,
				englishName, machine, type,
				itemInputCount, itemOutputCount, fluidInputCount, fluidOutputCount,
				guiParams, progressBarParams, efficiencyBarParams, energyBarParams,
				itemPositions, fluidPositions,
				frontOverlay, topOverlay, sideOverlay,
				tiers, ioBucketCapacity,
				extraConfig
		);
	}
}
