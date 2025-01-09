package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.inventory.AbstractConfigurableStack;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.stats.PlayerStatistics;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.fluid.FluidVariant;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.item.ItemVariant;
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
	
	public static boolean fluidIngredientMatch(FluidVariant resource, FluidIngredient ingredient)
	{
		if(ingredient.isSimple())
		{
			for(var stack : ingredient.getStacks())
			{
				return resource.equals(FluidVariant.of(stack.getFluid()));
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
			long remainingAmount = input.amount() * (input.probability() == 0 ? 1 : multiplier);
			for(ConfigurableFluidStack stack : stacks)
			{
				if(fluidIngredientMatch(stack.getResource(), input.fluid()))
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
			int remainingAmount = output.amount() * multiplier;
			// Try to insert in non-empty stacks or locked first, then also allow insertion
			// in empty stacks.
			for(int loopRun = 0; loopRun < 2; loopRun++)
			{
				int stackId = 0;
				for(ConfigurableItemStack stack : stacks)
				{
					stackId++;
					ItemVariant key = stack.getResource();
					if(key.equals(output.variant()) || key.isBlank())
					{
						// If simulating or chanced output, respect the adjusted capacity.
						// If putting the output, don't respect the adjusted capacity in case it was
						// reduced during the processing.
						int remainingCapacity = simulate || output.probability() < 1 ?
								(int) stack.getRemainingCapacityFor(output.variant()) :
								output.variant().getMaxStackSize() - (int) stack.getAmount();
						int ins = Math.min(remainingAmount, remainingCapacity);
						if(ins > 0)
						{
							if(key.isBlank())
							{
								if((stack.isMachineLocked() || stack.isPlayerLocked() || loopRun == 1) && stack.isValid(output.getStack()))
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
						}
						remainingAmount -= ins;
						// ins changed inside of previous if, need to check again!
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
					FluidVariant outputKey = FluidVariant.of(output.fluid());
					if(stack.isResourceAllowedByLock(outputKey)
					   && ((tries == 1 && stack.isResourceBlank()) || stack.getResource().equals(outputKey)))
					{
						long inserted = Math.min(output.amount() * multiplier, stack.getRemainingSpace());
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
	
	public static void lockRecipe(MachineRecipe recipe, Inventory playerInventory, CrafterComponent.Inventory inventory)
	{
		// ITEM INPUTS
		outer:
		for(MachineRecipe.ItemInput input : recipe.itemInputs)
		{
			for(ConfigurableItemStack stack : inventory.getItemInputs())
			{
				if(stack.getLockedInstance() != null && input.matches(new ItemStack(stack.getLockedInstance())))
				{
					continue outer;
				}
			}
			Item targetItem = null;
			// Find the first match in the player inventory (useful for logs for example)
			for(int i = 0; i < playerInventory.getContainerSize(); i++)
			{
				ItemStack playerStack = playerInventory.getItem(i);
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
					targetItem = input.getInputItems().getFirst();
				}
			}
			
			if(targetItem != null)
			{
				AbstractConfigurableStack.playerLockNoOverride(targetItem, inventory.getItemInputs());
			}
		}
		// ITEM OUTPUTS
		outer:
		for(MachineRecipe.ItemOutput output : recipe.itemOutputs)
		{
			for(ConfigurableItemStack stack : inventory.getItemOutputs())
			{
				if(stack.getLockedInstance() == output.variant().getItem())
					continue outer;
			}
			AbstractConfigurableStack.playerLockNoOverride(output.variant().getItem(), inventory.getItemOutputs());
		}
		
		// FLUID INPUTS
		outer:
		for(MachineRecipe.FluidInput input : recipe.fluidInputs)
		{
			for(ConfigurableFluidStack stack : inventory.getFluidInputs())
			{
				if(stack.getLockedInstance() != null && input.fluid().test(new FluidStack(stack.getLockedInstance(), 1)))
				{
					continue outer;
				}
			}
			Fluid targetFluid = null;
			// Find the first match in the player inventory
			for(int i = 0; i < playerInventory.getContainerSize(); i++)
			{
				var playerStack = FluidUtil.getFluidContained(playerInventory.getItem(i)).orElse(FluidStack.EMPTY);
				if(!playerStack.isEmpty() && input.fluid().test(new FluidStack(playerStack.getFluid(), 1)))
				{
					targetFluid = playerStack.getFluid();
					break;
				}
			}
			if(targetFluid == null)
			{
				// Find the first match that is an item from MI
				for(Fluid fluid : input.getInputFluids())
				{
					ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
					if(id.getNamespace().equals(MI.ID))
					{
						targetFluid = fluid;
						break;
					}
				}
			}
			if(targetFluid == null)
			{
				// If there is only one value in the tag, pick that one
				if(input.getInputFluids().size() == 1)
				{
					targetFluid = input.getInputFluids().getFirst();
				}
			}
			if(targetFluid != null)
			{
				AbstractConfigurableStack.playerLockNoOverride(targetFluid, inventory.getFluidInputs());
			}
		}
		// FLUID OUTPUTS
		outer:
		for(MachineRecipe.FluidOutput output : recipe.fluidOutputs)
		{
			for(ConfigurableFluidStack stack : inventory.getFluidOutputs())
			{
				if(stack.isLockedTo(output.fluid()))
					continue outer;
			}
			AbstractConfigurableStack.playerLockNoOverride(output.fluid(), inventory.getFluidOutputs());
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
