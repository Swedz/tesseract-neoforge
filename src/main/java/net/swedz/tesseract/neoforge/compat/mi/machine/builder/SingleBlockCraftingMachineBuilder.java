package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.ElectricCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.SteamCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineGuiConfigurator;

import java.util.function.Consumer;

import static aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines.*;

public final class SingleBlockCraftingMachineBuilder extends MachineWithGuiBuilder<SingleBlockCraftingMachineBuilder>
{
	private final MachineRecipeType recipeType;
	
	private int tiers;
	
	private int steamX = 12, steamY = 35;
	
	private Config extraConfig = new Config();
	
	SingleBlockCraftingMachineBuilder(MIHook hook,
									  String name, String englishName,
									  MachineRecipeType recipeType)
	{
		super(hook, name, englishName);
		Assert.notNull(recipeType);
		this.recipeType = recipeType;
	}
	
	/**
	 * Note that the {@link MachineCasing} passed here will be ignored.
	 */
	@Override
	public SingleBlockCraftingMachineBuilder builtinModel(MachineCasing casing, String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		return super.builtinModel(casing, overlayFolder, (b) ->
		{
			// Single block crafting machines by default have a front and active overlay
			b.front().active();
			builder.accept(b);
		});
	}
	
	public SingleBlockCraftingMachineBuilder builtinModel(String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		return this.builtinModel(null, overlayFolder, builder);
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
	
	public SingleBlockCraftingMachineBuilder steamSlotPosition(int x, int y)
	{
		this.steamX = x;
		this.steamY = y;
		return this;
	}
	
	public SingleBlockCraftingMachineBuilder gui(SteamMode steamMode, MachineGuiConfigurator builder)
	{
		return this.gui(false, steamMode, recipeType, builder);
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
	protected void internalRegister()
	{
		Assert.that(tiers != 0, "At least one tier must be selected");
		Assert.notNull(gui, "GUI must be configured");
		Assert.notNull(gui.getProgressBar(), "Progress bar must be configured");
		
		var slotPositions = gui.getSlots().toSlotPositions();
		
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
			var guiParams = gui.createGuiParams(hook.id(id));
			var steamGui = gui.copy().inventoryOnlySlots((s) -> s.fluidInput(steamX, steamY, MIFluids.STEAM::asFluid, steamBuckets));
			
			HackedMachineRegistrationHelper.registerMachine(
					hook,
					englishPrefix + englishName, id,
					blockFactory, holderModifier, propertiesModifier,
					defaultMineableTags,
					(bet) -> new SteamCraftingMachineBlockEntity(
							bet, recipeType,
							steamGui.buildInventory(),
							guiParams, steamGui.getProgressBar(), tier, extraConfig.steamOverclockCatalysts
					),
					(bet) ->
					{
						if(slotPositions.hasItems())
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
			Assert.notNull(gui.getEnergyBar(), "Energy bar must be configured");
			Assert.notNull(gui.getEfficiencyBar(), "Efficiency bar must be configured");
			
			String id = tiers == TIER_ELECTRIC ? name : "electric_" + name;
			
			var guiParams = gui.createGuiParams(hook.id(id));
			
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
							gui.buildInventory(),
							guiParams, gui.getEnergyBar(), gui.getProgressBar(), gui.getEfficiencyBar(), MachineTier.LV, 3200
					),
					(bet) ->
					{
						ElectricCraftingMachineBlockEntity.registerEnergyApi(bet);
						if(slotPositions.hasItems())
						{
							MachineBlockEntity.registerItemApi(bet);
						}
						if(slotPositions.hasFluids())
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
		
		gui.registerRecipeCategory(hook, name, englishName, tiers);
	}
}
