package net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.inventory.AbstractConfigurableStack;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.fluid.FluidVariant;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.item.ItemVariant;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.AbstractModularCrafterComponent;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public final class MultipliedCrafterComponent extends AbstractModularCrafterComponent<RecipeHolder<MachineRecipe>>
{
	private final Supplier<MachineRecipeType> recipeTypeGetter;
	private final Supplier<Integer>           maxMultiplierGetter;
	private final Supplier<EuCostTransformer> euCostTransformer;
	
	private int tryRecipeMultiplier = 0;
	private int recipeMultiplier    = 0;
	
	public MultipliedCrafterComponent(MachineBlockEntity blockEntity, CrafterComponent.Inventory inventory, ModularCrafterAccessBehavior behavior,
									  Supplier<MachineRecipeType> recipeTypeGetter, Supplier<Integer> maxMultiplierGetter, Supplier<EuCostTransformer> euCostTransformer)
	{
		super(blockEntity, inventory, behavior);
		this.recipeTypeGetter = recipeTypeGetter;
		this.maxMultiplierGetter = maxMultiplierGetter;
		this.euCostTransformer = euCostTransformer;
	}
	
	public MachineRecipeType getRecipeType()
	{
		return recipeTypeGetter.get();
	}
	
	public int getRecipeMultiplier()
	{
		return recipeMultiplier;
	}
	
	public int getMaxMultiplier()
	{
		return maxMultiplierGetter.get();
	}
	
	@Override
	public long transformEuCost(long eu)
	{
		return euCostTransformer.get().transform(this, eu);
	}
	
	@Override
	protected boolean canContinueRecipe()
	{
		return recipeMultiplier != 0;
	}
	
	@Override
	protected ResourceLocation getRecipeId(RecipeHolder<MachineRecipe> recipe)
	{
		return recipe.id();
	}
	
	@Override
	protected RecipeHolder<MachineRecipe> getRecipeById(ResourceLocation recipeId)
	{
		return this.getRecipeType().getRecipe(behavior.getCrafterWorld(), recipeId);
	}
	
	@Override
	public long getBaseRecipeEu()
	{
		return activeRecipe.value().eu;
	}
	
	@Override
	public long getRecipeEuCost(RecipeHolder<MachineRecipe> recipe)
	{
		return recipe.value().eu;
	}
	
	@Override
	public long getRecipeTotalEuCost(RecipeHolder<MachineRecipe> recipe)
	{
		return recipe.value().getTotalEu();
	}
	
	@Override
	public boolean doConditionsMatchForRecipe(RecipeHolder<MachineRecipe> recipe)
	{
		return recipe.value().conditionsMatch(conditionContext);
	}
	
	@Override
	protected void onTick()
	{
	}
	
	@Override
	protected Iterable<RecipeHolder<MachineRecipe>> getRecipes()
	{
		if(this.getRecipeType() == null)
		{
			return Collections.emptyList();
		}
		else if(efficiencyTicks > 0)
		{
			return Collections.singletonList(activeRecipe);
		}
		else
		{
			int currentHash = inventory.hash();
			if(currentHash == lastInvHash)
			{
				if(lastForcedTick == 0)
				{
					lastForcedTick = 100;
				}
				else
				{
					--lastForcedTick;
					return Collections.emptyList();
				}
			}
			else
			{
				lastInvHash = currentHash;
			}
			
			ServerLevel serverWorld = (ServerLevel) behavior.getCrafterWorld();
			MachineRecipeType recipeType = this.getRecipeType();
			List<RecipeHolder<MachineRecipe>> recipes = new ArrayList<>(recipeType.getFluidOnlyRecipes(serverWorld));
			for(ConfigurableItemStack stack : inventory.getItemInputs())
			{
				if(!stack.isEmpty())
				{
					recipes.addAll(recipeType.getMatchingRecipes(serverWorld, stack.getResource().getItem()));
				}
			}
			return recipes;
		}
	}
	
	private int calculateItemInputRecipeMultiplier(MachineRecipe recipe)
	{
		List<ItemStack> itemsInHatches = inventory.getItemInputs().stream()
				.map((item) -> item.getResource().toStack((int) item.getAmount()))
				.toList();
		
		int itemMultiplier = this.getMaxMultiplier();
		for(MachineRecipe.ItemInput input : recipe.itemInputs)
		{
			int countItemsInHatches = 0;
			for(ItemStack stack : itemsInHatches)
			{
				if(input.matches(stack))
				{
					countItemsInHatches += stack.getCount();
				}
			}
			
			if(input.probability() == 0f && countItemsInHatches >= input.amount())
			{
				continue;
			}
			
			int multiplier = countItemsInHatches / input.amount();
			if(multiplier < itemMultiplier)
			{
				itemMultiplier = multiplier;
			}
			if(itemMultiplier <= 1)
			{
				break;
			}
		}
		return itemMultiplier;
	}
	
	private int calculateItemOutputRecipeMultiplier(MachineRecipe recipe)
	{
		List<ItemStack> itemsInHatches = inventory.getItemOutputs().stream()
				.map((item) -> item.getResource().toStack((int) item.getAmount()))
				.toList();
		
		int itemMultiplier = this.getMaxMultiplier();
		for(MachineRecipe.ItemOutput output : recipe.itemOutputs)
		{
			if(output.probability() < 1)
			{
				continue;
			}
			
			int maxOutputCount = output.amount() * this.getMaxMultiplier();
			
			int outputSpace = 0;
			for(ConfigurableItemStack item : inventory.getItemOutputs())
			{
				ItemVariant key = item.getResource();
				if(key.getItem() == output.variant().getItem() || key.isBlank())
				{
					int remainingCapacity = (int) item.getRemainingCapacityFor(output.variant());
					outputSpace += remainingCapacity;
					if(outputSpace >= maxOutputCount)
					{
						outputSpace = maxOutputCount;
						break;
					}
				}
			}
			
			int multiplier = outputSpace / output.amount();
			if(multiplier < itemMultiplier)
			{
				itemMultiplier = multiplier;
			}
			if(itemMultiplier <= 1)
			{
				break;
			}
		}
		
		return itemMultiplier;
	}
	
	private int calculateFluidInputRecipeMultiplier(MachineRecipe recipe)
	{
		int fluidMultiplier = this.getMaxMultiplier();
		for(MachineRecipe.FluidInput input : recipe.fluidInputs)
		{
			long countFluidInHatches = 0;
			for(ConfigurableFluidStack stack : inventory.getFluidInputs())
			{
				if(stack.getResource().equals(FluidVariant.of(input.fluid())))
				{
					countFluidInHatches += stack.getAmount();
				}
			}
			
			if(input.probability() == 0f && countFluidInHatches >= input.amount())
			{
				continue;
			}
			
			int multiplier = (int) (countFluidInHatches / input.amount());
			if(multiplier < fluidMultiplier)
			{
				fluidMultiplier = multiplier;
			}
			if(fluidMultiplier <= 1)
			{
				break;
			}
		}
		return fluidMultiplier;
	}
	
	private int calculateFluidOutputRecipeMultiplier(MachineRecipe recipe)
	{
		int fluidMultiplier = this.getMaxMultiplier();
		for(int i = 0; i < Math.min(recipe.fluidOutputs.size(), behavior.getMaxFluidOutputs()); ++i)
		{
			MachineRecipe.FluidOutput output = recipe.fluidOutputs.get(i);
			
			if(output.probability() < 1)
			{
				continue;
			}
			
			long maxOutputCount = output.amount() * this.getMaxMultiplier();
			
			outer:
			for(int tries = 0; tries < 2; tries++)
			{
				for(ConfigurableFluidStack stack : inventory.getFluidOutputs())
				{
					FluidVariant outputKey = FluidVariant.of(output.fluid());
					if(stack.isResourceAllowedByLock(outputKey) && ((tries == 1 && stack.isResourceBlank()) || stack.getResource().equals(outputKey)))
					{
						long outputSpace = Math.min(stack.getRemainingSpace(), maxOutputCount);
						
						int multiplier = (int) (outputSpace / output.amount());
						if(multiplier < fluidMultiplier)
						{
							fluidMultiplier = multiplier;
						}
						if(fluidMultiplier <= 1)
						{
							break outer;
						}
					}
				}
			}
		}
		
		return fluidMultiplier;
	}
	
	@Override
	protected boolean takeItemInputs(RecipeHolder<MachineRecipe> recipeHolder, boolean simulate)
	{
		MachineRecipe recipe = recipeHolder.value();
		
		List<ConfigurableItemStack> baseList = inventory.getItemInputs();
		List<ConfigurableItemStack> stacks = simulate ? ConfigurableItemStack.copyList(baseList) : baseList;
		
		boolean ok = true;
		for(MachineRecipe.ItemInput input : recipe.itemInputs)
		{
			// if we are not simulating, there is a chance we don't need to take this output
			if(!simulate && input.probability() < 1)
			{
				if(ThreadLocalRandom.current().nextFloat() >= input.probability())
				{
					continue;
				}
			}
			int remainingAmount = input.amount() * (input.probability() == 0 ? 1 : tryRecipeMultiplier);
			for(ConfigurableItemStack stack : stacks)
			{
				if(stack.getAmount() > 0 && input.matches(stack.getResource().toStack()))
				{
					int taken = Math.min((int) stack.getAmount(), remainingAmount);
					if(taken > 0 && !simulate)
					{
						behavior.getStatsOrDummy().addUsedItems(stack.getResource().getItem(), taken);
					}
					stack.decrement(taken);
					remainingAmount -= taken;
					if(remainingAmount == 0)
					{
						break;
					}
				}
			}
			if(remainingAmount > 0)
			{
				ok = false;
			}
		}
		
		return ok;
	}
	
	@Override
	protected boolean takeFluidInputs(RecipeHolder<MachineRecipe> recipeHolder, boolean simulate)
	{
		MachineRecipe recipe = recipeHolder.value();
		
		List<ConfigurableFluidStack> baseList = inventory.getFluidInputs();
		List<ConfigurableFluidStack> stacks = simulate ? ConfigurableFluidStack.copyList(baseList) : baseList;
		
		boolean ok = true;
		for(MachineRecipe.FluidInput input : recipe.fluidInputs)
		{
			// if we are not simulating, there is a chance we don't need to take this output
			if(!simulate && input.probability() < 1)
			{
				if(ThreadLocalRandom.current().nextFloat() >= input.probability())
				{
					continue;
				}
			}
			long remainingAmount = input.amount() * (input.probability() == 0 ? 1 : tryRecipeMultiplier);
			for(ConfigurableFluidStack stack : stacks)
			{
				if(stack.getResource().equals(FluidVariant.of(input.fluid())))
				{
					long taken = Math.min(remainingAmount, stack.getAmount());
					if(taken > 0 && !simulate)
					{
						behavior.getStatsOrDummy().addUsedFluids(stack.getResource().getFluid(), taken);
					}
					stack.decrement(taken);
					remainingAmount -= taken;
					if(remainingAmount == 0)
					{
						break;
					}
				}
			}
			if(remainingAmount > 0)
			{
				ok = false;
			}
		}
		return ok;
	}
	
	@Override
	protected boolean putItemOutputs(RecipeHolder<MachineRecipe> recipeHolder, boolean simulate, boolean toggleLock)
	{
		MachineRecipe recipe = recipeHolder.value();
		int recipeMultiplier = tryRecipeMultiplier == 0 ? this.recipeMultiplier : tryRecipeMultiplier;
		
		List<ConfigurableItemStack> baseList = inventory.getItemOutputs();
		List<ConfigurableItemStack> stacks = simulate ? ConfigurableItemStack.copyList(baseList) : baseList;
		
		List<Integer> locksToToggle = new ArrayList<>();
		List<Item> lockItems = new ArrayList<>();
		
		boolean ok = true;
		for(MachineRecipe.ItemOutput output : recipe.itemOutputs)
		{
			if(output.probability() < 1)
			{
				if(simulate)
				{
					continue; // don't check output space for probabilistic recipes
				}
				float randFloat = ThreadLocalRandom.current().nextFloat();
				if(randFloat > output.probability())
				{
					continue;
				}
			}
			int remainingAmount = output.amount() * recipeMultiplier;
			// Try to insert in non-empty stacks or locked first, then also allow insertion
			// in empty stacks.
			for(int loopRun = 0; loopRun < 2; loopRun++)
			{
				int stackId = 0;
				for(ConfigurableItemStack stack : stacks)
				{
					stackId++;
					ItemVariant key = stack.getResource();
					if(key.getItem() == output.variant().getItem() || key.isBlank())
					{
						// If simulating or chanced output, respect the adjusted capacity.
						// If putting the output, don't respect the adjusted capacity in case it was
						// reduced during the processing.
						int remainingCapacity = simulate || output.probability() < 1 ? (int) stack.getRemainingCapacityFor(output.variant())
								: output.variant().getMaxStackSize() - (int) stack.getAmount();
						int ins = Math.min(remainingAmount, remainingCapacity);
						if(key.isBlank())
						{
							if((stack.isMachineLocked() || stack.isPlayerLocked() || loopRun == 1) && stack.isValid(new ItemStack(output.variant().getItem())))
							{
								stack.setAmount(ins);
								stack.setKey(output.variant());
							}
							else
							{
								ins = 0;
							}
						}
						else
						{
							stack.increment(ins);
						}
						remainingAmount -= ins;
						if(ins > 0)
						{
							locksToToggle.add(stackId - 1);
							lockItems.add(output.variant().getItem());
							if(!simulate)
							{
								behavior.getStatsOrDummy().addProducedItems(behavior.getCrafterWorld(), output.variant().getItem(), ins);
							}
						}
						if(remainingAmount == 0)
						{
							break;
						}
					}
				}
			}
			if(remainingAmount > 0)
			{
				ok = false;
			}
		}
		
		if(toggleLock)
		{
			for(int i = 0; i < locksToToggle.size(); i++)
			{
				baseList.get(locksToToggle.get(i)).enableMachineLock(lockItems.get(i));
			}
		}
		return ok;
	}
	
	@Override
	protected boolean putFluidOutputs(RecipeHolder<MachineRecipe> recipeHolder, boolean simulate, boolean toggleLock)
	{
		MachineRecipe recipe = recipeHolder.value();
		int recipeMultiplier = tryRecipeMultiplier == 0 ? this.recipeMultiplier : tryRecipeMultiplier;
		
		List<ConfigurableFluidStack> baseList = inventory.getFluidOutputs();
		List<ConfigurableFluidStack> stacks = simulate ? ConfigurableFluidStack.copyList(baseList) : baseList;
		
		List<Integer> locksToToggle = new ArrayList<>();
		List<Fluid> lockFluids = new ArrayList<>();
		
		boolean ok = true;
		for(int i = 0; i < Math.min(recipe.fluidOutputs.size(), behavior.getMaxFluidOutputs()); ++i)
		{
			MachineRecipe.FluidOutput output = recipe.fluidOutputs.get(i);
			if(output.probability() < 1)
			{
				if(simulate)
				{
					continue; // don't check output space for probabilistic recipes
				}
				float randFloat = ThreadLocalRandom.current().nextFloat();
				if(randFloat > output.probability())
				{
					continue;
				}
			}
			// First, try to find a slot that contains the fluid. If we couldn't find one,
			// we insert in any stack
			outer:
			for(int tries = 0; tries < 2; ++tries)
			{
				for(int j = 0; j < stacks.size(); j++)
				{
					ConfigurableFluidStack stack = stacks.get(j);
					FluidVariant outputKey = FluidVariant.of(output.fluid());
					if(stack.isResourceAllowedByLock(outputKey)
							&& ((tries == 1 && stack.isResourceBlank()) || stack.getResource().equals(outputKey)))
					{
						long inserted = Math.min(output.amount() * recipeMultiplier, stack.getRemainingSpace());
						if(inserted > 0)
						{
							stack.setKey(outputKey);
							stack.increment(inserted);
							locksToToggle.add(j);
							lockFluids.add(output.fluid());
							if(!simulate)
							{
								behavior.getStatsOrDummy().addProducedFluids(output.fluid(), inserted);
							}
						}
						if(inserted < output.amount() * recipeMultiplier)
						{
							ok = false;
						}
						break outer;
					}
				}
				if(tries == 1)
				{
					ok = false;
				}
			}
		}
		
		if(toggleLock)
		{
			for(int i = 0; i < locksToToggle.size(); i++)
			{
				baseList.get(locksToToggle.get(i)).enableMachineLock(lockFluids.get(i));
			}
		}
		return ok;
	}
	
	@Override
	protected boolean tryStartRecipe(RecipeHolder<MachineRecipe> recipe)
	{
		if(this.getMaxMultiplier() > 1)
		{
			int itemInputMultiplier = this.calculateItemInputRecipeMultiplier(recipe.value());
			if(itemInputMultiplier > 1)
			{
				int itemOutputMultiplier = this.calculateItemOutputRecipeMultiplier(recipe.value());
				if(itemOutputMultiplier > 1)
				{
					int itemMultiplier = Math.min(itemInputMultiplier, itemOutputMultiplier);
					
					int fluidInputMultiplier = this.calculateFluidInputRecipeMultiplier(recipe.value());
					if(fluidInputMultiplier > 1)
					{
						int fluidOutputMultiplier = this.calculateFluidOutputRecipeMultiplier(recipe.value());
						if(fluidOutputMultiplier > 1)
						{
							int fluidMultiplier = Math.min(fluidInputMultiplier, fluidOutputMultiplier);
							
							tryRecipeMultiplier = Math.min(itemMultiplier, fluidMultiplier);
							if(super.tryStartRecipe(recipe))
							{
								recipeMultiplier = tryRecipeMultiplier;
								tryRecipeMultiplier = 0;
								return true;
							}
						}
					}
				}
			}
		}
		
		tryRecipeMultiplier = 1;
		recipeMultiplier = 1;
		boolean success = super.tryStartRecipe(recipe);
		tryRecipeMultiplier = 0;
		return success;
	}
	
	@Override
	public void writeNbt(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.writeNbt(tag, registries);
		tag.putInt("recipeMultiplier", recipeMultiplier);
	}
	
	@Override
	public void readNbt(CompoundTag tag, HolderLookup.Provider registries, boolean isUpgradingMachine)
	{
		super.readNbt(tag, registries, isUpgradingMachine);
		recipeMultiplier = tag.getInt("recipeMultiplier");
	}
	
	@Override
	protected void clearActiveRecipes()
	{
		super.clearActiveRecipes();
		recipeMultiplier = 0;
	}
	
	@Override
	public void lockRecipe(ResourceLocation recipeId, Inventory inventory)
	{
		// Find MachineRecipe
		MachineRecipeType recipeType = this.getRecipeType();
		if(recipeType == null)
		{
			return;
		}
		Optional<RecipeHolder<MachineRecipe>> optionalMachineRecipe = recipeType.getRecipes(behavior.getCrafterWorld()).stream()
				.filter((recipe) -> recipe.id().equals(recipeId)).findFirst();
		if(optionalMachineRecipe.isEmpty())
		{
			return;
		}
		RecipeHolder<MachineRecipe> recipe = optionalMachineRecipe.get();
		// ITEM INPUTS
		outer:
		for(MachineRecipe.ItemInput input : recipe.value().itemInputs)
		{
			for(ConfigurableItemStack stack : this.inventory.getItemInputs())
			{
				if(stack.getLockedInstance() != null && input.matches(new ItemStack(stack.getLockedInstance())))
				{
					continue outer;
				}
			}
			Item targetItem = null;
			// Find the first match in the player inventory (useful for logs for example)
			for(int i = 0; i < inventory.getContainerSize(); i++)
			{
				ItemStack playerStack = inventory.getItem(i);
				if(!playerStack.isEmpty() && input.matches(new ItemStack(playerStack.getItem())))
				{
					targetItem = playerStack.getItem();
					break;
				}
			}
			if(targetItem == null)
			{
				// Find the first match that is an item from MI (useful for ingots for example)
				for(Item item : input.getInputItems())
				{
					ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
					if(id.getNamespace().equals(MI.ID))
					{
						targetItem = item;
						break;
					}
				}
			}
			if(targetItem == null)
			{
				// If there is only one value in the tag, pick that one
				if(input.getInputItems().size() == 1)
				{
					targetItem = input.getInputItems().get(0);
				}
			}
			
			if(targetItem != null)
			{
				AbstractConfigurableStack.playerLockNoOverride(targetItem, this.inventory.getItemInputs());
			}
		}
		// ITEM OUTPUTS
		outer:
		for(MachineRecipe.ItemOutput output : recipe.value().itemOutputs)
		{
			for(ConfigurableItemStack stack : this.inventory.getItemOutputs())
			{
				if(stack.getLockedInstance() == output.variant().getItem())
					continue outer;
			}
			AbstractConfigurableStack.playerLockNoOverride(output.variant().getItem(), this.inventory.getItemOutputs());
		}
		
		// FLUID INPUTS
		outer:
		for(MachineRecipe.FluidInput input : recipe.value().fluidInputs)
		{
			for(ConfigurableFluidStack stack : this.inventory.getFluidInputs())
			{
				if(stack.isLockedTo(input.fluid()))
				{
					continue outer;
				}
			}
			AbstractConfigurableStack.playerLockNoOverride(input.fluid(), this.inventory.getFluidInputs());
		}
		// FLUID OUTPUTS
		outer:
		for(MachineRecipe.FluidOutput output : recipe.value().fluidOutputs)
		{
			for(ConfigurableFluidStack stack : this.inventory.getFluidOutputs())
			{
				if(stack.isLockedTo(output.fluid()))
					continue outer;
			}
			AbstractConfigurableStack.playerLockNoOverride(output.fluid(), this.inventory.getFluidOutputs());
		}
		
		// LOCK ITEMS
		if(!recipe.value().itemInputs.isEmpty() || !recipe.value().itemOutputs.isEmpty())
		{
			lockAll(this.inventory.getItemInputs());
			lockAll(this.inventory.getItemOutputs());
		}
		// LOCK FLUIDS
		if(!recipe.value().fluidInputs.isEmpty() || !recipe.value().fluidOutputs.isEmpty())
		{
			lockAll(this.inventory.getFluidInputs());
			lockAll(this.inventory.getFluidOutputs());
		}
	}
}
