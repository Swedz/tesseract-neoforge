package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;
import aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

import java.util.function.Consumer;

public final class SingleBlockCraftingMachinesMIHookContext implements MIHookContext
{
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
