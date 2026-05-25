package net.swedz.tesseract.neoforge.compat.mi.machine.builder.slots;

import aztech.modern_industrialization.inventory.ConfigurableFluidStack;
import aztech.modern_industrialization.inventory.ConfigurableItemStack;
import aztech.modern_industrialization.inventory.MIInventory;
import aztech.modern_industrialization.inventory.SlotPositions;
import aztech.modern_industrialization.machines.components.MachineInventoryComponent;
import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MachineSlotConfiguration
{
	public static MachineSlotConfiguration empty()
	{
		return new MachineSlotConfiguration(new MachineSlotDefinition[0]);
	}
	
	public static MachineSlotConfiguration.Builder combine(MachineSlotConfiguration... configurations)
	{
		var combined = new MachineSlotConfiguration.Builder();
		for(var group : configurations)
		{
			for(int i = 0; i < group.size(); i++)
			{
				combined.add(group.get(i));
			}
		}
		return combined;
	}
	
	private final MachineSlotDefinition[] slots;
	
	private MachineSlotConfiguration(MachineSlotDefinition[] slots)
	{
		this.slots = slots;
	}
	
	public MachineSlotDefinition get(int index)
	{
		return slots[index];
	}
	
	public int size()
	{
		return slots.length;
	}
	
	public AllSlotPositions toSlotPositions()
	{
		var all = new SlotPositions.Builder();
		var items = new SlotPositions.Builder();
		var itemInputs = new SlotPositions.Builder();
		var itemOutputs = new SlotPositions.Builder();
		var fluids = new SlotPositions.Builder();
		var fluidInputs = new SlotPositions.Builder();
		var fluidOutputs = new SlotPositions.Builder();
		for(var slot : slots)
		{
			all.addSlot(slot.x(), slot.y());
			if(slot instanceof ItemMachineSlotDefinition itemSlot)
			{
				items.addSlot(slot.x(), slot.y());
				(slot.playerInsert() || slot.pipesInsert() ? itemInputs : itemOutputs).addSlot(slot.x(), slot.y());
			}
			else if(slot instanceof FluidMachineSlotDefinition fluidSlot)
			{
				fluids.addSlot(slot.x(), slot.y());
				(slot.playerInsert() || slot.pipesInsert() ? fluidInputs : fluidOutputs).addSlot(slot.x(), slot.y());
			}
			else
			{
				throw new IllegalArgumentException("Unsupported slot type " + slot.getClass().getSimpleName());
			}
		}
		return new AllSlotPositions(
				all.build(),
				items.build(), itemInputs.build(), itemOutputs.build(),
				fluids.build(), fluidInputs.build(), fluidOutputs.build()
		);
	}
	
	public record AllSlotPositions(
			SlotPositions all,
			SlotPositions items, SlotPositions itemInputs, SlotPositions itemOutputs,
			SlotPositions fluids, SlotPositions fluidInputs, SlotPositions fluidOutputs
	)
	{
		public boolean hasItems()
		{
			return items.size() > 0;
		}
		
		public boolean hasFluids()
		{
			return fluids.size() > 0;
		}
		
		public int inputs()
		{
			return itemInputs.size() + fluidInputs.size();
		}
		
		public boolean hasInputs()
		{
			return this.inputs() > 0;
		}
		
		public int outputs()
		{
			return itemOutputs.size() + fluidOutputs.size();
		}
		
		public boolean hasOutputs()
		{
			return this.outputs() > 0;
		}
	}
	
	public MIInventory toInventory()
	{
		List<ConfigurableItemStack> items = Lists.newArrayList();
		List<ConfigurableFluidStack> fluids = Lists.newArrayList();
		
		for(var slot : slots)
		{
			if(slot instanceof ItemMachineSlotDefinition itemSlot)
			{
				items.add(itemSlot.create());
			}
			else if(slot instanceof FluidMachineSlotDefinition fluidSlot)
			{
				fluids.add(fluidSlot.create());
			}
			else
			{
				throw new IllegalArgumentException("Unsupported slot type " + slot.getClass().getSimpleName());
			}
		}
		
		var slotPositions = this.toSlotPositions();
		
		return new MIInventory(
				items, fluids,
				slotPositions.items(),
				slotPositions.fluids()
		);
	}
	
	public MachineInventoryComponent toInventoryComponent()
	{
		List<ConfigurableItemStack> itemInputs = Lists.newArrayList();
		List<ConfigurableItemStack> itemOutputs = Lists.newArrayList();
		List<ConfigurableFluidStack> fluidInputs = Lists.newArrayList();
		List<ConfigurableFluidStack> fluidOutputs = Lists.newArrayList();
		
		for(var slot : slots)
		{
			if(slot instanceof ItemMachineSlotDefinition itemSlot)
			{
				(slot.playerInsert() || slot.pipesInsert() ? itemInputs : itemOutputs).add(itemSlot.create());
			}
			else if(slot instanceof FluidMachineSlotDefinition fluidSlot)
			{
				(slot.playerInsert() || slot.pipesInsert() ? fluidInputs : fluidOutputs).add(fluidSlot.create());
			}
			else
			{
				throw new IllegalArgumentException("Unsupported slot type " + slot.getClass().getSimpleName());
			}
		}
		
		var slotPositions = this.toSlotPositions();
		
		return new MachineInventoryComponent(
				itemInputs, itemOutputs,
				fluidInputs, fluidOutputs,
				slotPositions.items(),
				slotPositions.fluids()
		);
	}
	
	public static final class Builder
	{
		private final List<MachineSlotDefinition> slots = Lists.newArrayList();
		
		private int fallbackFluidCapacity = 16;
		
		public Builder fluidCapacity(int fallbackFluidCapacity)
		{
			this.fallbackFluidCapacity = fallbackFluidCapacity;
			return this;
		}
		
		public Builder append(Builder other)
		{
			slots.addAll(other.slots);
			return this;
		}
		
		public Builder append(SlotPositions itemInputs, SlotPositions itemOutputs,
							  SlotPositions fluidInputs, SlotPositions fluidOutputs)
		{
			for(int index = 0; index < itemInputs.size(); index++)
			{
				this.itemInput(itemInputs.getX(index), itemInputs.getY(index));
			}
			for(int index = 0; index < itemOutputs.size(); index++)
			{
				this.itemOutput(itemOutputs.getX(index), itemOutputs.getY(index));
			}
			for(int index = 0; index < fluidInputs.size(); index++)
			{
				this.fluidInput(fluidInputs.getX(index), fluidInputs.getY(index));
			}
			for(int index = 0; index < fluidOutputs.size(); index++)
			{
				this.fluidOutput(fluidOutputs.getX(index), fluidOutputs.getY(index));
			}
			return this;
		}
		
		public Builder append(SlotPositions.Builder itemInputs, SlotPositions.Builder itemOutputs,
							  SlotPositions.Builder fluidInputs, SlotPositions.Builder fluidOutputs)
		{
			return this.append(
					itemInputs.build(), itemOutputs.build(),
					fluidInputs.build(), fluidOutputs.build()
			);
		}
		
		public Builder append(Consumer<SlotPositions.Builder> itemInputs, Consumer<SlotPositions.Builder> itemOutputs,
							  Consumer<SlotPositions.Builder> fluidInputs, Consumer<SlotPositions.Builder> fluidOutputs)
		{
			var itemInputsBuilder = new SlotPositions.Builder();
			itemInputs.accept(itemInputsBuilder);
			
			var itemOutputsBuilder = new SlotPositions.Builder();
			itemOutputs.accept(itemOutputsBuilder);
			
			var fluidInputsBuilder = new SlotPositions.Builder();
			fluidInputs.accept(fluidInputsBuilder);
			
			var fluidOutputsBuilder = new SlotPositions.Builder();
			fluidOutputs.accept(fluidOutputsBuilder);
			
			return this.append(
					itemInputsBuilder, itemOutputsBuilder,
					fluidInputsBuilder, fluidOutputsBuilder
			);
		}
		
		private Builder add(MachineSlotDefinition slot)
		{
			slots.add(slot);
			return this;
		}
		
		private Builder item(int x, int y,
							 boolean playerInsert, boolean pipesInsert,
							 boolean playerExtract, boolean pipesExtract,
							 Supplier<Item> lock)
		{
			return this.add(new ItemMachineSlotDefinition(x, y, playerInsert, pipesInsert, playerExtract, pipesExtract, lock));
		}
		
		public Builder itemInput(int x, int y, Supplier<Item> lock)
		{
			return this.item(x, y, true, true, true, false, lock);
		}
		
		public Builder itemInput(int x, int y)
		{
			return this.itemInput(x, y, null);
		}
		
		public Builder itemOutput(int x, int y, Supplier<Item> lock)
		{
			return this.item(x, y, false, false, true, true, lock);
		}
		
		public Builder itemOutput(int x, int y)
		{
			return this.itemOutput(x, y, null);
		}
		
		public Builder itemIO(int x, int y, Supplier<Item> lock)
		{
			return this.item(x, y, true, true, true, true, lock);
		}
		
		public Builder itemIO(int x, int y)
		{
			return this.itemIO(x, y, null);
		}
		
		private Builder items(int x, int y, int columns, int rows,
							  boolean playerInsert, boolean pipesInsert,
							  boolean playerExtract, boolean pipesExtract,
							  Supplier<Item> lock)
		{
			for(int row = 0; row < rows; row++)
			{
				for(int column = 0; column < columns; column++)
				{
					this.item(x + (column * 18), y + (row * 18), playerInsert, pipesInsert, playerExtract, pipesExtract, lock);
				}
			}
			return this;
		}
		
		public Builder itemInputs(int x, int y, int columns, int rows, Supplier<Item> lock)
		{
			return this.items(x, y, columns, rows, true, true, true, false, lock);
		}
		
		public Builder itemInputs(int x, int y, int columns, int rows)
		{
			return this.itemInputs(x, y, columns, rows, null);
		}
		
		public Builder itemOutputs(int x, int y, int columns, int rows, Supplier<Item> lock)
		{
			return this.items(x, y, columns, rows, false, false, true, true, lock);
		}
		
		public Builder itemOutputs(int x, int y, int columns, int rows)
		{
			return this.itemOutputs(x, y, columns, rows, null);
		}
		
		public Builder itemIOs(int x, int y, int columns, int rows, Supplier<Item> lock)
		{
			return this.items(x, y, columns, rows, true, true, true, true, lock);
		}
		
		public Builder itemIOs(int x, int y, int columns, int rows)
		{
			return this.itemIOs(x, y, columns, rows, null);
		}
		
		private Builder fluid(int x, int y,
							  boolean playerInsert, boolean pipesInsert,
							  boolean playerExtract, boolean pipesExtract,
							  Supplier<Fluid> lock, int capacityBuckets)
		{
			return this.add(new FluidMachineSlotDefinition(x, y, playerInsert, pipesInsert, playerExtract, pipesExtract, lock, capacityBuckets * 1000));
		}
		
		public Builder fluidInput(int x, int y, Supplier<Fluid> lock, int capacityBuckets)
		{
			return this.fluid(x, y, true, true, true, false, lock, capacityBuckets);
		}
		
		public Builder fluidInput(int x, int y, Supplier<Fluid> lock)
		{
			return this.fluidInput(x, y, lock, fallbackFluidCapacity);
		}
		
		public Builder fluidInput(int x, int y, int capacityBuckets)
		{
			return this.fluidInput(x, y, null, capacityBuckets);
		}
		
		public Builder fluidInput(int x, int y)
		{
			return this.fluidInput(x, y, fallbackFluidCapacity);
		}
		
		public Builder fluidOutput(int x, int y, Supplier<Fluid> lock, int capacityBuckets)
		{
			return this.fluid(x, y, false, false, true, true, lock, capacityBuckets);
		}
		
		public Builder fluidOutput(int x, int y, Supplier<Fluid> lock)
		{
			return this.fluidOutput(x, y, lock, fallbackFluidCapacity);
		}
		
		public Builder fluidOutput(int x, int y, int capacityBuckets)
		{
			return this.fluidOutput(x, y, null, capacityBuckets);
		}
		
		public Builder fluidOutput(int x, int y)
		{
			return this.fluidOutput(x, y, fallbackFluidCapacity);
		}
		
		public Builder fluidIO(int x, int y, Supplier<Fluid> lock, int capacityBuckets)
		{
			return this.fluid(x, y, true, true, true, true, lock, capacityBuckets);
		}
		
		public Builder fluidIO(int x, int y, Supplier<Fluid> lock)
		{
			return this.fluidIO(x, y, lock, fallbackFluidCapacity);
		}
		
		public Builder fluidIO(int x, int y, int capacityBuckets)
		{
			return this.fluidIO(x, y, null, capacityBuckets);
		}
		
		public Builder fluidIO(int x, int y)
		{
			return this.fluidIO(x, y, fallbackFluidCapacity);
		}
		
		private Builder fluids(int x, int y, int columns, int rows,
							   boolean playerInsert, boolean pipesInsert,
							   boolean playerExtract, boolean pipesExtract,
							   Supplier<Fluid> lock, int capacityBuckets)
		{
			for(int row = 0; row < rows; row++)
			{
				for(int column = 0; column < columns; column++)
				{
					this.fluid(x + (column * 18), y + (row * 18), playerInsert, pipesInsert, playerExtract, pipesExtract, lock, capacityBuckets);
				}
			}
			return this;
		}
		
		public Builder fluidInputs(int x, int y, int columns, int rows, Supplier<Fluid> lock, int capacityBuckets)
		{
			return this.fluids(x, y, columns, rows, true, true, true, false, lock, capacityBuckets);
		}
		
		public Builder fluidInputs(int x, int y, int columns, int rows, Supplier<Fluid> lock)
		{
			return this.fluidInputs(x, y, columns, rows, lock, fallbackFluidCapacity);
		}
		
		public Builder fluidInputs(int x, int y, int columns, int rows, int capacityBuckets)
		{
			return this.fluidInputs(x, y, columns, rows, null, capacityBuckets);
		}
		
		public Builder fluidInputs(int x, int y, int columns, int rows)
		{
			return this.fluidInputs(x, y, columns, rows, fallbackFluidCapacity);
		}
		
		public Builder fluidOutputs(int x, int y, int columns, int rows, Supplier<Fluid> lock, int capacityBuckets)
		{
			return this.fluids(x, y, columns, rows, false, false, true, true, lock, capacityBuckets);
		}
		
		public Builder fluidOutputs(int x, int y, int columns, int rows, Supplier<Fluid> lock)
		{
			return this.fluidOutputs(x, y, columns, rows, lock, fallbackFluidCapacity);
		}
		
		public Builder fluidOutputs(int x, int y, int columns, int rows, int capacityBuckets)
		{
			return this.fluidOutputs(x, y, columns, rows, null, capacityBuckets);
		}
		
		public Builder fluidOutputs(int x, int y, int columns, int rows)
		{
			return this.fluidOutputs(x, y, columns, rows, fallbackFluidCapacity);
		}
		
		public Builder fluidIOs(int x, int y, int columns, int rows, Supplier<Fluid> lock, int capacityBuckets)
		{
			return this.fluids(x, y, columns, rows, true, true, true, true, lock, capacityBuckets);
		}
		
		public Builder fluidIOs(int x, int y, int columns, int rows, Supplier<Fluid> lock)
		{
			return this.fluidIOs(x, y, columns, rows, lock, fallbackFluidCapacity);
		}
		
		public Builder fluidIOs(int x, int y, int columns, int rows, int capacityBuckets)
		{
			return this.fluidIOs(x, y, columns, rows, null, capacityBuckets);
		}
		
		public Builder fluidIOs(int x, int y, int columns, int rows)
		{
			return this.fluidIOs(x, y, columns, rows, fallbackFluidCapacity);
		}
		
		public MachineSlotConfiguration build()
		{
			return new MachineSlotConfiguration(slots.toArray(MachineSlotDefinition[]::new));
		}
		
		public MachineSlotConfiguration build(Consumer<Builder> builder)
		{
			builder.accept(this);
			return this.build();
		}
	}
}
