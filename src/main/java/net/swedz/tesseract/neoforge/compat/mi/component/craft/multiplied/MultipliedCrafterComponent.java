package net.swedz.tesseract.neoforge.compat.mi.component.craft.multiplied;

import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.inventory.MIItemStorage;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.fluid.FluidVariant;
import aztech.modern_industrialization.thirdparty.fabrictransfer.api.transaction.Transaction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.AbstractModularCrafterComponent;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccessBehavior;
import net.swedz.tesseract.neoforge.compat.mi.helper.CrafterComponentHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
		MachineRecipeType type = this.getRecipeType();
		return type != null ? type.getRecipe(behavior.getCrafterWorld(), recipeId) : null;
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
			
			ServerLevel serverWorld = behavior.getCrafterWorld();
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
		int multiplier = 1;
		int maxMultiplier = this.getMaxMultiplier();
		while(multiplier < maxMultiplier)
		{
			int middleMultiplier = (multiplier + maxMultiplier + 1) / 2;
			if(this.canItemOutputsAllFit(recipe, middleMultiplier))
			{
				multiplier = middleMultiplier;
			}
			else
			{
				maxMultiplier = middleMultiplier - 1;
			}
		}
		return multiplier;
	}
	
	private boolean canItemOutputsAllFit(MachineRecipe recipe, int multiplier)
	{
		try (Transaction transaction = Transaction.openOuter())
		{
			MIItemStorage outputStorage = new MIItemStorage(inventory.getItemOutputs());
			
			for(MachineRecipe.ItemOutput output : recipe.itemOutputs)
			{
				if(output.probability() < 1)
				{
					continue;
				}
				
				int outputAmount = output.amount() * multiplier;
				long inserted = outputStorage.insertAllSlot(output.variant(), outputAmount, transaction);
				if(inserted != outputAmount)
				{
					return false;
				}
			}
		}
		return true;
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
		return CrafterComponentHelper.takeItemInputs(recipeHolder.value(), simulate, behavior, inventory, tryRecipeMultiplier);
	}
	
	@Override
	protected boolean takeFluidInputs(RecipeHolder<MachineRecipe> recipeHolder, boolean simulate)
	{
		return CrafterComponentHelper.takeFluidInputs(recipeHolder.value(), simulate, behavior, inventory, tryRecipeMultiplier);
	}
	
	@Override
	protected boolean putItemOutputs(RecipeHolder<MachineRecipe> recipeHolder, boolean simulate, boolean toggleLock)
	{
		return CrafterComponentHelper.putItemOutputs(recipeHolder.value(), simulate, toggleLock, behavior, inventory, tryRecipeMultiplier == 0 ? recipeMultiplier : tryRecipeMultiplier);
	}
	
	@Override
	protected boolean putFluidOutputs(RecipeHolder<MachineRecipe> recipeHolder, boolean simulate, boolean toggleLock)
	{
		return CrafterComponentHelper.putFluidOutputs(recipeHolder.value(), simulate, toggleLock, behavior, inventory, tryRecipeMultiplier == 0 ? recipeMultiplier : tryRecipeMultiplier);
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
		MachineRecipeType recipeType = this.getRecipeType();
		if(recipeType == null)
		{
			return;
		}
		Optional<RecipeHolder<MachineRecipe>> optionalMachineRecipe = recipeType.getRecipesWithCache(behavior.getCrafterWorld()).stream()
				.filter((recipe) -> recipe.id().equals(recipeId)).findFirst();
		optionalMachineRecipe.ifPresent((recipe) ->
				CrafterComponentHelper.lockRecipe(recipe.value(), inventory, this.inventory));
	}
}
