package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.compat.almostunified.AlmostUnifiedFacade;
import aztech.modern_industrialization.inventory.AbstractConfigurableStack;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.stats.PlayerStatistics;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.DataComponentHolderResource;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class CrafterComponentHelper
{
	private static boolean takeItemInputs(
			MachineRecipe recipe, boolean simulate,
			CommonBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
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
			int remainingAmount = input.amount() * (input.probability() == 0 ? 1 : multiplier);
			for(ConfigurableItemStack stack : stacks)
			{
				if(stack.getAmount() > 0 && stack.getResource().test(input.ingredient()))
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
	
	public static boolean takeItemInputs(
			MachineRecipe recipe, boolean simulate,
			CrafterComponent.Behavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return takeItemInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean takeItemInputs(
			MachineRecipe recipe, boolean simulate,
			ModularCrafterAccessBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return takeItemInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean fluidIngredientMatch(FluidResource resource, FluidIngredient ingredient)
	{
		if(ingredient.isSimple())
		{
			for(var fluid : ingredient.fluids())
			{
				if(resource.equals(FluidResource.of(fluid.value())))
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
			MachineRecipe recipe, boolean simulate,
			CommonBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
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
			int remainingAmount = input.amount() * (input.probability() == 0 ? 1 : multiplier);
			for(ConfigurableFluidStack stack : stacks)
			{
				if(fluidIngredientMatch(stack.getResource(), input.fluid()))
				{
					int taken = Math.min(remainingAmount, stack.getAmount());
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
	
	public static boolean takeFluidInputs(
			MachineRecipe recipe, boolean simulate,
			CrafterComponent.Behavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return takeFluidInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean takeFluidInputs(
			MachineRecipe recipe, boolean simulate,
			ModularCrafterAccessBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return takeFluidInputs(recipe, simulate, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	private static boolean putItemOutputs(
			MachineRecipe recipe, boolean simulate, boolean toggleLock,
			CommonBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
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
			int remainingAmount = output.template().count() * multiplier;
			// Try to insert in non-empty stacks or locked first, then also allow insertion
			// in empty stacks.
			for(int loopRun = 0; loopRun < 2; loopRun++)
			{
				int stackId = 0;
				for(ConfigurableItemStack stack : stacks)
				{
					stackId++;
					var key = stack.getResource();
					if(key.matches(output.template()) || key.isEmpty())
					{
						var outputVariant = ItemResource.of(output.template().item(), output.template().components());
						// If simulating or chanced output, respect the adjusted capacity.
						// If putting the output, don't respect the adjusted capacity in case it was
						// reduced during the processing.
						int remainingCapacity = simulate || output.probability() < 1 ?
								stack.getRemainingCapacityFor(outputVariant) :
								outputVariant.getMaxStackSize() - stack.getAmount();
						int ins = Math.min(remainingAmount, remainingCapacity);
						if(ins > 0)
						{
							if(key.isEmpty())
							{
								if((stack.isMachineLocked() || stack.isPlayerLocked() || loopRun == 1) && stack.isValid(output.getStack()))
								{
									stack.setAmount(ins);
									stack.setKey(outputVariant);
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
						}
						remainingAmount -= ins;
						// ins changed inside of previous if, need to check again!
						if(ins > 0)
						{
							locksToToggle.add(stackId - 1);
							lockItems.add(outputVariant.getItem());
							if(!simulate)
							{
								behavior.getStatsOrDummy().addProducedItems(behavior.getCrafterWorld(), outputVariant.getItem(), ins);
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
			MachineRecipe recipe, boolean simulate, boolean toggleLock,
			CrafterComponent.Behavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return putItemOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean putItemOutputs(
			MachineRecipe recipe, boolean simulate, boolean toggleLock,
			ModularCrafterAccessBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return putItemOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	private static boolean putFluidOutputs(
			MachineRecipe recipe, boolean simulate, boolean toggleLock,
			CommonBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
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
					var outputKey = FluidResource.of(output.fluid());
					if(stack.isResourceAllowedByLock(outputKey)
					   && ((tries == 1 && stack.isEmpty()) || stack.getResource().equals(outputKey)))
					{
						int inserted = Math.min(output.amount() * multiplier, stack.getRemainingSpace());
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
						if(inserted < output.amount() * multiplier)
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
			MachineRecipe recipe, boolean simulate, boolean toggleLock,
			CrafterComponent.Behavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return putFluidOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	public static boolean putFluidOutputs(
			MachineRecipe recipe, boolean simulate, boolean toggleLock,
			ModularCrafterAccessBehavior behavior, CrafterComponent.Inventory inventory, int multiplier
	)
	{
		return putFluidOutputs(recipe, simulate, toggleLock, CommonBehavior.from(behavior), inventory, multiplier);
	}
	
	private static <T, K extends DataComponentHolderResource<T>, S extends AbstractConfigurableStack<T, K>> void handleLocking(
			List<S> stacks,
			Predicate<T> matchesRecipe,
			int requiredAmount,
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
							Identifier id = BuiltInRegistries.ITEM.getKey(item);
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
					(item) -> output.template().is(item),
					output.template().count(),
					() -> output.template().item().value()
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
							var playerStack = FluidUtil.getFirstStackContained(playerInventory.getItem(i));
							if(!playerStack.isEmpty() && input.fluid().test(new FluidStack(playerStack.getFluid(), 1)))
							{
								return playerStack.getFluid();
							}
						}
						List<Fluid> inputFluids = input.getInputFluids();
						// Find the first match that is an item from MI
						for(Fluid fluid : inputFluids)
						{
							Identifier id = BuiltInRegistries.FLUID.getKey(fluid);
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
			};
		}
	}
}
