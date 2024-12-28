package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.AbstractCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricCraftingMultiblockBlockEntity;
import aztech.modern_industrialization.machines.blockentities.multiblocks.SteamCraftingMultiblockBlockEntity;
import aztech.modern_industrialization.machines.init.MachineTier;
import net.swedz.tesseract.neoforge.compat.mi.api.MachineTierHolder;
import net.swedz.tesseract.neoforge.compat.mi.mixin.accessor.SteamCraftingMultiblockBlockEntityAccessor;

public final class MachineTierHelper
{
	/**
	 * Gets the {@link MachineTier} for the machine.
	 * <br><br>
	 * Below are all the cases handled:
	 * <ul>
	 *     <li>{@link MachineTierHolder} will use the {@link MachineTierHolder#getMachineTier()} method</li>
	 * </ul>
	 * (From MI):
	 * <ul>
	 *     <li>{@link AbstractCraftingMachineBlockEntity} will use the tier value directly</li>
	 *     <li>{@link SteamCraftingMultiblockBlockEntity} will use an accessor to check if it is steel or bronze</li>
	 *     <li>{@link ElectricCraftingMultiblockBlockEntity} is assumed to be a {@link MachineTier#MULTIBLOCK} machine tier</li>
	 * </ul>
	 *
	 * @param machine the machine block entity
	 * @return the {@link MachineTier} for the machine, null if the machine does not fit any of the above cases
	 */
	public static MachineTier getMachineTier(MachineBlockEntity machine)
	{
		if(machine instanceof MachineTierHolder holder)
		{
			return holder.getMachineTier();
		}
		else if(machine instanceof AbstractCraftingMachineBlockEntity crafting)
		{
			return crafting.tier;
		}
		else if(machine instanceof SteamCraftingMultiblockBlockEntity crafting)
		{
			return ((SteamCraftingMultiblockBlockEntityAccessor) crafting).isSteelTier() ? MachineTier.STEEL : MachineTier.BRONZE;
		}
		else if(machine instanceof ElectricCraftingMultiblockBlockEntity crafting)
		{
			return MachineTier.MULTIBLOCK;
		}
		return null;
	}
}
