package net.swedz.tesseract.neoforge.compat.mi.component;

import aztech.modern_industrialization.inventory.MIFluidStorage;
import aztech.modern_industrialization.machines.MachineComponent;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.List;
import java.util.function.Supplier;

public final class SteamProductionComponent implements MachineComponent.ServerOnly
{
	private final MIFluidStorage fluidStorage;
	
	private final List<FluidResource> waterTypes;
	private final FluidResource       steamKey;
	
	private final Supplier<Integer> maxEuProduction;
	private final Supplier<Integer> waterToSteam;
	
	private final Reference2IntMap<FluidResource> steamBuffer = new Reference2IntOpenHashMap<>();
	
	public SteamProductionComponent(MIFluidStorage fluidStorage, List<FluidResource> waterTypes, FluidResource steamKey, Supplier<Integer> maxEuProduction, Supplier<Integer> waterToSteam)
	{
		this.fluidStorage = fluidStorage;
		this.waterTypes = waterTypes;
		this.steamKey = steamKey;
		this.maxEuProduction = maxEuProduction;
		this.waterToSteam = waterToSteam;
	}
	
	public FluidResource findWater()
	{
		for(int index = 0; index < fluidStorage.size(); index++)
		{
			var fluid = fluidStorage.getResource(index);
			if(fluid.isEmpty())
			{
				continue;
			}
			for(var waterType : waterTypes)
			{
				if(fluid.equals(waterType))
				{
					return fluid;
				}
			}
		}
		return FluidResource.EMPTY;
	}
	
	public FluidResource tryMakeSteam()
	{
		int waterToSteamRate = this.waterToSteam.get();
		
		var waterFluid = this.findWater();
		if(waterFluid.isEmpty())
		{
			return waterFluid;
		}
		
		int steamToProduce = maxEuProduction.get();
		try(var transaction = Transaction.openRoot())
		{
			int steamProducedSimulation;
			try(var simulation = Transaction.open(transaction))
			{
				steamProducedSimulation = fluidStorage.insertAllSlot(steamKey, steamToProduce, simulation);
			}
			
			if(steamProducedSimulation > 0)
			{
				int waterToConsume = (steamProducedSimulation - steamBuffer.getInt(steamKey.getFluid()) + waterToSteamRate - 1) / waterToSteamRate;
				int waterConsumed = fluidStorage.extractAllSlot(waterFluid, waterToConsume, transaction);
				steamBuffer.mergeInt(steamKey, waterConsumed * waterToSteamRate, Integer::sum);
				
				int steamProduced = fluidStorage.insertAllSlot(steamKey, Math.min(steamToProduce, steamBuffer.getInt(steamKey.getFluid())), transaction);
				steamBuffer.mergeInt(steamKey, -steamProduced, Integer::sum);
				
				transaction.commit();
			}
		}
		
		return waterFluid;
	}
	
	@Override
	public void writeNbt(ValueOutput output)
	{
		var buffer = new CompoundTag();
		for(var entry : steamBuffer.reference2IntEntrySet())
		{
			if(entry.getIntValue() != 0)
			{
				buffer.putLong(entry.getKey().toString(), entry.getIntValue());
			}
		}
		output.store("steamBuffer", CompoundTag.CODEC, buffer);
	}
	
	@Override
	public void readNbt(ValueInput input, boolean isUpgradingMachine)
	{
		var buffer = input.read("steamBuffer", CompoundTag.CODEC).orElseThrow();
		for(var key : buffer.keySet())
		{
			var fluid = BuiltInRegistries.FLUID.getValue(Identifier.tryParse(key));
			if(fluid != Fluids.EMPTY)
			{
				steamBuffer.put(FluidResource.of(fluid), buffer.getIntOr(key, 0));
			}
		}
	}
}
