package net.swedz.tesseract.neoforge.compat.mi.guicomponent.slotpanel;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.MITooltips;
import aztech.modern_industrialization.inventory.HackySlot;
import aztech.modern_industrialization.inventory.SlotGroup;
import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.components.CasingComponent;
import aztech.modern_industrialization.machines.components.OverdriveComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import aztech.modern_industrialization.machines.gui.GuiComponent;
import aztech.modern_industrialization.machines.gui.GuiComponentServer;
import aztech.modern_industrialization.machines.gui.MachineGuiParameters;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.component.SimpleItemStackComponent;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ModularSlotPanel implements GuiComponentServer<ModularSlotPanel.Params, ModularSlotPanel.Data>
{
	public static final GuiComponentServer.Type<Params, Data> TYPE = new GuiComponentServer.Type<>(Tesseract.id("modular_slot_panel"), Params.STREAM_CODEC, Data.STREAM_CODEC);
	
	private static final Map<ResourceLocation, Slot> SLOTS = Maps.newHashMap();
	
	public static final ResourceLocation REDSTONE_MODULE  = registerMISlot("redstone_module", SlotPanel.SlotType.REDSTONE_MODULE);
	public static final ResourceLocation UPGRADES         = registerMISlot("upgrades", SlotPanel.SlotType.UPGRADES);
	public static final ResourceLocation CASINGS          = registerMISlot("casings", SlotPanel.SlotType.CASINGS);
	public static final ResourceLocation OVERDRIVE_MODULE = registerMISlot("overdrive_module", SlotPanel.SlotType.OVERDRIVE_MODULE);
	
	public static ResourceLocation registerSlot(ResourceLocation id, SlotGroup group,
												int stackLimit, Predicate<ItemStack> insertionChecker,
												ResourceLocation atlas, int u, int v,
												Supplier<Component> tooltip)
	{
		if(SLOTS.containsKey(id))
		{
			throw new IllegalArgumentException("There is already a slot type registered for the id '" + id.toString() + "'");
		}
		SLOTS.put(id, new Slot(id, group, stackLimit, insertionChecker, atlas, u, v, tooltip));
		return id;
	}
	
	private static ResourceLocation registerMISlot(String name, SlotPanel.SlotType slotType)
	{
		return registerSlot(MI.id(name), slotType.group, slotType.slotLimit, slotType.insertionChecker, null, slotType.u, slotType.v, () -> slotType.tooltip.text().withStyle(MITooltips.DEFAULT_STYLE));
	}
	
	static Slot getSlot(ResourceLocation id)
	{
		Slot slot = SLOTS.get(id);
		if(slot == null)
		{
			throw new IllegalArgumentException("Could not find slot with id '" + id.toString() + "'");
		}
		return slot;
	}
	
	static int getSlotX(MachineGuiParameters guiParameters)
	{
		return guiParameters.backgroundWidth + 6;
	}
	
	static int getSlotY(int index)
	{
		return 19 + (index * 20);
	}
	
	private final MachineBlockEntity machine;
	
	private final int offsetY;
	
	private final List<Slot>                     slots          = Lists.newArrayList();
	private final List<Supplier<Integer>>        stackLimits    = Lists.newArrayList();
	private final List<SimpleItemStackComponent> slotComponents = Lists.newArrayList();
	
	public ModularSlotPanel(MachineBlockEntity machine, int offsetY)
	{
		this.machine = machine;
		this.offsetY = offsetY;
	}
	
	private ModularSlotPanel with(Slot slot, Supplier<Integer> stackLimit, SimpleItemStackComponent component)
	{
		slots.add(slot);
		stackLimits.add(stackLimit);
		slotComponents.add(component);
		return this;
	}
	
	public ModularSlotPanel with(ResourceLocation slotId, Supplier<Integer> stackLimit, SimpleItemStackComponent component)
	{
		return this.with(getSlot(slotId), stackLimit, component);
	}
	
	private ModularSlotPanel with(Slot slot, SimpleItemStackComponent component)
	{
		return this.with(slot, slot::stackLimit, component);
	}
	
	public ModularSlotPanel with(ResourceLocation slotId, SimpleItemStackComponent component)
	{
		return this.with(getSlot(slotId), component);
	}
	
	public ModularSlotPanel withRedstoneModule(RedstoneControlComponent component)
	{
		return this.with(REDSTONE_MODULE, SimpleItemStackComponent.wrap(component));
	}
	
	public ModularSlotPanel withUpgrades(UpgradeComponent component)
	{
		return this.with(UPGRADES, SimpleItemStackComponent.wrap(component));
	}
	
	public ModularSlotPanel withCasings(CasingComponent component)
	{
		return this.with(CASINGS, SimpleItemStackComponent.wrap(component));
	}
	
	public ModularSlotPanel withOverdrive(OverdriveComponent component)
	{
		return this.with(OVERDRIVE_MODULE, SimpleItemStackComponent.wrap(component));
	}
	
	@Override
	public Params getParams()
	{
		return new Params(offsetY, slots);
	}
	
	@Override
	public Data extractData()
	{
		return new Data(stackLimits.stream().map(Supplier::get).toList());
	}
	
	@Override
	public Type<Params, Data> getType()
	{
		return TYPE;
	}
	
	@Override
	public void setupMenu(GuiComponent.MenuFacade menu)
	{
		for(int i = 0; i < slots.size(); i++)
		{
			Slot slot = slots.get(i);
			Supplier<Integer> stackLimit = stackLimits.get(i);
			SimpleItemStackComponent component = slotComponents.get(i);
			
			menu.addSlotToMenu(new HackySlot(getSlotX(machine.guiParams), getSlotY(i))
			{
				@Override
				protected ItemStack getRealStack()
				{
					return component.getStack().copy();
				}
				
				@Override
				protected void setRealStack(ItemStack stack)
				{
					component.setStackServer(machine, stack);
				}
				
				@Override
				public boolean mayPlace(ItemStack stack)
				{
					return slot.insertionChecker().test(stack);
				}
				
				@Override
				public int getMaxStackSize()
				{
					return stackLimit.get();
				}
			}, slot.group());
		}
	}
	
	public record Slot(
			ResourceLocation id, SlotGroup group,
			int stackLimit, Predicate<ItemStack> insertionChecker,
			ResourceLocation atlas, int u, int v,
			Supplier<Component> tooltip
	)
	{
		public static final StreamCodec<ByteBuf, Slot> STREAM_CODEC = ResourceLocation.STREAM_CODEC
				.map(ModularSlotPanel::getSlot, Slot::id);
	}
	
	public record Params(int offsetY, List<Slot> slots)
	{
		public static final StreamCodec<ByteBuf, Params> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, Params::offsetY,
				Slot.STREAM_CODEC.apply(ByteBufCodecs.list()), Params::slots,
				Params::new
		);
	}
	
	public record Data(List<Integer> stackLimits)
	{
		public static final StreamCodec<ByteBuf, Data> STREAM_CODEC = ByteBufCodecs.VAR_INT
				.apply(ByteBufCodecs.list())
				.map(Data::new, Data::stackLimits);
	}
}
