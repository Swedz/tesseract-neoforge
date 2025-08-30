package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.components.MachineInventoryComponent;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.helper.MachineInventoryHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineRecipePredicate;

import java.util.function.Consumer;

public final class MachineRecipeCategoryBuilder
{
	private final boolean           isMultiblock;
	private final SteamMode         steamMode;
	private final MachineRecipeType recipeType;
	
	private SlotPositions itemInputPositions  = new SlotPositions.Builder().build();
	private SlotPositions itemOutputPositions = new SlotPositions.Builder().build();
	
	private SlotPositions fluidInputPositions  = new SlotPositions.Builder().build();
	private SlotPositions fluidOutputPositions = new SlotPositions.Builder().build();
	
	ProgressBar.Parameters progressBar;
	
	private MachineRecipePredicate predicate = (recipe) -> true;
	
	MachineRecipeCategoryBuilder(boolean isMultiblock, SteamMode steamMode, MachineRecipeType recipeType)
	{
		this.isMultiblock = isMultiblock;
		this.steamMode = steamMode;
		this.recipeType = recipeType;
	}
	
	public MachineRecipeCategoryBuilder items(Consumer<SlotPositions.Builder> input,
											  Consumer<SlotPositions.Builder> output)
	{
		this.itemInputPositions = new SlotPositions.Builder().buildWithConsumer(input);
		this.itemOutputPositions = new SlotPositions.Builder().buildWithConsumer(output);
		return this;
	}
	
	public MachineRecipeCategoryBuilder fluids(Consumer<SlotPositions.Builder> input,
											   Consumer<SlotPositions.Builder> output)
	{
		this.fluidInputPositions = new SlotPositions.Builder().buildWithConsumer(input);
		this.fluidOutputPositions = new SlotPositions.Builder().buildWithConsumer(output);
		return this;
	}
	
	public MachineRecipeCategoryBuilder progressBar(int renderX, int renderY, String progressBarType, boolean isVertical)
	{
		progressBar = new ProgressBar.Parameters(renderX, renderY, progressBarType, isVertical);
		return this;
	}
	
	public MachineRecipeCategoryBuilder progressBar(int renderX, int renderY, String progressBarType)
	{
		return this.progressBar(renderX, renderY, progressBarType, false);
	}
	
	public MachineRecipeCategoryBuilder predicate(MachineRecipePredicate predicate)
	{
		Assert.notNull(predicate);
		this.predicate = predicate;
		return this;
	}
	
	private static SlotPositions combine(SlotPositions a, SlotPositions b)
	{
		var combined = new SlotPositions.Builder();
		for(int i = 0; i < a.size(); i++)
		{
			combined.addSlot(a.getX(i), a.getY(i));
		}
		for(int i = 0; i < b.size(); i++)
		{
			combined.addSlot(b.getX(i), b.getY(i));
		}
		return combined.build();
	}
	
	private SlotPositions itemPositions()
	{
		return combine(itemInputPositions, itemOutputPositions);
	}
	
	private SlotPositions fluidPositions()
	{
		return combine(fluidInputPositions, fluidOutputPositions);
	}
	
	boolean hasInputs()
	{
		return itemInputPositions.size() > 0 || fluidInputPositions.size() > 0;
	}
	
	boolean hasOutputs()
	{
		return itemOutputPositions.size() > 0 || fluidOutputPositions.size() > 0;
	}
	
	boolean hasItems()
	{
		return itemInputPositions.size() > 0 || itemOutputPositions.size() > 0;
	}
	
	boolean hasFluids()
	{
		return fluidInputPositions.size() > 0 || fluidOutputPositions.size() > 0;
	}
	
	MachineInventoryComponent buildInventory(int steamBuckets, int bucketCapacity)
	{
		return MachineInventoryHelper.buildInventoryComponent(
				itemInputPositions.size(), itemOutputPositions.size(),
				fluidInputPositions.size(), fluidOutputPositions.size(),
				this.itemPositions(), this.fluidPositions(),
				steamBuckets, bucketCapacity
		);
	}
	
	void build(MIHook hook,
			   String name, String englishName,
			   int tiers)
	{
		Assert.notNull(progressBar, "Progress bar must be configured");
		Assert.that(this.hasInputs() && this.hasOutputs(), "At least one input and one output slot must be provided");
		
		HackedMachineRegistrationHelper.registerReiTiers(
				hook,
				englishName, name, recipeType,
				// The null and false constants are populated by registerReiTiers
				new MachineCategoryParams(
						null, null,
						itemInputPositions, itemOutputPositions,
						fluidInputPositions, fluidOutputPositions,
						progressBar,
						null, null, false, null
				),
				tiers
		);
	}
	
	void build(MIHook hook,
			   String name, String englishName)
	{
		Assert.notNull(progressBar, "Progress bar must be configured");
		Assert.that(this.hasInputs() && this.hasOutputs(), "At least one input and one output slot must be provided");
		
		HackedMachineRegistrationHelper.registerRecipeCategory(
				hook,
				name, englishName, recipeType,
				itemInputPositions, itemOutputPositions,
				fluidInputPositions, fluidOutputPositions,
				progressBar,
				(recipe) -> predicate.test(recipe),
				isMultiblock, steamMode
		);
	}
}
