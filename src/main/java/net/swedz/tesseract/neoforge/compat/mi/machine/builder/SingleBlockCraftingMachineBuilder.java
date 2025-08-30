package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.ElectricCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.SteamCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;

import java.util.function.Consumer;
import java.util.function.Function;

import static aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines.*;

public final class SingleBlockCraftingMachineBuilder extends MachineBuilder<SingleBlockCraftingMachineBuilder>
{
	private final MachineRecipeType recipeType;
	
	private int tiers;
	
	private Function<ResourceLocation, MachineGuiParameters> guiParameters = (id) -> new MachineGuiParameters.Builder(id, true).build();
	
	private MachineRecipeCategoryBuilder recipeCategory;
	private int                          bucketCapacity;
	
	private RecipeEfficiencyBar.Parameters efficiencyBar;
	private EnergyBar.Parameters           energyBar;
	
	private Config extraConfig = new Config();
	
	SingleBlockCraftingMachineBuilder(MIHook hook,
									  String name, String englishName,
									  MachineRecipeType recipeType)
	{
		super(hook, name, englishName);
		Assert.notNull(recipeType);
		this.recipeType = recipeType;
	}
	
	@Override
	public SingleBlockCraftingMachineBuilder builtinModel(MachineCasing casing, String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		return super.builtinModel(casing, overlayFolder, (b) ->
		{
			// All crafting machines should use active overlays
			b.active(true);
			builder.accept(b);
		});
	}
	
	public SingleBlockCraftingMachineBuilder bronze()
	{
		tiers |= TIER_BRONZE;
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder steel()
	{
		tiers |= TIER_STEEL;
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder electric()
	{
		tiers |= TIER_ELECTRIC;
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder gui(boolean lockButton, int backgroundHeight)
	{
		guiParameters = (id) -> new MachineGuiParameters.Builder(id, lockButton).backgroundHeight(backgroundHeight).build();
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder gui(int backgroundHeight)
	{
		return this.gui(true, backgroundHeight);
	}
	
	public SingleBlockCraftingMachineBuilder recipeCategory(SteamMode steamMode, Consumer<MachineRecipeCategoryBuilder> builder)
	{
		Assert.noneNull(steamMode, builder);
		recipeCategory = new MachineRecipeCategoryBuilder(false, steamMode, recipeType);
		builder.accept(recipeCategory);
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder fluids(int bucketCapacity)
	{
		this.bucketCapacity = bucketCapacity;
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder efficiencyBar(int renderX, int renderY)
	{
		efficiencyBar = new RecipeEfficiencyBar.Parameters(renderX, renderY);
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder energyBar(int renderX, int renderY)
	{
		energyBar = new EnergyBar.Parameters(renderX, renderY);
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder extra(Consumer<Config> builder)
	{
		builder.accept(extraConfig);
		return this;
	}
	
	private static MachineCasing getCasing(MachineTier tier)
	{
		return switch (tier)
		{
			case BRONZE -> MachineCasings.BRONZE;
			case STEEL -> MachineCasings.STEEL;
			case LV -> CableTier.LV.casing;
			default -> throw new RuntimeException("Invalid tier: " + tier);
		};
	}
	
	@Override
	protected void internalBuild()
	{
		Assert.that(tiers != 0, "At least one tier must be selected");
		Assert.notNull(recipeCategory, "Recipe category must be configured");
		
		// Register the bronze and steel machines
		for(int index = 0; index < 2; ++index)
		{
			if(index == 0 && (tiers & TIER_BRONZE) == 0)
			{
				continue;
			}
			if(index == 1 && (tiers & TIER_STEEL) == 0)
			{
				continue;
			}
			
			MachineTier tier = index == 0 ? MachineTier.BRONZE : MachineTier.STEEL;
			String prefix = index == 0 ? "bronze" : "steel";
			String englishPrefix = index == 0 ? "Bronze " : "Steel ";
			int steamBuckets = index == 0 ? 2 : 4;
			String id = prefix + "_" + name;
			var guiParams = guiParameters.apply(hook.id(id));
			
			HackedMachineRegistrationHelper.registerMachine(
					hook,
					englishPrefix + englishName, id,
					blockFactory, holderModifier, propertiesModifier,
					defaultMineableTags,
					(bet) -> new SteamCraftingMachineBlockEntity(
							bet, recipeType,
							recipeCategory.buildInventory(steamBuckets, bucketCapacity),
							guiParams, recipeCategory.progressBar, tier, extraConfig.steamOverclockCatalysts
					),
					(bet) ->
					{
						if(recipeCategory.hasItems())
						{
							MachineBlockEntity.registerItemApi(bet);
						}
						MachineBlockEntity.registerFluidApi(bet);
					}
			);
			if(builtinModel != null)
			{
				builtinModel.build(hook, id, getCasing(tier));
			}
		}
		
		// Register the electric machine
		if((tiers & TIER_ELECTRIC) > 0)
		{
			Assert.notNull(efficiencyBar, "Progress bar must be configured");
			Assert.notNull(energyBar, "Energy bar must be configured");
			
			String id = tiers == TIER_ELECTRIC ? name : "electric_" + name;
			
			var guiParams = guiParameters.apply(hook.id(id));
			
			String electricEnglishName = englishName;
			
			if((tiers & TIER_BRONZE) > 0 | (tiers & TIER_STEEL) > 0)
			{
				electricEnglishName = "Electric " + englishName;
			}
			
			HackedMachineRegistrationHelper.registerMachine(
					hook,
					electricEnglishName, id,
					blockFactory, holderModifier, propertiesModifier,
					defaultMineableTags,
					(bet) -> new ElectricCraftingMachineBlockEntity(
							bet, recipeType,
							recipeCategory.buildInventory(0, bucketCapacity),
							guiParams, energyBar, recipeCategory.progressBar, efficiencyBar, MachineTier.LV, 3200
					),
					(bet) ->
					{
						ElectricCraftingMachineBlockEntity.registerEnergyApi(bet);
						if(recipeCategory.hasItems())
						{
							MachineBlockEntity.registerItemApi(bet);
						}
						if(recipeCategory.hasFluids())
						{
							MachineBlockEntity.registerFluidApi(bet);
						}
					}
			);
			if(builtinModel != null)
			{
				builtinModel.build(hook, id, getCasing(MachineTier.LV));
			}
		}
		
		recipeCategory.build(hook, name, englishName, tiers);
	}
}
