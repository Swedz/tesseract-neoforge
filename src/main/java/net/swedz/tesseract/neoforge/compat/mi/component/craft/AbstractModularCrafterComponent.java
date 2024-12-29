package net.swedz.tesseract.neoforge.compat.mi.component.craft;

import aztech.modern_industrialization.api.machine.component.InventoryAccess;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.machines.IComponent;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookEfficiency;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.machine.EfficiencyMIHookContext;

import static aztech.modern_industrialization.util.Simulation.*;

/**
 * Most of the code here was directly copied from {@link CrafterComponent}, please understand.
 *
 * @param <R> the recipe type to use
 */
public abstract class AbstractModularCrafterComponent<R> implements IComponent.ServerOnly, ModularCrafterAccess<R>
{
	protected final MachineProcessCondition.Context conditionContext;
	
	protected final CrafterComponent.Inventory   inventory;
	protected final ModularCrafterAccessBehavior behavior;
	
	protected R                activeRecipe;
	protected ResourceLocation delayedActiveRecipe;
	
	protected long usedEnergy;
	protected long recipeEnergy;
	protected long recipeMaxEu;
	
	protected int efficiencyTicks;
	protected int maxEfficiencyTicks;
	
	protected long previousBaseEu = -1;
	protected long previousMaxEu  = -1;
	
	protected int lastInvHash    = 0;
	protected int lastForcedTick = 0;
	
	public AbstractModularCrafterComponent(
			MachineBlockEntity blockEntity,
			CrafterComponent.Inventory inventory,
			ModularCrafterAccessBehavior behavior
	)
	{
		this.inventory = inventory;
		this.behavior = behavior;
		this.conditionContext = () -> blockEntity;
	}
	
	protected long transformEuCost(long eu)
	{
		return eu;
	}
	
	protected abstract boolean canContinueRecipe();
	
	protected abstract Iterable<R> getRecipes();
	
	protected abstract ResourceLocation getRecipeId(R recipe);
	
	protected abstract R getRecipeById(ResourceLocation recipeId);
	
	protected abstract void onTick();
	
	protected boolean takeInputs(R recipe, boolean simulate)
	{
		return this.takeItemInputs(recipe, simulate) &&
			   this.takeFluidInputs(recipe, simulate);
	}
	
	protected abstract boolean takeItemInputs(R recipe, boolean simulate);
	
	protected abstract boolean takeFluidInputs(R recipe, boolean simulate);
	
	protected boolean putOutputs(R recipe, boolean simulate, boolean toggleLock)
	{
		return this.putItemOutputs(recipe, simulate, toggleLock) &&
			   this.putFluidOutputs(recipe, simulate, toggleLock);
	}
	
	protected abstract boolean putItemOutputs(R recipe, boolean simulate, boolean toggleLock);
	
	protected abstract boolean putFluidOutputs(R recipe, boolean simulate, boolean toggleLock);
	
	public abstract void lockRecipe(ResourceLocation recipeId, Inventory inventory);
	
	@Override
	public InventoryAccess getInventory()
	{
		return inventory;
	}
	
	@Override
	public ModularCrafterAccessBehavior getBehavior()
	{
		return behavior;
	}
	
	@Override
	public boolean hasActiveRecipe()
	{
		return activeRecipe != null;
	}
	
	@Override
	public R getActiveRecipe()
	{
		return activeRecipe;
	}
	
	@Override
	public float getProgress()
	{
		return (float) usedEnergy / recipeEnergy;
	}
	
	@Override
	public int getEfficiencyTicks()
	{
		return efficiencyTicks;
	}
	
	@Override
	public int getMaxEfficiencyTicks()
	{
		return maxEfficiencyTicks;
	}
	
	@Override
	public long getCurrentRecipeEu()
	{
		return recipeMaxEu;
	}
	
	@Override
	public void decreaseEfficiencyTicks()
	{
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				conditionContext.getBlockEntity(), this.hasActiveRecipe(),
				maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onDecreaseEfficiencyTicks);
		if(context.isCancelled())
		{
			return;
		}
		
		efficiencyTicks = Math.max(efficiencyTicks - 1, 0);
		this.clearActiveRecipeIfPossible();
	}
	
	@Override
	public void increaseEfficiencyTicks(int increment)
	{
		EfficiencyMIHookContext context = new EfficiencyMIHookContext(
				conditionContext.getBlockEntity(), this.hasActiveRecipe(),
				maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
		);
		MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onIncreaseEfficiencyTicks);
		if(context.isCancelled())
		{
			return;
		}
		
		efficiencyTicks = Math.min(efficiencyTicks + increment, maxEfficiencyTicks);
	}
	
	/**
	 * Attempt to re-lock hatches to continue the active recipe.
	 *
	 * @return True if there is no current recipe or if the hatches could be locked
	 * for it, false otherwise.
	 */
	public boolean tryContinueRecipe()
	{
		this.loadDelayedActiveRecipe();
		
		if(this.hasActiveRecipe() && this.canContinueRecipe())
		{
			if(this.putOutputs(activeRecipe, true, false))
			{
				this.putOutputs(activeRecipe, true, true);
			}
			else
			{
				return false;
			}
		}
		
		return true;
	}
	
	protected void loadDelayedActiveRecipe()
	{
		if(delayedActiveRecipe != null)
		{
			activeRecipe = this.getRecipeById(delayedActiveRecipe);
			delayedActiveRecipe = null;
			if(activeRecipe == null)
			{
				// If a recipe got removed, we need to reset the efficiency and the used energy
				// to allow the machine to resume processing.
				efficiencyTicks = 0;
				usedEnergy = 0;
			}
		}
	}
	
	protected boolean updateActiveRecipe()
	{
		for(R recipe : this.getRecipes())
		{
			if(behavior.isRecipeBanned(this.getRecipeEuCost(recipe)))
			{
				continue;
			}
			if(this.tryStartRecipe(recipe))
			{
				// Make sure we recalculate the max efficiency ticks if the recipe changes or if
				// the efficiency has reached 0 (the latter is to recalculate the efficiency for
				// 0.3.6 worlds without having to break and replace the machines)
				if(activeRecipe != recipe || efficiencyTicks == 0)
				{
					maxEfficiencyTicks = this.getRecipeMaxEfficiencyTicks(recipe);
				}
				activeRecipe = recipe;
				usedEnergy = 0;
				recipeEnergy = this.transformEuCost(this.getRecipeTotalEuCost(recipe));
				recipeMaxEu = this.getRecipeMaxEu(this.getRecipeEuCost(recipe), recipeEnergy, efficiencyTicks);
				return true;
			}
		}
		return false;
	}
	
	protected boolean tryStartRecipe(R recipe)
	{
		if(this.takeInputs(recipe, true) &&
		   this.putOutputs(recipe, true, false) &&
		   this.doConditionsMatchForRecipe(recipe))
		{
			this.takeInputs(recipe, false);
			this.putOutputs(recipe, true, true);
			return true;
		}
		return false;
	}
	
	public boolean tickRecipe()
	{
		if(behavior.getCrafterWorld().isClientSide())
		{
			throw new IllegalStateException("May not call client side.");
		}
		
		{
			EfficiencyMIHookContext context = new EfficiencyMIHookContext(
					conditionContext.getBlockEntity(), this.hasActiveRecipe(),
					maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
			);
			MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onTickStart);
			efficiencyTicks = context.getEfficiencyTicks();
		}
		
		this.onTick();
		
		boolean active = false;
		boolean enabled = behavior.isEnabled();
		
		this.loadDelayedActiveRecipe();
		
		boolean started = false;
		if(usedEnergy == 0 && enabled)
		{
			if(behavior.canConsumeEu(1))
			{
				started = this.updateActiveRecipe();
			}
		}
		
		long eu = 0;
		boolean finished = false;
		if(activeRecipe != null && enabled)
		{
			if(usedEnergy > 0 || started)
			{
				recipeMaxEu = this.getRecipeMaxEu(this.getRecipeEuCost(activeRecipe), recipeEnergy, efficiencyTicks);
				eu = this.doConditionsMatchForRecipe(activeRecipe) ? behavior.consumeEu(Math.min(recipeMaxEu, recipeEnergy - usedEnergy), ACT) : 0;
				active = eu > 0;
				usedEnergy += eu;
				
				if(usedEnergy == recipeEnergy)
				{
					this.putOutputs(activeRecipe, false, false);
					
					this.clearLocks();
					
					usedEnergy = 0;
					finished = true;
				}
			}
			else if(behavior.isOverdriving())
			{
				eu = this.doConditionsMatchForRecipe(activeRecipe) ? behavior.consumeEu(recipeMaxEu, ACT) : 0;
				active = eu > 0;
			}
		}
		
		if(activeRecipe != null && (previousBaseEu != behavior.getBaseRecipeEu() || previousMaxEu != behavior.getMaxRecipeEu()))
		{
			previousBaseEu = behavior.getBaseRecipeEu();
			previousMaxEu = behavior.getMaxRecipeEu();
			maxEfficiencyTicks = this.getRecipeMaxEfficiencyTicks(activeRecipe);
			efficiencyTicks = Math.min(efficiencyTicks, maxEfficiencyTicks);
		}
		
		// If we finished a recipe, we can add an efficiency tick
		if(finished)
		{
			if(efficiencyTicks < maxEfficiencyTicks)
			{
				efficiencyTicks++;
			}
		}
		// If we didn't use the max energy this tick and the recipe is still ongoing, remove one efficiency tick
		else if(eu < recipeMaxEu)
		{
			if(efficiencyTicks > 0)
			{
				efficiencyTicks--;
			}
		}
		
		{
			final long euUsed = eu;
			EfficiencyMIHookContext context = new EfficiencyMIHookContext(
					conditionContext.getBlockEntity(), this.hasActiveRecipe(),
					maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
			);
			MIHooks.triggerHookEfficiencyListeners(context, (h, c) -> h.onTickEnd(c, euUsed));
			efficiencyTicks = context.getEfficiencyTicks();
		}
		
		// If the recipe is done, allow starting another one when the efficiency reaches 0
		this.clearActiveRecipeIfPossible();
		
		return active;
	}
	
	@Override
	public void writeNbt(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putLong("usedEnergy", usedEnergy);
		tag.putLong("recipeEnergy", recipeEnergy);
		tag.putLong("recipeMaxEu", recipeMaxEu);
		if(this.hasActiveRecipe())
		{
			tag.putString("activeRecipe", this.getRecipeId(activeRecipe).toString());
		}
		else if(delayedActiveRecipe != null)
		{
			tag.putString("activeRecipe", delayedActiveRecipe.toString());
		}
		tag.putInt("efficiencyTicks", efficiencyTicks);
		tag.putInt("maxEfficiencyTicks", maxEfficiencyTicks);
	}
	
	@Override
	public void readNbt(CompoundTag tag, HolderLookup.Provider registries, boolean isUpgradingMachine)
	{
		usedEnergy = tag.getInt("usedEnergy");
		recipeEnergy = tag.getInt("recipeEnergy");
		recipeMaxEu = tag.getInt("recipeMaxEu");
		delayedActiveRecipe = tag.contains("activeRecipe") ? ResourceLocation.parse(tag.getString("activeRecipe")) : null;
		if(delayedActiveRecipe == null && usedEnergy > 0)
		{
			usedEnergy = 0;
			Tesseract.LOGGER.error("Had to set the usedEnergy of MultipliedCrafterComponent to 0, but that should never happen!");
		}
		efficiencyTicks = tag.getInt("efficiencyTicks");
		maxEfficiencyTicks = tag.getInt("maxEfficiencyTicks");
		
		{
			EfficiencyMIHookContext context = new EfficiencyMIHookContext(
					conditionContext.getBlockEntity(), this.hasActiveRecipe(),
					maxEfficiencyTicks, efficiencyTicks, recipeMaxEu
			);
			MIHooks.triggerHookEfficiencyListeners(context, MIHookEfficiency::onReadNbt);
			efficiencyTicks = context.getEfficiencyTicks();
			recipeMaxEu = context.getMaxRecipeEu();
		}
	}
	
	protected void clearActiveRecipes()
	{
		activeRecipe = null;
	}
	
	protected void clearActiveRecipeIfPossible()
	{
		if(efficiencyTicks == 0 && usedEnergy == 0)
		{
			this.clearActiveRecipes();
		}
	}
	
	protected long getRecipeMaxEu(long recipeEu, long totalEu, int efficiencyTicks)
	{
		long baseEu = Math.max(this.transformEuCost(behavior.getBaseRecipeEu()), this.transformEuCost(recipeEu));
		return Math.min(totalEu, Math.min((int) Math.floor(baseEu * CrafterComponent.getEfficiencyOverclock(efficiencyTicks)), this.transformEuCost(behavior.getMaxRecipeEu())));
	}
	
	protected int getRecipeMaxEfficiencyTicks(R recipe)
	{
		long eu = this.getRecipeEuCost(recipe);
		long totalEu = this.transformEuCost(this.getRecipeTotalEuCost(recipe));
		for(int ticks = 0; true; ++ticks)
		{
			if(this.getRecipeMaxEu(eu, totalEu, ticks) == Math.min(this.transformEuCost(behavior.getMaxRecipeEu()), totalEu))
			{
				return ticks;
			}
		}
	}
	
	protected void clearLocks()
	{
		for(ConfigurableItemStack stack : inventory.getItemOutputs())
		{
			if(stack.isMachineLocked())
			{
				stack.disableMachineLock();
			}
		}
		for(ConfigurableFluidStack stack : inventory.getFluidOutputs())
		{
			if(stack.isMachineLocked())
			{
				stack.disableMachineLock();
			}
		}
	}
}
