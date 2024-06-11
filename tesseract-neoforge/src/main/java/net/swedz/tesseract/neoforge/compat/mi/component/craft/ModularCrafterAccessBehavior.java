package net.swedz.tesseract.neoforge.compat.mi.component.craft;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.api.machine.holder.CrafterComponentHolder;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.stats.PlayerStatistics;
import aztech.modern_industrialization.stats.PlayerStatisticsData;
import aztech.modern_industrialization.util.Simulation;
import net.minecraft.world.level.Level;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.efficiency.EfficiencyMIHookContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ModularCrafterAccessBehavior
{
	default boolean isEnabled()
	{
		return true;
	}
	
	long consumeEu(long max, Simulation simulation);
	
	default boolean canConsumeEu(long amount)
	{
		return this.consumeEu(amount, Simulation.SIMULATE) == amount;
	}
	
	default boolean isRecipeBanned(long recipeEuCost)
	{
		return recipeEuCost > this.getMaxRecipeEu();
	}
	
	long getBaseRecipeEu();
	
	long getBaseMaxRecipeEu();
	
	@ApiStatus.NonExtendable
	default long getMaxRecipeEu()
	{
		long maxRecipeEu = this.getBaseMaxRecipeEu();
		if(this instanceof MachineBlockEntity machineBlockEntity &&
				machineBlockEntity instanceof CrafterComponentHolder crafterComponentHolder)
		{
			CrafterAccess crafter = crafterComponentHolder.getCrafterComponent();
			EfficiencyMIHookContext context = new EfficiencyMIHookContext(
					machineBlockEntity, crafter.hasActiveRecipe(),
					crafter.getMaxEfficiencyTicks(), crafter.getEfficiencyTicks(), maxRecipeEu
			);
			MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onGetRecipeMaxEu);
			return context.getMaxRecipeEu();
		}
		return maxRecipeEu;
	}
	
	// can't use getWorld() or the remapping will fail
	Level getCrafterWorld();
	
	default int getMaxFluidOutputs()
	{
		return Integer.MAX_VALUE;
	}
	
	@Nullable
	UUID getOwnerUuid();
	
	default PlayerStatistics getStatsOrDummy()
	{
		var uuid = getOwnerUuid();
		if(uuid == null)
		{
			return PlayerStatistics.DUMMY;
		}
		else
		{
			return PlayerStatisticsData.get(getCrafterWorld().getServer()).get(uuid);
		}
	}
}
