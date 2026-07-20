package net.swedz.tesseract.neoforge.compat.mi.hack;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.compat.rei.machines.MachineCategoryParams;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.compat.rei.machines.SteamMode;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.MachineBlock;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.ElectricCraftingMachineBlockEntity;
import aztech.modern_industrialization.machines.blockentities.SteamCraftingMachineBlockEntity;
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
import aztech.modern_industrialization.machines.recipe.MachineRecipe;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import aztech.modern_industrialization.util.MobSpawning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.swedz.tesseract.neoforge.compat.mi.helper.MachineInventoryHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookRegistry;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockHolderModifier;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockPropertiesModifier;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;
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
import java.util.function.Predicate;
import java.util.function.Supplier;

import static aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines.*;

/**
 * <p>The methods in this helper class are copied from various places in MI's source code and modified to respect the
 * registries and namespaces of whatever mod is registering the machine, rei categories, or recipe types.</p>
 *
 * <p><b>It is recommended to not use these methods yourself.</b> They are intended for internal use within Tesseract
 * only. If you need to call one of these methods instead of a method in a hook context, there is something missing in
 * the hook context and that should be considered a bug / mistake. Also, I will change these methods without warning.
 * :)</p>
 */
public final class HackedMachineRegistrationHelper
{
	/**
	 * @see MachineRegistrationHelper#registerMachine(String, String, Function, Consumer[])
	 */
	public static Supplier<BlockEntityType<?>> registerMachine(
			MIHook hook,
			String englishName,
			String name,
			MachineBlockFactory blockFactory,
			MachineBlockHolderModifier holderModifier,
			MachineBlockPropertiesModifier overrideProperties,
			boolean defaultMineableTags,
			MachineBlockEntityFactory factory,
			MachineBlockRegistrators... extraRegistrators
	)
	{
		MIHookRegistry registry = hook.registry();
		ResourceLocation id = hook.id(name);
		
		AtomicReference<BlockEntityType<?>> bet = new AtomicReference<>();
		BiFunction<BlockPos, BlockState, MachineBlockEntity> ctor = (pos, state) -> factory.create(new BEP(bet.get(), pos, state));
		
		BlockWithItemHolder<?, ?> blockHolder = new BlockWithItemHolder<>(
				id,
				englishName,
				registry.blockRegistry(),
				(p) ->
						blockFactory == null ?
								new MachineBlock(ctor, p) :
								blockFactory.create(ctor, p),
				registry.itemRegistry(),
				BlockItem::new
		);
		blockHolder.item().sorted(registry.sortOrderMachines());
		if(defaultMineableTags)
		{
			blockHolder.tag(BlockTags.NEEDS_STONE_TOOL, BlockTags.MINEABLE_WITH_PICKAXE);
		}
		blockHolder
				.withLootTable(CommonLootTableBuilders::self)
				.withProperties((properties) ->
				{
					if(overrideProperties != null)
					{
						overrideProperties.modify(properties);
					}
					else
					{
						applyDefaultMachineProperties(properties);
					}
				})
				.withModel((holder) -> (provider) ->
				{
					MIHookTracker.MachineModelProperties machineModelProperties = MIHookTracker.getMachineModel(id);
					if(machineModelProperties == null)
					{
						CommonModelBuilders.blockstateOnly(holder).accept(provider);
						return;
					}
					provider.simpleBlockWithItem(
							BuiltInRegistries.BLOCK.get(id), provider.models()
									.getBuilder(name)
									.customLoader((bmb, exFile) -> new FakedMachineModelBuilder<>(machineModelProperties, bmb, exFile))
									.end()
					);
				});
		if(holderModifier != null)
		{
			holderModifier.modify(blockHolder);
		}
		blockHolder.register();
		
		registry.onBlockRegister(blockHolder);
		registry.onItemRegister(blockHolder.item());
		
		return registry.blockEntityRegistry().register(
				name,
				() ->
				{
					Block block = blockHolder.get();
					
					bet.set(BlockEntityType.Builder.of(ctor::apply, block).build(null));
					
					for(var extraRegistrator : extraRegistrators)
					{
						extraRegistrator.apply(bet.get());
					}
					
					registry.onBlockEntityRegister(bet.get());
					
					return bet.get();
				}
		);
	}
	
	public static Supplier<BlockEntityType<?>> registerMachine(
			MIHook hook,
			String englishName,
			String name,
			MachineBlockFactory blockCreator,
			MachineBlockHolderModifier modifyBlock,
			MachineBlockPropertiesModifier overrideProperties,
			MachineBlockEntityFactory factory,
			MachineBlockRegistrators... extraRegistrators
	)
	{
		return registerMachine(hook, englishName, name, blockCreator, modifyBlock, overrideProperties, true, factory, extraRegistrators);
	}
	
	public static Supplier<BlockEntityType<?>> registerMachine(
			MIHook hook,
			String englishName,
			String name,
			MachineBlockEntityFactory factory,
			MachineBlockRegistrators... extraRegistrators
	)
	{
		return registerMachine(hook, englishName, name, null, null, null, factory, extraRegistrators);
	}
	
	public static void applyDefaultMachineProperties(BlockBehaviour.Properties properties)
	{
		properties
				.mapColor(MapColor.METAL)
				.destroyTime(4)
				.requiresCorrectToolForDrops()
				.isValidSpawn(MobSpawning.NO_SPAWN)
				.isRedstoneConductor(Blocks::never);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, MachineCasing, boolean, boolean, boolean, boolean)
	 */
	public static void addMachineModel(
			MIHook hook,
			String name,
			MachineCasing defaultCasing,
			String overlay,
			boolean front,
			boolean top,
			boolean side,
			boolean active,
			String outputTexture
	)
	{
		MIHookTracker.addMachineModel(hook.id(name), defaultCasing, overlay, front, top, side, active, outputTexture);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, MachineCasing, boolean, boolean, boolean, boolean)
	 */
	public static void addMachineModel(
			MIHook hook,
			String name,
			MachineCasing defaultCasing,
			String overlay,
			boolean front,
			boolean top,
			boolean side,
			boolean active
	)
	{
		addMachineModel(hook, name, defaultCasing, overlay, front, top, side, active, null);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, MachineCasing, boolean, boolean, boolean)
	 */
	public static void addMachineModel(
			MIHook hook,
			String name,
			MachineCasing defaultCasing,
			String overlay,
			boolean front,
			boolean top,
			boolean side
	)
	{
		addMachineModel(hook, name, defaultCasing, overlay, front, top, side, true);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, String, boolean, boolean, boolean)
	 */
	public static void addMachineModel(
			MIHook hook,
			String name,
			MachineTier tier,
			String overlay,
			boolean front,
			boolean top,
			boolean side,
			boolean active
	)
	{
		var defaultCasing = switch(tier)
		{
			case BRONZE -> MachineCasings.BRONZE;
			case STEEL -> MachineCasings.STEEL;
			case LV -> CableTier.LV.casing;
			default -> throw new RuntimeException("Invalid tier: " + tier);
		};
		addMachineModel(hook, name, defaultCasing, overlay, front, top, side, active);
	}
	
	/**
	 * @see MachineRegistrationHelper#addMachineModel(String, String, MachineCasing, boolean, boolean, boolean, boolean)
	 */
	public static void addMachineModel(
			MIHook hook,
			String name,
			MachineTier tier,
			String overlay,
			boolean front,
			boolean top,
			boolean side
	)
	{
		addMachineModel(hook, name, tier, overlay, front, top, side, true);
	}
	
	/**
	 * @see SingleBlockCraftingMachines#registerMachineTiers(String, String, MachineRecipeType, int, int, int, int, Consumer, ProgressBar.Params, RecipeEfficiencyBar.Params, EnergyBar.Params, Consumer, Consumer, boolean, boolean, boolean, int, int, Config)
	 */
	public static void registerMachineTiers(
			MIHook hook,
			String englishName,
			String machineName,
			MachineRecipeType type,
			int itemInputCount,
			int itemOutputCount,
			int fluidInputCount,
			int fluidOutputCount,
			Consumer<MachineGuiParameters.Builder> guiParams,
			ProgressBar.Params progressBarParams,
			RecipeEfficiencyBar.Params efficiencyBarParams,
			EnergyBar.Params energyBarParams,
			Consumer<SlotPositions.Builder> itemPositions,
			Consumer<SlotPositions.Builder> fluidPositions,
			boolean frontOverlay,
			boolean topOverlay,
			boolean sideOverlay,
			int tiers,
			int ioBucketCapacity,
			SingleBlockCraftingMachines.Config extraConfig
	)
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
			MachineGuiParameters.Builder guiParamsBuilder = new MachineGuiParameters.Builder(hook.id(id), true);
			guiParams.accept(guiParamsBuilder);
			MachineGuiParameters builtGuiParams = guiParamsBuilder.build();
			
			registerMachine(
					hook,
					englishPrefix + englishName,
					id,
					(bet) -> new SteamCraftingMachineBlockEntity(
							bet,
							type,
							MachineInventoryHelper.buildInventoryComponent(
									itemInputCount,
									itemOutputCount,
									fluidInputCount,
									fluidOutputCount,
									items,
									fluids,
									steamBuckets,
									ioBucketCapacity
							),
							builtGuiParams,
							progressBarParams,
							tier,
							extraConfig.steamOverclockCatalysts
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
			addMachineModel(hook, id, tier, machineName, frontOverlay, topOverlay, sideOverlay);
		}
		if((tiers & TIER_ELECTRIC) > 0)
		{
			SlotPositions items = new SlotPositions.Builder().buildWithConsumer(itemPositions);
			SlotPositions fluids = new SlotPositions.Builder().buildWithConsumer(fluidPositions);
			
			String id = tiers == TIER_ELECTRIC ? machineName : "electric_" + machineName;
			
			MachineGuiParameters.Builder guiParamsBuilder = new MachineGuiParameters.Builder(hook.id(id), true);
			guiParams.accept(guiParamsBuilder);
			MachineGuiParameters builtGuiParams = guiParamsBuilder.build();
			
			String electricEnglishName = englishName;
			
			if((tiers & TIER_BRONZE) > 0 | (tiers & TIER_STEEL) > 0)
			{
				electricEnglishName = "Electric " + englishName;
			}
			
			registerMachine(
					hook,
					electricEnglishName, id,
					(bet) -> new ElectricCraftingMachineBlockEntity(
							bet,
							type,
							MachineInventoryHelper.buildInventoryComponent(
									itemInputCount,
									itemOutputCount,
									fluidInputCount,
									fluidOutputCount,
									items,
									fluids,
									0,
									ioBucketCapacity
							),
							builtGuiParams,
							energyBarParams,
							progressBarParams,
							efficiencyBarParams,
							MachineTier.LV,
							3200
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
			addMachineModel(hook, id, MachineTier.LV, machineName, frontOverlay, topOverlay, sideOverlay);
		}
		
		SlotPositions items = new SlotPositions.Builder().buildWithConsumer(itemPositions);
		SlotPositions fluids = new SlotPositions.Builder().buildWithConsumer(fluidPositions);
		registerReiTiers(
				hook,
				englishName,
				machineName,
				type,
				new MachineCategoryParams(
						null,
						null,
						items.sublist(0, itemInputCount),
						items.sublist(itemInputCount, itemInputCount + itemOutputCount),
						fluids.sublist(0, fluidInputCount),
						fluids.sublist(fluidInputCount, fluidInputCount + fluidOutputCount), progressBarParams,
						null,
						null,
						false,
						SteamMode.BOTH
				),
				tiers
		);
	}
	
	public static void registerRecipeCategory(
			MIHook hook,
			String id,
			String englishName,
			MachineRecipeType recipeType,
			MachineCategoryParams inputParams
	)
	{
		var machineId = hook.id(id);
		var params = new MachineCategoryParams(
				englishName,
				machineId,
				inputParams.itemInputs,
				inputParams.itemOutputs,
				inputParams.fluidInputs,
				inputParams.fluidOutputs,
				inputParams.progressBarParams,
				recipeType,
				inputParams.recipePredicate,
				inputParams.isMultiblock,
				inputParams.steamMode
		);
		MIHookTracker.addReiCategoryName(machineId, englishName);
		hook.enqueue(() ->
		{
			ReiMachineRecipes.registerCategory(machineId, params);
			ReiMachineRecipes.registerMachineClickArea(machineId, params.progressBarParams.toRectangle());
			ReiMachineRecipes.registerRecipeCategoryForMachine(machineId, params.category);
		});
		params.workstations.add(machineId);
	}
	
	public static void registerRecipeCategory(
			MIHook hook,
			String id,
			String englishName,
			MachineRecipeType recipeType,
			SlotPositions itemInputs,
			SlotPositions itemOutputs,
			SlotPositions fluidInputs,
			SlotPositions fluidOutputs,
			ProgressBar.Params progressBarParams,
			Predicate<MachineRecipe> recipePredicate,
			boolean isMultiblock,
			SteamMode steamMode
	)
	{
		var machineId = hook.id(id);
		registerRecipeCategory(
				hook,
				id,
				englishName,
				recipeType,
				new MachineCategoryParams(
						null,
						null,
						itemInputs,
						itemOutputs,
						fluidInputs,
						fluidOutputs,
						progressBarParams,
						null,
						recipePredicate,
						isMultiblock,
						steamMode
				)
		);
	}
	
	/**
	 * @see SingleBlockCraftingMachines#registerReiTiers(String, String, MachineRecipeType, MachineCategoryParams, int)
	 */
	public static void registerReiTiers(
			MIHook hook,
			String englishName,
			String machine,
			MachineRecipeType recipeType,
			MachineCategoryParams categoryParams,
			int tiers
	)
	{
		List<MachineCategoryParams> previousCategories = new ArrayList<>();
		int previousMaxEu = 0;
		for(int i = 0; i < 3; ++i)
		{
			if(((tiers >> i) & 1) > 0)
			{
				int minEu = previousMaxEu + 1;
				int maxEu = i == 0 ? 2 : (i == 1 ? 4 : Integer.MAX_VALUE);
				String prefix = i == 0 ? "bronze_" : (i == 1 ? "steel_" : (tiers == TIER_ELECTRIC ? "" : "electric_"));
				ResourceLocation itemId = hook.id(prefix + machine);
				String englishPrefix = i == 0 ? "Bronze " : (i == 1 ? "Steel " : "Electric ");
				String fullEnglishName = tiers == TIER_ELECTRIC || previousMaxEu == 0 ? englishName : englishPrefix + englishName;
				MachineCategoryParams category = new MachineCategoryParams(
						fullEnglishName,
						itemId,
						categoryParams.itemInputs,
						categoryParams.itemOutputs,
						categoryParams.fluidInputs,
						categoryParams.fluidOutputs,
						categoryParams.progressBarParams,
						recipeType,
						(recipe) ->
								categoryParams.recipePredicate.test(recipe) &&
								minEu <= recipe.eu &&
								recipe.eu <= maxEu,
						false,
						i < 2 ? SteamMode.BOTH : SteamMode.ELECTRIC_ONLY
				);
				MIHookTracker.addReiCategoryName(itemId, fullEnglishName);
				hook.enqueue(() ->
				{
					ReiMachineRecipes.registerCategory(itemId, category);
					ReiMachineRecipes.registerMachineClickArea(itemId, categoryParams.progressBarParams.toRectangle());
				});
				previousCategories.add(category);
				for(MachineCategoryParams param : previousCategories)
				{
					param.workstations.add(itemId);
					hook.enqueue(() -> ReiMachineRecipes.registerRecipeCategoryForMachine(itemId, param.category));
				}
				previousMaxEu = maxEu;
			}
		}
	}
	
	/**
	 * @see MIMachineRecipeTypes#create(String, Function)
	 */
	public static MachineRecipeType createMachineRecipeType(
			MIHook hook,
			String name,
			Function<ResourceLocation, MachineRecipeType> creator
	)
	{
		MIHookRegistry registry = hook.registry();
		
		MachineRecipeType type = creator.apply(hook.id(name));
		registry.recipeSerializerRegistry().register(name, () -> type);
		registry.recipeTypeRegistry().register(name, () -> type);
		registry.onMachineRecipeTypeRegister(type);
		hook.enqueue(() -> MIMachineRecipeTypesAccessor.getRecipeTypes().add(type));
		
		return type;
	}
}
