package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CrafterComponent;
import aztech.modern_industrialization.machines.components.MachineInventoryComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineRecipePredicate;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.slots.MachineSlotConfiguration;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MachineGuiConfiguration
{
	private final boolean           isMultiblock;
	private final SteamMode         steamMode;
	private final MachineRecipeType recipeType;
	
	private int     guiHeight  = 166;
	private boolean lockButton = true;
	
	private final MachineSlotConfiguration.Builder inventoryOnlySlots = new MachineSlotConfiguration.Builder();
	private final MachineSlotConfiguration.Builder slots              = new MachineSlotConfiguration.Builder();
	
	private ProgressBar.Params         progressBar;
	private EnergyBar.Params           energyBar;
	private RecipeEfficiencyBar.Params efficiencyBar;
	
	private MachineRecipePredicate predicate = (recipe) -> true;
	
	MachineGuiConfiguration(boolean isMultiblock, SteamMode steamMode, MachineRecipeType recipeType)
	{
		this.isMultiblock = isMultiblock;
		this.steamMode = steamMode;
		this.recipeType = recipeType;
	}
	
	public MachineGuiConfiguration copy()
	{
		var copy = new MachineGuiConfiguration(isMultiblock, steamMode, recipeType);
		copy.guiHeight = guiHeight;
		copy.lockButton = lockButton;
		copy.inventoryOnlySlots.append(inventoryOnlySlots);
		copy.slots.append(slots);
		copy.progressBar = progressBar;
		copy.energyBar = energyBar;
		copy.efficiencyBar = efficiencyBar;
		copy.predicate = predicate;
		return copy;
	}
	
	public boolean isMultiblock()
	{
		return isMultiblock;
	}
	
	public SteamMode getSteamMode()
	{
		return steamMode;
	}
	
	public MachineRecipeType getRecipeType()
	{
		return recipeType;
	}
	
	public int getGuiHeight()
	{
		return guiHeight;
	}
	
	public boolean hasLockButton()
	{
		return lockButton;
	}
	
	public MachineSlotConfiguration getInventoryOnlySlots()
	{
		return inventoryOnlySlots.build();
	}
	
	public MachineSlotConfiguration getSlots()
	{
		return slots.build();
	}
	
	public ProgressBar.Params getProgressBar()
	{
		return progressBar;
	}
	
	public EnergyBar.Params getEnergyBar()
	{
		return energyBar;
	}
	
	public RecipeEfficiencyBar.Params getEfficiencyBar()
	{
		return efficiencyBar;
	}
	
	public MachineRecipePredicate getPredicate()
	{
		return predicate;
	}
	
	public MachineGuiConfiguration guiHeight(int guiHeight)
	{
		this.guiHeight = guiHeight;
		return this;
	}
	
	public MachineGuiConfiguration lockButton(boolean lockButton)
	{
		this.lockButton = lockButton;
		return this;
	}
	
	public MachineGuiConfiguration inventoryOnlySlots(Consumer<MachineSlotConfiguration.Builder> builder)
	{
		builder.accept(inventoryOnlySlots);
		return this;
	}
	
	public MachineGuiConfiguration slots(Consumer<MachineSlotConfiguration.Builder> builder)
	{
		builder.accept(slots);
		return this;
	}
	
	public MachineGuiConfiguration progressBar(int renderX, int renderY, String progressBarType, boolean isVertical)
	{
		this.progressBar = new ProgressBar.Params(renderX, renderY, progressBarType, isVertical);
		return this;
	}
	
	public MachineGuiConfiguration progressBar(int renderX, int renderY, String progressBarType)
	{
		return this.progressBar(renderX, renderY, progressBarType, false);
	}
	
	public MachineGuiConfiguration energyBar(int renderX, int renderY)
	{
		this.energyBar = new EnergyBar.Params(renderX, renderY);
		return this;
	}
	
	public MachineGuiConfiguration efficiencyBar(int renderX, int renderY)
	{
		this.efficiencyBar = new RecipeEfficiencyBar.Params(renderX, renderY);
		return this;
	}
	
	public MachineGuiConfiguration predicate(MachineRecipePredicate predicate)
	{
		Assert.notNull(predicate);
		this.predicate = predicate;
		return this;
	}
	
	public MachineGuiParameters createGuiParams(ResourceLocation blockId)
	{
		return new MachineGuiParameters.Builder(blockId, lockButton)
				.backgroundHeight(guiHeight)
				.build();
	}
	
	public MachineInventoryComponent buildInventory()
	{
		return MachineSlotConfiguration.combine(inventoryOnlySlots.build(), slots.build())
				.build()
				.toInventoryComponent();
	}
	
	public void registerProgressBar(MachineBlockEntity machine, Supplier<Float> progress)
	{
		Assert.noneNull(machine, progress);
		if(progressBar != null)
		{
			machine.guiComponents.register(new ProgressBar(progressBar, progress));
		}
	}
	
	public void registerEnergyBar(MachineBlockEntity machine, Supplier<Long> euSupplier, Supplier<Long> maxEuSupplier)
	{
		Assert.noneNull(machine, euSupplier, maxEuSupplier);
		if(energyBar != null)
		{
			machine.guiComponents.register(new EnergyBar(energyBar, euSupplier, maxEuSupplier));
		}
	}
	
	public void registerEfficiencyBar(MachineBlockEntity machine, CrafterComponent crafter)
	{
		Assert.noneNull(machine, crafter);
		if(efficiencyBar != null)
		{
			machine.guiComponents.register(new RecipeEfficiencyBar(efficiencyBar, crafter));
		}
	}
	
	boolean hasRecipeCategory()
	{
		return recipeType != null;
	}
	
	void registerRecipeCategory(MIHook hook,
								String name, String englishName,
								int tiers)
	{
		Assert.notNull(recipeType, "Recipe type must be provided");
		Assert.notNull(progressBar, "Progress bar must be configured");
		
		var slotPositions = slots.build().toSlotPositions();
		Assert.that(slotPositions.hasInputs() && slotPositions.hasOutputs(), "At least one input and one output slot must be provided");
		
		HackedMachineRegistrationHelper.registerReiTiers(
				hook,
				englishName, name, recipeType,
				// The null and false constants are populated by registerReiTiers
				new MachineCategoryParams(
						null, null,
						slotPositions.itemInputs(), slotPositions.itemOutputs(),
						slotPositions.fluidInputs(), slotPositions.fluidOutputs(),
						progressBar,
						null, null, false, null
				),
				tiers
		);
	}
	
	void registerRecipeCategory(MIHook hook,
								String name, String englishName)
	{
		Assert.notNull(recipeType, "Recipe type must be provided");
		Assert.notNull(progressBar, "Progress bar must be configured");
		
		var slotPositions = slots.build().toSlotPositions();
		Assert.that(slotPositions.hasInputs() && slotPositions.hasOutputs(), "At least one input and one output slot must be provided");
		
		HackedMachineRegistrationHelper.registerRecipeCategory(
				hook,
				name, englishName, recipeType,
				slotPositions.itemInputs(), slotPositions.itemOutputs(),
				slotPositions.fluidInputs(), slotPositions.fluidOutputs(),
				progressBar,
				(recipe) -> predicate.test(recipe),
				isMultiblock, steamMode
		);
	}
}
