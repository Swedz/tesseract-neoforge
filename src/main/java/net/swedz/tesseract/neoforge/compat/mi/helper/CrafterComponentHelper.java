package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.compat.almostunified.AlmostUnifiedFacade;
import aztech.modern_industrialization.inventory.AbstractConfigurableStack;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.stats.PlayerStatistics;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.fluid.FluidVariant;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.storage.TransferVariant;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public final class CrafterComponentHelper
{
	private static boolean takeItemInputs(
			MachineRecipe recipe, boolean simulate,
			CommonBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		List<ConfigurableItemStack> baseList = inventory.getItemInputs();
		long[] used = simulate ? new long[baseList.size()] : null;
		
		boolean ok = true;
		for(var input : recipe.itemInputs)
		{
			// if we are not simulating, there is a chance we don't need to take this output
			if(!simulate && input.probability() < 1)
			{
				if(ThreadLocalRandom.current().nextFloat() >= input.probability())
				{
					continue;
				}
			}
			int remainingAmount = input.amount() * (input.probability() == 0 ? 1 : multiplier);
			for(int stackIndex = 0; stackIndex < baseList.size(); stackIndex++)
			{
				var stack = baseList.get(stackIndex);
				long amount = stack.getAmount();
				if(simulate)
				{
					amount -= used[stackIndex];
				}
				
				if(amount <= 0 || !stack.getResource().test(input.ingredient()))
				{
					continue;
				}
				
				int taken = Math.min((int) amount, remainingAmount);
				if(taken <= 0)
				{
					continue;
				}
				
				if(!simulate)
				{
					behavior.getStatsOrDummy().addUsedItems(stack.getResource().getItem(), taken);
					stack.decrement(taken);
				}
				else
				{
					used[stackIndex] += taken;
				}
				
				remainingAmount -= taken;
				if(remainingAmount == 0)
				{
					break;
				}
			}
			if(remainingAmount > 0)
			{
				ok = false;
			}
		}
		
		return ok;
	}
	
	public static boolean takeItemInputs(
			MachineRecipe recipe,
			boolean simulate,
			CrafterComponent.Behavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return takeItemInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean takeItemInputs(
			MachineRecipe recipe,
			boolean simulate,
			ModularCrafterAccessBehavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return takeItemInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean fluidIngredientMatch(FluidVariant resource, FluidIngredient ingredient)
	{
		if(ingredient.isSimple())
		{
			for(var stack : ingredient.getStacks())
			{
				if(resource.equals(FluidVariant.of(stack.getFluid())))
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return ingredient.test(resource.toStack(1));
		}
	}
	
	private static boolean takeFluidInputs(
			MachineRecipe recipe,
			boolean simulate,
			CommonBehavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier,
			boolean oneInputPerStack
	)
	{
		List<ConfigurableFluidStack> baseList = inventory.getFluidInputs();
		boolean[] usedStacks = oneInputPerStack ? new boolean[baseList.size()] : null;
		long[] used = simulate ? new long[baseList.size()] : null;
		
		boolean ok = true;
		for(var input : recipe.fluidInputs)
		{
			// if we are not simulating, there is a chance we don't need to take this output
			if(!simulate && input.probability() < 1)
			{
				if(ThreadLocalRandom.current().nextFloat() >= input.probability())
				{
					continue;
				}
			}
			
			long remainingAmount = input.amount() * (input.probability() == 0 ? 1 : multiplier);
			for(int stackIndex = 0; stackIndex < baseList.size(); ++stackIndex)
			{
				if(oneInputPerStack && usedStacks[stackIndex])
				{
					continue;
				}
				
				var stack = baseList.get(stackIndex);
				long amount = stack.getAmount();
				if(simulate)
				{
					amount -= used[stackIndex];
				}
				
				if(amount <= 0 || !fluidIngredientMatch(stack.getResource(), input.fluid()))
				{
					continue;
				}
				
				long taken = Math.min(amount, remainingAmount);
				if(taken <= 0)
				{
					continue;
				}
				
				if(oneInputPerStack)
				{
					usedStacks[stackIndex] = true;
				}
				
				if(!simulate)
				{
					behavior.getStatsOrDummy().addUsedFluids(stack.getResource().getFluid(), taken);
					stack.decrement(taken);
				}
				else
				{
					used[stackIndex] += taken;
				}
				
				remainingAmount -= taken;
				if(remainingAmount == 0)
				{
					break;
				}
			}
			if(remainingAmount > 0)
			{
				ok = false;
			}
		}
		return ok;
	}
	
	public static boolean takeFluidInputs(
			MachineRecipe recipe,
			boolean simulate,
			CrafterComponent.Behavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return takeFluidInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier, behavior.oneFluidInputPerStack());
	}
	
	public static boolean takeFluidInputs(
			MachineRecipe recipe,
			boolean simulate,
			ModularCrafterAccessBehavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return takeFluidInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier, behavior.oneFluidInputPerStack());
	}
	
	private record OptimizedUpdatedStacksArray<T>(
			List<T> baseList,
			T[] updatedStacks,
			Function<T, T> copyFunction
	)
	{
		public T get(int index)
		{
			T stack = updatedStacks[index];
			if(stack == null)
			{
				stack = baseList.get(index);
			}
			return stack;
		}
		
		public T getToModify(T baseValue, int index)
		{
			var simStack = updatedStacks[index];
			if(simStack == null)
			{
				simStack = copyFunction.apply(baseValue);
				updatedStacks[index] = simStack;
			}
			return simStack;
		}
	}
	
	private static boolean putItemOutputs(
			MachineRecipe recipe,
			boolean simulate,
			boolean toggleLock,
			CommonBehavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		List<ConfigurableItemStack> baseList = inventory.getItemOutputs();
		var simulatedStacks = simulate ?
				new OptimizedUpdatedStacksArray<>(
						baseList,
						new ConfigurableItemStack[baseList.size()],
						ConfigurableItemStack::new
				) :
				null;
		
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
			int remainingAmount = output.amount() * multiplier;
			// Try to insert in non-empty stacks or locked first, then also allow insertion
			// in empty stacks.
			for(int loopRun = 0; loopRun < 2; loopRun++)
			{
				for(int stackIndex = 0; stackIndex < baseList.size(); stackIndex++)
				{
					var stack = simulate ?
							simulatedStacks.get(stackIndex) :
							baseList.get(stackIndex);
					var key = stack.getResource();
					if(stack.isResourceAllowedByLock(output.variant()) &&
					   (key.equals(output.variant()) || key.isBlank()))
					{
						int remainingCapacity = simulate || output.probability() < 1 || loopRun == 0 ?
								(int) stack.getRemainingCapacityFor(output.variant()) :
								output.variant().getMaxStackSize() - (int) stack.getAmount();
						int ins = Math.min(remainingAmount, remainingCapacity);
						if(ins > 0)
						{
							if(key.isBlank())
							{
								if(stack.isMachineLocked() || stack.isPlayerLocked() || loopRun == 1)
								{
									var stackToModify = simulate ?
											simulatedStacks.getToModify(stack, stackIndex) :
											stack;
									stackToModify.setAmount(ins);
									stackToModify.setKey(output.variant());
								}
								else
								{
									ins = 0;
								}
							}
							else
							{
								var stackToModify = simulate ?
										simulatedStacks.getToModify(stack, stackIndex) :
										stack;
								stackToModify.increment(ins);
							}
						}
						remainingAmount -= ins;
						// ins changed inside of previous if, need to check again!
						if(ins > 0)
						{
							locksToToggle.add(stackIndex);
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
	
	public static boolean putItemOutputs(
			MachineRecipe recipe,
			boolean simulate,
			boolean toggleLock,
			CrafterComponent.Behavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return putItemOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean putItemOutputs(
			MachineRecipe recipe,
			boolean simulate,
			boolean toggleLock,
			ModularCrafterAccessBehavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return putItemOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	private static boolean putFluidOutputs(
			MachineRecipe recipe,
			boolean simulate,
			boolean toggleLock,
			CommonBehavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		List<ConfigurableFluidStack> baseList = inventory.getFluidOutputs();
		var simulatedStacks = simulate ?
				new OptimizedUpdatedStacksArray<>(baseList, new ConfigurableFluidStack[baseList.size()], ConfigurableFluidStack::new) :
				null;
		
		List<Integer> locksToToggle = new ArrayList<>();
		List<Fluid> lockFluids = new ArrayList<>();
		
		boolean ok = true;
		for(int i = 0; i < Math.min(recipe.fluidOutputs.size(), behavior.getMaxFluidOutputs()); ++i)
		{
			var output = recipe.fluidOutputs.get(i);
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
			long outputAmount = output.amount() * multiplier;
			// First, try to find a slot that contains the fluid. If we couldn't find one,
			// we insert in any stack
			outer:
			for(int tries = 0; tries < 2; ++tries)
			{
				for(int stackIndex = 0; stackIndex < baseList.size(); stackIndex++)
				{
					var stack = simulate ?
							simulatedStacks.get(stackIndex) :
							baseList.get(stackIndex);
					var outputKey = FluidVariant.of(output.fluid());
					if(stack.isResourceAllowedByLock(outputKey) &&
					   ((tries == 1 && stack.isResourceBlank()) || stack.getResource().equals(outputKey)))
					{
						long inserted = Math.min(outputAmount, stack.getRemainingSpace());
						if(inserted > 0)
						{
							var stackToModify = simulate ?
									simulatedStacks.getToModify(stack, stackIndex) :
									stack;
							stackToModify.setKey(outputKey);
							stackToModify.increment(inserted);
							locksToToggle.add(stackIndex);
							lockFluids.add(output.fluid());
							if(!simulate)
							{
								behavior.getStatsOrDummy().addProducedFluids(output.fluid(), inserted);
							}
						}
						if(inserted < outputAmount)
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
	
	public static boolean putFluidOutputs(
			MachineRecipe recipe,
			boolean simulate,
			boolean toggleLock,
			CrafterComponent.Behavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return putFluidOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean putFluidOutputs(
			MachineRecipe recipe,
			boolean simulate,
			boolean toggleLock,
			ModularCrafterAccessBehavior behavior,
			CrafterComponent.Inventory inventory,
			int multiplier
	)
	{
		return putFluidOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	private static <T, K extends TransferVariant<T>, S extends AbstractConfigurableStack<T, K>> void handleLocking(
			List<S> stacks,
			Predicate<T> matchesRecipe,
			long requiredAmount,
			Supplier<T> lockTarget
	)
	{
		for(S stack : stacks)
		{
			if(stack.getLockedInstance() != null && matchesRecipe.apply(stack.getLockedInstance()))
			{
				requiredAmount -= stack.getTotalCapacityFor(stack.getLockedInstance());
				if(requiredAmount <= 0)
				{
					// We have all we need already
					return;
				}
			}
		}
		var newLockedInstance = lockTarget.get();
		if(newLockedInstance == null)
		{
			return;
		}
		AbstractConfigurableStack.playerLockNoOverride(newLockedInstance, requiredAmount, stacks);
	}
	
	public static void lockRecipe(MachineRecipe recipe, Inventory playerInventory, CrafterComponent.Inventory inventory)
	{
		// ITEM INPUTS
		for(var input : recipe.itemInputs)
		{
			handleLocking(
					inventory.getItemInputs(),
					(item) -> input.matches(new ItemStack(item)),
					input.amount(),
					() ->
					{
						// Find the first match in the player inventory (useful for logs for example)
						for(int i = 0; i < playerInventory.getContainerSize(); i++)
						{
							ItemStack playerStack = playerInventory.getItem(i);
							if(!playerStack.isEmpty() && input.matches(new ItemStack(playerStack.getItem())))
							{
								return playerStack.getItem();
							}
						}
						List<Item> inputItems = input.getInputItems();
						// Find the preferred item with Almost Unified if possible
						if(!inputItems.isEmpty())
						{
							var targetItem = AlmostUnifiedFacade.INSTANCE.getTargetItem(inputItems.getFirst());
							if(targetItem != null)
							{
								return targetItem;
							}
						}
						// Find the first match that is an item from MI (useful for ingots for example)
						for(Item item : inputItems)
						{
							ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
							if(id.getNamespace().equals(MI.ID))
							{
								return item;
							}
						}
						// If there is only one value in the tag, pick that one
						if(inputItems.size() == 1)
						{
							return inputItems.getFirst();
						}
						return null;
					}
			);
		}
		
		// ITEM OUTPUTS
		for(var output : recipe.itemOutputs)
		{
			handleLocking(
					inventory.getItemOutputs(),
					(item) -> output.variant().isOf(item),
					output.amount(),
					output.variant()::getItem
			);
		}
		
		// FLUID INPUTS
		for(var input : recipe.fluidInputs)
		{
			handleLocking(
					inventory.getFluidInputs(),
					(fluid) -> input.fluid().test(new FluidStack(fluid, 1)),
					input.amount(),
					() ->
					{
						// Find the first match in the player inventory
						for(int i = 0; i < playerInventory.getContainerSize(); i++)
						{
							var playerStack = FluidUtil.getFluidContained(playerInventory.getItem(i)).orElse(FluidStack.EMPTY);
							if(!playerStack.isEmpty() && input.fluid().test(new FluidStack(playerStack.getFluid(), 1)))
							{
								return playerStack.getFluid();
							}
						}
						List<Fluid> inputFluids = input.getInputFluids();
						// Find the first match that is an item from MI
						for(Fluid fluid : inputFluids)
						{
							ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
							if(id.getNamespace().equals(MI.ID))
							{
								return fluid;
							}
						}
						// If there is only one value in the tag, pick that one
						if(inputFluids.size() == 1)
						{
							return inputFluids.getFirst();
						}
						return null;
					}
			);
		}
		
		// FLUID OUTPUTS
		for(var output : recipe.fluidOutputs)
		{
			handleLocking(
					inventory.getFluidOutputs(),
					(fluid) -> output.fluid() == fluid,
					output.amount(),
					output::fluid
			);
		}
		
		// LOCK ITEMS
		if(!recipe.itemInputs.isEmpty() || !recipe.itemOutputs.isEmpty())
		{
			lockAll(inventory.getItemInputs());
			lockAll(inventory.getItemOutputs());
		}
		
		// LOCK FLUIDS
		if(!recipe.fluidInputs.isEmpty() || !recipe.fluidOutputs.isEmpty())
		{
			lockAll(inventory.getFluidInputs());
			lockAll(inventory.getFluidOutputs());
		}
	}
	
	public static void lockAll(List<? extends AbstractConfigurableStack<?, ?>> stacks)
	{
		for(AbstractConfigurableStack stack : stacks)
		{
			if(stack.isEmpty() && stack.getLockedInstance() == null)
			{
				stack.togglePlayerLock();
			}
		}
	}
	
	private interface CommonBehavior
	{
		ServerLevel getCrafterWorld();
		
		int getMaxFluidOutputs();
		
		PlayerStatistics getStatsOrDummy();
		
		boolean oneFluidInputPerStack();
		
		static CommonBehavior from(CrafterComponent.Behavior behavior)
		{
			return new CommonBehavior()
			{
				@Override
				public ServerLevel getCrafterWorld()
				{
					return behavior.getCrafterWorld();
				}
				
				@Override
				public int getMaxFluidOutputs()
				{
					return behavior.getMaxFluidOutputs();
				}
				
				@Override
				public PlayerStatistics getStatsOrDummy()
				{
					return behavior.getStatsOrDummy();
				}
				
				@Override
				public boolean oneFluidInputPerStack()
				{
					return behavior.oneFluidInputPerStack();
				}
			};
		}
		
		static CommonBehavior from(ModularCrafterAccessBehavior behavior)
		{
			return new CommonBehavior()
			{
				@Override
				public ServerLevel getCrafterWorld()
				{
					return behavior.getCrafterWorld();
				}
				
				@Override
				public int getMaxFluidOutputs()
				{
					return behavior.getMaxFluidOutputs();
				}
				
				@Override
				public PlayerStatistics getStatsOrDummy()
				{
					return behavior.getStatsOrDummy();
				}
				
				@Override
				public boolean oneFluidInputPerStack()
				{
					return behavior.oneFluidInputPerStack();
				}
			};
		}
	}
}
