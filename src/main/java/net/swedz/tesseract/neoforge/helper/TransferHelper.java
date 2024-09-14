package net.swedz.tesseract.neoforge.helper;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Predicate;

public final class TransferHelper
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	/**
	 * Attempts to move all items from one item handler to another.
	 * <br><br>
	 * Taken from {@link aztech.modern_industrialization.util.TransferHelper#moveAll(IItemHandler, IItemHandler, boolean)} and modified to return a list of extracted items.
	 *
	 * @param source        the source item handler
	 * @param target        the target item handler
	 * @param stackInTarget whether the items should get combined in the target item handler
	 * @return the list of items that have been extracted
	 */
	public static List<ItemStack> moveAll(IItemHandler source, IItemHandler target, boolean stackInTarget)
	{
		List<ItemStack> moved = Lists.newArrayList();
		
		int srcSlots = source.getSlots();
		
		for(int i = 0; i < srcSlots; ++i)
		{
			ItemStack extracted = source.extractItem(i, Integer.MAX_VALUE, true);
			if(extracted.isEmpty())
			{
				continue;
			}
			int extractedCount = extracted.getCount();
			
			ItemStack leftover = stackInTarget ?
					ItemHandlerHelper.insertItemStacked(target, extracted, true) :
					ItemHandlerHelper.insertItem(target, extracted, true);
			int insertedCount = extractedCount - leftover.getCount();
			if(insertedCount <= 0)
			{
				continue;
			}
			
			extracted = source.extractItem(i, insertedCount, false);
			if(extracted.isEmpty())
			{
				continue;
			}
			
			leftover = stackInTarget ?
					ItemHandlerHelper.insertItemStacked(target, extracted, false) :
					ItemHandlerHelper.insertItem(target, extracted, false);
			
			moved.add(extracted.copy());
			
			if(!leftover.isEmpty())
			{
				leftover = source.insertItem(i, leftover, false);
				
				if(!leftover.isEmpty())
				{
					LOGGER.warn("Item handler {} rejected {}, discarding.", target, leftover);
				}
			}
		}
		
		return moved;
	}
	
	/**
	 * Attempts to extract items matching a predicate into a single stack. The first extracted item will determine what
	 * base item and components to use - all other matching stacks will be checked against this as well as the
	 * predicate.
	 * <br><br>
	 * Taken from {@link aztech.modern_industrialization.util.TransferHelper#extractMatching(IItemHandler, Predicate, int)} and is unchanged.
	 *
	 * @param source    the source item handler
	 * @param predicate the predicate to check against
	 * @param maxAmount the max amount of items to extract
	 * @return the extracted and combined stack
	 */
	public static ItemStack extractMatching(IItemHandler source, Predicate<ItemStack> predicate, int maxAmount)
	{
		int sourceSlots = source.getSlots();
		
		ItemStack ret = ItemStack.EMPTY;
		int slot;
		for(slot = 0; slot < sourceSlots && ret.isEmpty(); ++slot)
		{
			ItemStack stack = source.getStackInSlot(slot);
			if(predicate.test(stack))
			{
				ret = source.extractItem(slot, maxAmount, false);
			}
		}
		if(ret.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		
		++slot;
		for(; slot < sourceSlots && maxAmount < ret.getCount(); ++slot)
		{
			ItemStack stack = source.getStackInSlot(slot);
			if(ItemStack.matches(stack, ret))
			{
				ItemStack extracted = source.extractItem(slot, maxAmount - ret.getCount(), true);
				ret.grow(extracted.getCount());
			}
		}
		
		return ret;
	}
	
	/**
	 * Attempt to move energy from one energy handler to another.
	 * <br><br>
	 * Taken from {@link dev.technici4n.grandpower.api.EnergyStorageUtil#move(dev.technici4n.grandpower.api.ILongEnergyStorage, dev.technici4n.grandpower.api.ILongEnergyStorage, long)} and adapted for standard {@link IEnergyStorage}s.
	 *
	 * @param source    the source energy handler
	 * @param target    the target energy handler
	 * @param maxAmount the max amount of energy to move
	 * @return the amount of moved energy
	 */
	public static int move(IEnergyStorage source, IEnergyStorage target, int maxAmount)
	{
		int simulatedExtract = source.extractEnergy(maxAmount, true);
		int simulatedInsert = target.receiveEnergy(simulatedExtract, true);
		
		int extractedAmount = source.extractEnergy(simulatedInsert, false);
		int insertedAmount = target.receiveEnergy(extractedAmount, false);
		
		if(insertedAmount < extractedAmount)
		{
			int leftover = source.receiveEnergy(extractedAmount - insertedAmount, false);
			if(leftover > 0)
			{
				LOGGER.error("Energy storage {} did not accept {} leftover energy from {}! Voiding it.", source, leftover, target);
			}
		}
		
		return insertedAmount;
	}
}
