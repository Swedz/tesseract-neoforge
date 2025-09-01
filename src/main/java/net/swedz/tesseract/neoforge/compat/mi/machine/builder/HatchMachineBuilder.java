package net.swedz.tesseract.neoforge.compat.mi.machine.builder;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.blockentities.hatches.EnergyHatch;
import aztech.modern_industrialization.machines.blockentities.hatches.FluidHatch;
import aztech.modern_industrialization.machines.blockentities.hatches.ItemHatch;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.models.MachineCasing;
import com.google.common.collect.Lists;
import net.swedz.tesseract.neoforge.api.Assert;
import net.swedz.tesseract.neoforge.compat.mi.hack.HackedMachineRegistrationHelper;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockHatchBlockEntityFactory;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockHolderHatchModifier;
import net.swedz.tesseract.neoforge.compat.mi.machine.builder.function.MachineBlockRegistrators;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class HatchMachineBuilder extends MachineBuilder<HatchMachineBuilder>
{
	private RegistrationType type;
	
	private MachineBlockHatchBlockEntityFactory blockEntityFactory;
	private MachineBlockHolderHatchModifier     holderHatchModifier;
	
	private CableTier energyCableTier;
	
	HatchMachineBuilder(MIHook hook,
						String name, String englishName)
	{
		super(hook, name, englishName);
	}
	
	public HatchMachineBuilder modify(MachineBlockHolderHatchModifier modifier)
	{
		this.holderHatchModifier = modifier;
		return super.modify(null);
	}
	
	public HatchMachineBuilder item(int rows, int columns, int startX, int startY)
	{
		Assert.that(rows > 0);
		Assert.that(columns > 0);
		type = RegistrationType.ITEM;
		blockEntityFactory = (bep, input, machineId) ->
		{
			List<ConfigurableItemStack> itemStacks = Lists.newArrayList();
			for(int slot = 0; slot < rows * columns; slot++)
			{
				if(input)
				{
					itemStacks.add(ConfigurableItemStack.standardInputSlot());
				}
				else
				{
					itemStacks.add(ConfigurableItemStack.standardOutputSlot());
				}
			}
			var inventory = new MIInventory(
					itemStacks,
					Collections.emptyList(),
					new SlotPositions.Builder().addSlots(startX, startY, columns, rows).build(),
					SlotPositions.empty()
			);
			return new ItemHatch(bep, new MachineGuiParameters.Builder(machineId, true).build(), input, !name.equals("bronze"), inventory);
		};
		return this.registrator(ItemHatch::registerItemApi);
	}
	
	public HatchMachineBuilder fluid(int bucketCapacity)
	{
		Assert.that(bucketCapacity > 0);
		type = RegistrationType.FLUID;
		blockEntityFactory = (bep, input, machineId) ->
		{
			List<ConfigurableFluidStack> fluidStacks = Collections.singletonList(input ?
					ConfigurableFluidStack.standardInputSlot(bucketCapacity * 1000L) :
					ConfigurableFluidStack.standardOutputSlot(bucketCapacity * 1000L));
			var inventory = new MIInventory(
					Collections.emptyList(), fluidStacks,
					SlotPositions.empty(), new SlotPositions.Builder().addSlot(80, 40).build()
			);
			return new FluidHatch(bep, new MachineGuiParameters.Builder(machineId, true).build(), input, !name.equals("bronze"), inventory);
		};
		return this.registrator(FluidHatch::registerFluidApi);
	}
	
	public HatchMachineBuilder energy(CableTier tier)
	{
		Assert.notNull(tier);
		type = RegistrationType.ENERGY;
		energyCableTier = tier;
		blockEntityFactory = (bep, input, machineId) -> new EnergyHatch(bep, new MachineGuiParameters.Builder(machineId, false).build(), input, tier);
		return this.registrator(EnergyHatch::registerEnergyApi);
	}
	
	public HatchMachineBuilder special(MachineBlockHatchBlockEntityFactory factory)
	{
		Assert.notNull(factory);
		type = RegistrationType.SPECIAL;
		blockEntityFactory = factory;
		return this;
	}
	
	@Override
	public HatchMachineBuilder builtinModel(MachineCasing casing, String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		return super.builtinModel(casing, overlayFolder, (b) ->
		{
			// Hatches by default have a front and side overlay
			b.front().side();
			builder.accept(b);
		});
	}
	
	public HatchMachineBuilder builtinModel(MachineCasing casing, String overlayFolder)
	{
		return this.builtinModel(casing, overlayFolder, null);
	}
	
	public HatchMachineBuilder builtinModel(MachineCasing casing, Consumer<MachineBuiltinModelBuilder> builder)
	{
		Assert.notNull(type, "The type must be selected before including a builtin model");
		Assert.that(type != RegistrationType.SPECIAL, "Overlay folder must be specified for special hatches");
		return this.builtinModel(casing, this.getDefaultOverlayFolder(), builder);
	}
	
	public HatchMachineBuilder builtinModel(MachineCasing casing)
	{
		return this.builtinModel(casing, (Consumer<MachineBuiltinModelBuilder>) null);
	}
	
	public HatchMachineBuilder builtinModel(String overlayFolder, Consumer<MachineBuiltinModelBuilder> builder)
	{
		Assert.that(type == RegistrationType.ENERGY, "Machine casing must be specified for non-energy hatches");
		return this.builtinModel(energyCableTier.casing, overlayFolder, builder);
	}
	
	public HatchMachineBuilder builtinModel(Consumer<MachineBuiltinModelBuilder> builder)
	{
		return this.builtinModel(this.getDefaultOverlayFolder(), builder);
	}
	
	public HatchMachineBuilder builtinModel()
	{
		return this.builtinModel((Consumer<MachineBuiltinModelBuilder>) null);
	}
	
	private String getName()
	{
		return switch (type)
		{
			case ITEM -> name + "_item";
			case FLUID -> name + "_fluid";
			case ENERGY -> energyCableTier.name + "_energy";
			case SPECIAL -> name;
		};
	}
	
	private String getEnglishName()
	{
		return switch (type)
		{
			case ITEM -> englishName + " Item";
			case FLUID -> englishName + " Fluid";
			case ENERGY -> energyCableTier.shortEnglishName + " Energy";
			case SPECIAL -> englishName;
		};
	}
	
	private String getDefaultOverlayFolder()
	{
		return switch (type)
		{
			case ITEM -> "hatch_item";
			case FLUID -> "hatch_fluid";
			case ENERGY -> "hatch_energy";
			default -> throw new IllegalStateException("Unexpected value: " + type);
		};
	}
	
	@Override
	protected void internalRegister()
	{
		Assert.notNull(type, "Hatch type must be configured");
		
		var name = this.getName();
		var englishName = this.getEnglishName();
		
		if(type.registersBothIO())
		{
			for(int i = 0; i < 2; i++)
			{
				boolean input = i == 0;
				String machineId = "%s_%s_hatch".formatted(name, input ? "input" : "output");
				String machineEnglishName = "%s %s Hatch".formatted(englishName, input ? "Input" : "Output");
				
				HackedMachineRegistrationHelper.registerMachine(
						hook,
						machineEnglishName, machineId,
						blockFactory,
						holderModifier != null ? holderModifier : (holderHatchModifier != null ? (holder) -> holderHatchModifier.modify(holder, input) : null),
						propertiesModifier,
						defaultMineableTags,
						(bep) -> blockEntityFactory.create(bep, input, hook.id(machineId)),
						registrators.toArray(MachineBlockRegistrators[]::new)
				);
				if(builtinModel != null)
				{
					builtinModel.build(hook, name);
				}
			}
		}
		else
		{
			HackedMachineRegistrationHelper.registerMachine(
					hook,
					englishName, name,
					blockFactory,
					holderModifier != null ? holderModifier : (holderHatchModifier != null ? (holder) -> holderHatchModifier.modify(holder, false) : null),
					propertiesModifier,
					defaultMineableTags,
					(bep) -> blockEntityFactory.create(bep, false, hook.id(name)),
					registrators.toArray(MachineBlockRegistrators[]::new)
			);
			if(builtinModel != null)
			{
				builtinModel.build(hook, name);
			}
		}
	}
	
	public enum RegistrationType
	{
		ITEM(true),
		FLUID(true),
		ENERGY(true),
		SPECIAL(false);
		
		private final boolean registersBothIO;
		
		RegistrationType(boolean registersBothIO)
		{
			this.registersBothIO = registersBothIO;
		}
		
		public boolean registersBothIO()
		{
			return registersBothIO;
		}
	}
}
