package net.swedz.tesseract.neoforge.compat.mi.hack;

import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.ElectricCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.SteamCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.components.MachineInventoryComponent;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.EnergyBar;
import aztech.modern_industrialization.machines.guicomponents.ProgressBar;
import aztech.modern_industrialization.machines.guicomponents.RecipeEfficiencyBar;
import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.init.MachineRegistrationHelper;
import aztech.modern_industrialization.machines.init.MachineTier;
import aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines;
import aztech.modern_industrialization.machines.models.MachineCasing;
import aztech.modern_industrialization.machines.models.MachineCasings;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.util.MobSpawning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.mixin.accessor.MIMachineRecipeTypesAccessor;
import net.swedz.tesseract.neoforge.registry.common.CommonLootTableBuilders;
import net.swedz.tesseract.neoforge.registry.common.CommonModelBuilders;
import net.swedz.tesseract.neoforge.registry.holder.BlockWithItemHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines.*;

/**
 * The methods in this helper class are copied from various places in MI's source code and modified to respect the registries and namespaces of whatever mod is registering the machine, rei categories, or recipe types.
 */
public final class HackedMachineRegistrationHelper
{
	/**
	 * @see MachineRegistrationHelper#registerMachine(String, String, Function, Consumer[])
	 */
	@SafeVarargs
	public static Supplier<BlockEntityType<?>> registerMachine(String englishName, String name,
															   Function<BEP, MachineBlockEntity> factory,
															   Consumer<BlockEntityType<?>>... extraRegistrators)
	{
		MIHookRegistry registry = MIHooks.getRegistry(MIHookTracker.getTrackingModId());
		ResourceLocation id = MIHookTracker.id(name);
		
		AtomicReference<BlockEntityType<?>> bet = new AtomicReference<>();
		BiFunction<BlockPos, BlockState, MachineBlockEntity> ctor = (pos, state) -> factory.apply(new BEP(bet.get(), pos, state));
		
		BlockWithItemHolder<?, ?> blockHolder = new BlockWithItemHolder<>(
				id, englishName,
				registry.blockRegistry(), (p) -> new MachineBlock(ctor, p),
				registry.itemRegistry(), BlockItem::new
		);
		blockHolder.item().sorted(registry.sortOrderMachines());
		blockHolder
				.tag(BlockTags.NEEDS_STONE_TOOL, BlockTags.MINEABLE_WITH_PICKAXE)
				.withLootTable(CommonLootTableBuilders::self)
				.withProperties((p) -> p
						.mapColor(MapColor.METAL)
						.destroyTime(4)
						.requiresCorrectToolForDrops()
						.isValidSpawn(MobSpawning.NO_SPAWN))
				.withModel((holder) -> (provider) ->
				{
					MIHookTracker.MachineModelProperties machineModelProperties = MIHookTracker.getMachineModel(id);
					if(machineModelProperties == null)
					{
						CommonModelBuilders.blockstateOnly(holder).accept(provider);
						return;
					}
					provider.simpleBlockWithItem(BuiltInRegistries.BLOCK.get(id), provider.models()
							.getBuilder(name)
							.customLoader((bmb, exFile) -> new FakedMachineModelBuilder<>(machineModelProperties, bmb, exFile))
							.end());
				})
				.register();
		
		registry.onBlockRegister(blockHolder);
		registry.onItemRegister(blockHolder.item());
		
		return registry.blockEntityRegistry().register(name, () ->
		{
			Block block = blockHolder.get();
			
			bet.set(BlockEntityType.Builder.of(ctor::apply, block).build(null));
			
			for(Consumer<BlockEntityType<?>> extraRegistrator : extraRegistrators)
			{
				extraRegistrator.accept(bet.get());
			}
			
			registry.onBlockEntityRegister(bet.get());
			
			return bet.get();
		});
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, MachineCasing, boolean, boolean, boolean, boolean)
	 */
	public static void addMachineModel(String name, MachineCasing defaultCasing, String overlay, boolean front, boolean top, boolean side, boolean active)
	{
		MIHookTracker.addMachineModel(MIHookTracker.id(name), defaultCasing, overlay, front, top, side, active);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, MachineCasing, boolean, boolean, boolean)
	 */
	public static void addMachineModel(String name, MachineCasing defaultCasing, String overlay, boolean front, boolean top, boolean side)
	{
		addMachineModel(name, defaultCasing, overlay, front, top, side, true);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, String, boolean, boolean, boolean)
	 */
	public static void addMachineModel(String name, MachineTier tier, String overlay, boolean front, boolean top, boolean side, boolean active)
	{
		MachineCasing defaultCasing = switch (tier)
		{
			case BRONZE -> MachineCasings.BRONZE;
			case STEEL -> MachineCasings.STEEL;
			case LV -> CableTier.LV.casing;
			default -> throw new RuntimeException("Invalid tier: " + tier);
		};
		addMachineModel(name, defaultCasing, overlay, front, top, side, active);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, MachineCasing, boolean, boolean, boolean, boolean)
	 */
	public static void addMachineModel(String name, MachineTier tier, String overlay, boolean front, boolean top, boolean side)
	{
		addMachineModel(name, tier, overlay, front, top, side, true);
	}
	
	/**
	 * @see SingleBlockCraftingMachines#registerMachineTiers(String, String, MachineRecipeType, int, int, int, int, Consumer, ProgressBar.Parameters, RecipeEfficiencyBar.Parameters, EnergyBar.Parameters, Consumer, Consumer, boolean, boolean, boolean, int, int, Config)
	 */
	public static void registerMachineTiers(String englishName, String machineName, MachineRecipeType type, int itemInputCount, int itemOutputCount,
											int fluidInputCount,
											int fluidOutputCount, Consumer<MachineGuiParameters.Builder> guiParams, ProgressBar.Parameters progressBarParams,
											RecipeEfficiencyBar.Parameters efficiencyBarParams, EnergyBar.Parameters energyBarParams, Consumer<SlotPositions.Builder> itemPositions,
											Consumer<SlotPositions.Builder> fluidPositions, boolean frontOverlay, boolean topOverlay, boolean sideOverlay, int tiers,
											int ioBucketCapacity, SingleBlockCraftingMachines.Config extraConfig)
	{
		for(int i = 0; i < 2; ++i)
		{
			if(i == 0 && (tiers & TIER_BRONZE) == 0)
			{
				continue;
			}
			if(i == 1 && (tiers & TIER_STEEL) == 0)
			{
				continue;
			}
			
			SlotPositions items = new SlotPositions.Builder().buildWithConsumer(itemPositions);
			SlotPositions fluids = new SlotPositions.Builder().addSlot(12, 35).buildWithConsumer(fluidPositions);
			MachineTier tier = i == 0 ? MachineTier.BRONZE : MachineTier.STEEL;
			String prefix = i == 0 ? "bronze" : "steel";
			String englishPrefix = i == 0 ? "Bronze " : "Steel ";
			int steamBuckets = i == 0 ? 2 : 4;
			String id = prefix + "_" + machineName;
			MachineGuiParameters.Builder guiParamsBuilder = new MachineGuiParameters.Builder(MIHookTracker.id(id), true);
			guiParams.accept(guiParamsBuilder);
			MachineGuiParameters builtGuiParams = guiParamsBuilder.build();
			
			registerMachine(englishPrefix + englishName, id,
					(bet) -> new SteamCraftingMachineBlockEntity(
							bet, type,
							buildComponent(itemInputCount, itemOutputCount, fluidInputCount, fluidOutputCount, items, fluids, steamBuckets, ioBucketCapacity),
							builtGuiParams, progressBarParams, tier, extraConfig.steamOverclockCatalysts
					),
					(bet) ->
					{
						if(itemInputCount + itemOutputCount > 0)
						{
							MachineBlockEntity.registerItemApi(bet);
						}
						MachineBlockEntity.registerFluidApi(bet);
					}
			);
			addMachineModel(id, tier, machineName, frontOverlay, topOverlay, sideOverlay);
		}
		if((tiers & TIER_ELECTRIC) > 0)
		{
			SlotPositions items = new SlotPositions.Builder().buildWithConsumer(itemPositions);
			SlotPositions fluids = new SlotPositions.Builder().buildWithConsumer(fluidPositions);
			
			String id = tiers == TIER_ELECTRIC ? machineName : "electric_" + machineName;
			
			MachineGuiParameters.Builder guiParamsBuilder = new MachineGuiParameters.Builder(MIHookTracker.id(id), true);
			guiParams.accept(guiParamsBuilder);
			MachineGuiParameters builtGuiParams = guiParamsBuilder.build();
			
			String electricEnglishName = englishName;
			
			if((tiers & TIER_BRONZE) > 0 | (tiers & TIER_STEEL) > 0)
			{
				electricEnglishName = "Electric " + englishName;
			}
			
			registerMachine(
					electricEnglishName, id,
					(bet) -> new ElectricCraftingMachineBlockEntity(
							bet, type,
							buildComponent(itemInputCount, itemOutputCount, fluidInputCount, fluidOutputCount, items, fluids, 0, ioBucketCapacity),
							builtGuiParams,
							energyBarParams, progressBarParams, efficiencyBarParams, MachineTier.LV, 3200
					),
					(bet) ->
					{
						ElectricCraftingMachineBlockEntity.registerEnergyApi(bet);
						if(itemInputCount + itemOutputCount > 0)
						{
							MachineBlockEntity.registerItemApi(bet);
						}
						if(fluidInputCount + fluidOutputCount > 0)
						{
							MachineBlockEntity.registerFluidApi(bet);
						}
					}
			);
			addMachineModel(id, MachineTier.LV, machineName, frontOverlay, topOverlay, sideOverlay);
		}
		
		SlotPositions items = new SlotPositions.Builder().buildWithConsumer(itemPositions);
		SlotPositions fluids = new SlotPositions.Builder().buildWithConsumer(fluidPositions);
		registerReiTiers(
				englishName, machineName, type,
				new MachineCategoryParams(
						null, null, items.sublist(0, itemInputCount),
						items.sublist(itemInputCount, itemInputCount + itemOutputCount),
						fluids.sublist(0, fluidInputCount), fluids.sublist(fluidInputCount, fluidInputCount + fluidOutputCount), progressBarParams,
						null, null, false, SteamMode.BOTH
				),
				tiers
		);
	}
	
	/**
	 * @see SingleBlockCraftingMachines#registerReiTiers(String, String, MachineRecipeType, MachineCategoryParams, int)
	 */
	private static void registerReiTiers(String englishName, String machine, MachineRecipeType recipeType, MachineCategoryParams categoryParams, int tiers)
	{
		List<MachineCategoryParams> previousCategories = new ArrayList<>();
		int previousMaxEu = 0;
		for(int i = 0; i < 3; ++i)
		{
			if(((tiers >> i) & 1) > 0)
			{
				int minEu = previousMaxEu + 1;
				int maxEu = i == 0 ? 2 : i == 1 ? 4 : Integer.MAX_VALUE;
				String prefix = i == 0 ? "bronze_" : i == 1 ? "steel_" : tiers == TIER_ELECTRIC ? "" : "electric_";
				ResourceLocation itemId = MIHookTracker.id(prefix + machine);
				String englishPrefix = i == 0 ? "Bronze " : i == 1 ? "Steel " : "Electric ";
				String fullEnglishName = tiers == TIER_ELECTRIC || previousMaxEu == 0 ? englishName : englishPrefix + englishName;
				MachineCategoryParams category = new MachineCategoryParams(
						fullEnglishName, itemId, categoryParams.itemInputs,
						categoryParams.itemOutputs,
						categoryParams.fluidInputs, categoryParams.fluidOutputs, categoryParams.progressBarParams,
						recipeType, (recipe) -> minEu <= recipe.eu && recipe.eu <= maxEu, false,
						i < 2 ? SteamMode.BOTH : SteamMode.ELECTRIC_ONLY
				);
				MIHookTracker.addReiCategoryName(itemId, fullEnglishName);
				ReiMachineRecipes.registerCategory(itemId, category);
				ReiMachineRecipes.registerMachineClickArea(itemId, categoryParams.progressBarParams.toRectangle());
				previousCategories.add(category);
				for(MachineCategoryParams param : previousCategories)
				{
					param.workstations.add(itemId);
					ReiMachineRecipes.registerRecipeCategoryForMachine(itemId, param.category);
				}
				previousMaxEu = maxEu;
			}
		}
	}
	
	/**
	 * @see SingleBlockCraftingMachines#buildComponent(int, int, int, int, SlotPositions, SlotPositions, int, int)
	 */
	private static MachineInventoryComponent buildComponent(int itemInputCount, int itemOutputCount, int fluidInputCount, int fluidOutputCount,
															SlotPositions itemPositions, SlotPositions fluidPositions, int steamBuckets, int ioBucketCapacity)
	{
		
		List<ConfigurableItemStack> itemInputStacks = new ArrayList<>();
		for(int i = 0; i < itemInputCount; ++i)
		{
			itemInputStacks.add(ConfigurableItemStack.standardInputSlot());
		}
		List<ConfigurableItemStack> itemOutputStacks = new ArrayList<>();
		for(int i = 0; i < itemOutputCount; ++i)
		{
			itemOutputStacks.add(ConfigurableItemStack.standardOutputSlot());
		}
		List<ConfigurableFluidStack> fluidInputStacks = new ArrayList<>();
		if(steamBuckets > 0)
		{
			fluidInputStacks.add(ConfigurableFluidStack.lockedInputSlot(1000L * steamBuckets, MIFluids.STEAM.asFluid()));
		}
		for(int i = 0; i < fluidInputCount; ++i)
		{
			fluidInputStacks.add(ConfigurableFluidStack.standardInputSlot(1000L * ioBucketCapacity));
		}
		List<ConfigurableFluidStack> fluidOutputStacks = new ArrayList<>();
		for(int i = 0; i < fluidOutputCount; ++i)
		{
			fluidOutputStacks.add(ConfigurableFluidStack.standardOutputSlot(1000L * ioBucketCapacity));
		}
		return new MachineInventoryComponent(itemInputStacks, itemOutputStacks, fluidInputStacks, fluidOutputStacks, itemPositions, fluidPositions);
	}
	
	/**
	 * @see MIMachineRecipeTypes#create(String, Function)
	 */
	public static MachineRecipeType createMachineRecipeType(String name, Function<ResourceLocation, MachineRecipeType> creator)
	{
		MIHookRegistry registry = MIHooks.getRegistry(MIHookTracker.getTrackingModId());
		
		MachineRecipeType type = creator.apply(MIHookTracker.id(name));
		registry.recipeSerializerRegistry().register(name, () -> type);
		registry.recipeTypeRegistry().register(name, () -> type);
		registry.onMachineRecipeTypeRegister(type);
		MIMachineRecipeTypesAccessor.getRecipeTypes().add(type);
		
		return type;
	}
}
