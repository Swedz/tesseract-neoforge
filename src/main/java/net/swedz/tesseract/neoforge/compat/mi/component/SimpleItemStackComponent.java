package net.swedz.tesseract.neoforge.compat.mi.component;

import aztech.modern_industrialization.machines.MachineBlockEntity;
import aztech.modern_industrialization.machines.MachineComponent;
import aztech.modern_industrialization.machines.components.CasingComponent;
import aztech.modern_industrialization.machines.components.DropableComponent;
import aztech.modern_industrialization.machines.components.OverdriveComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.swedz.tesseract.neoforge.compat.mi.api.ComponentStackHolder;

public class SimpleItemStackComponent implements MachineComponent, DropableComponent, ComponentStackHolder
{
	public interface UpdatedCallback
	{
		void onUpdate(ItemStack from, ItemStack to);
	}
	
	protected final String stackTagKey;
	
	protected final UpdatedCallback callback;
	
	protected ItemStack stack = ItemStack.EMPTY;
	
	public SimpleItemStackComponent(String stackTagKey, UpdatedCallback callback)
	{
		this.stackTagKey = stackTagKey;
		this.callback = callback;
	}
	
	public SimpleItemStackComponent(String stackTagKey)
	{
		this(stackTagKey, null);
	}
	
	@Override
	public ItemStack getStack()
	{
		return stack;
	}
	
	@Override
	public void setStack(ItemStack stack)
	{
		ItemStack previous = this.stack;
		this.stack = stack;
		if(callback != null)
		{
			callback.onUpdate(previous, stack);
		}
	}
	
	public void setStackServer(MachineBlockEntity machine, ItemStack stack)
	{
		ItemStack previous = this.stack;
		this.stack = stack;
		machine.setChanged();
		machine.sync();
		if(callback != null)
		{
			callback.onUpdate(previous, stack);
		}
	}
	
	@Override
	public ItemStack getDrop()
	{
		return stack;
	}
	
	@Override
	public void writeNbt(ValueOutput output)
	{
		output.store(stackTagKey, ItemStack.OPTIONAL_CODEC, stack);
	}
	
	@Override
	public void readNbt(ValueInput input, boolean isUpgradingMachine)
	{
		stack = input.read(stackTagKey, ItemStack.OPTIONAL_CODEC).orElseThrow();
	}
	
	@Override
	public void writeClientNbt(ValueOutput output)
	{
	}
	
	@Override
	public void readClientNbt(ValueInput input)
	{
	}
	
	public static SimpleItemStackComponent wrap(RedstoneControlComponent component)
	{
		return new SimpleItemStackComponent("redstoneModuleStack")
		{
			@Override
			public ItemStack getStack()
			{
				return component.getDrop();
			}
			
			@Override
			public void setStackServer(MachineBlockEntity machine, ItemStack stack)
			{
				component.setStackServer(machine, stack);
			}
			
			@Override
			public ItemStack getDrop()
			{
				return component.getDrop();
			}
			
			@Override
			public void writeNbt(ValueOutput output)
			{
				component.writeNbt(output);
			}
			
			@Override
			public void readNbt(ValueInput input, boolean isUpgradingMachine)
			{
				component.readNbt(input, isUpgradingMachine);
			}
			
			@Override
			public void writeClientNbt(ValueOutput output)
			{
				component.writeClientNbt(output);
			}
			
			@Override
			public void readClientNbt(ValueInput input)
			{
				component.readClientNbt(input);
			}
		};
	}
	
	public static SimpleItemStackComponent wrap(UpgradeComponent component)
	{
		return new SimpleItemStackComponent("upgradesItemStack")
		{
			@Override
			public ItemStack getStack()
			{
				return component.getDrop();
			}
			
			@Override
			public void setStackServer(MachineBlockEntity machine, ItemStack stack)
			{
				component.setStackServer(machine, stack);
			}
			
			@Override
			public ItemStack getDrop()
			{
				return component.getDrop();
			}
			
			@Override
			public void writeNbt(ValueOutput output)
			{
				component.writeNbt(output);
			}
			
			@Override
			public void readNbt(ValueInput input, boolean isUpgradingMachine)
			{
				component.readNbt(input, isUpgradingMachine);
			}
			
			@Override
			public void writeClientNbt(ValueOutput output)
			{
				component.writeClientNbt(output);
			}
			
			@Override
			public void readClientNbt(ValueInput input)
			{
				component.readClientNbt(input);
			}
		};
	}
	
	public static SimpleItemStackComponent wrap(OverdriveComponent component)
	{
		return new SimpleItemStackComponent("overdriveModuleStack")
		{
			@Override
			public ItemStack getStack()
			{
				return component.getDrop();
			}
			
			@Override
			public void setStackServer(MachineBlockEntity machine, ItemStack stack)
			{
				component.setStackServer(machine, stack);
			}
			
			@Override
			public ItemStack getDrop()
			{
				return component.getDrop();
			}
			
			@Override
			public void writeNbt(ValueOutput output)
			{
				component.writeNbt(output);
			}
			
			@Override
			public void readNbt(ValueInput input, boolean isUpgradingMachine)
			{
				component.readNbt(input, isUpgradingMachine);
			}
			
			@Override
			public void writeClientNbt(ValueOutput output)
			{
				component.writeClientNbt(output);
			}
			
			@Override
			public void readClientNbt(ValueInput input)
			{
				component.readClientNbt(input);
			}
		};
	}
	
	public static SimpleItemStackComponent wrap(CasingComponent component)
	{
		return new SimpleItemStackComponent("casing")
		{
			@Override
			public ItemStack getStack()
			{
				return component.getDrop();
			}
			
			@Override
			public void setStackServer(MachineBlockEntity machine, ItemStack stack)
			{
				component.setCasingServer(machine, stack);
			}
			
			@Override
			public ItemStack getDrop()
			{
				return component.getDrop();
			}
			
			@Override
			public void writeNbt(ValueOutput output)
			{
				component.writeNbt(output);
			}
			
			@Override
			public void readNbt(ValueInput input, boolean isUpgradingMachine)
			{
				component.readNbt(input, isUpgradingMachine);
			}
			
			@Override
			public void writeClientNbt(ValueOutput output)
			{
				component.writeClientNbt(output);
			}
			
			@Override
			public void readClientNbt(ValueInput input)
			{
				component.readClientNbt(input);
			}
		};
	}
}
