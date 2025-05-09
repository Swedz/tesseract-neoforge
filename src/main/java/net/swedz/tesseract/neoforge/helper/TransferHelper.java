package net.swedz.tesseract.neoforge.helper;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper;
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
	 * Attempts to insert the stack into the item handler. It will continue to iterate over all slots until it either
	 * has inserted all of the item, or there are no slots remaining.
	 *
	 * @param target   the target item handler
	 * @param toInsert the stack to insert
	 * @param simulate whether the extraction should be simulated or not
	 * @return the amount inserted
	 */
	public static int insert(IItemHandler target, ItemStack toInsert, boolean simulate)
	{
		int amountInserted = 0;
		
		for(int slot = 0; slot < target.getSlots(); slot++)
		{
			if(target.isItemValid(slot, toInsert))
			{
				var stack = target.getStackInSlot(slot);
				if(stack.isEmpty() || ItemStack.isSameItemSameComponents(stack, toInsert))
				{
					int amountToInsert = Math.min(toInsert.getCount() - amountInserted, target.getSlotLimit(slot));
					var remaining = target.insertItem(slot, toInsert.copyWithCount(amountToInsert), simulate);
					amountInserted += (amountToInsert - remaining.getCount());
					if(amountInserted >= toInsert.getCount())
					{
						break;
					}
				}
			}
		}
		
		return amountInserted;
	}
	
	/**
	 * Attempts to insert the stack into the item handler. It will continue to iterate over all slots until it either
	 * has inserted all of the item, or there are no slots remaining.
	 *
	 * @param target   the target item handler
	 * @param toInsert the stack to insert
	 * @return the amount inserted
	 */
	public static int insert(IItemHandler target, ItemStack toInsert)
	{
		return insert(target, toInsert, false);
	}
	
	/**
	 * Attempts to extract items matching a predicate into a single stack. The first extracted item will determine what
	 * base item and components to use - all other matching stacks will be checked against this as well as the
	 * predicate.
	 * <br><br>
	 * Taken from {@link aztech.modern_industrialization.util.TransferHelper#extractMatching(Inventory, Predicate, int, boolean)} and is unchanged.
	 *
	 * @param inventory  the player inventory to use as the source
	 * @param predicate  the predicate to check against
	 * @param maxAmount  the max amount of items to extract
	 * @param containers whether item containing items in the inventory should be searched as well
	 * @param simulate   whether the extraction should be simulated or not
	 * @return the extracted and combined stack
	 */
	public static ItemStack extractMatching(Inventory inventory, Predicate<ItemStack> predicate, int maxAmount, boolean containers, boolean simulate)
	{
		int sourceSlots = inventory.getContainerSize();
		
		var ret = extractMatching(new PlayerInvWrapper(inventory), predicate, maxAmount, simulate);
		
		if(containers)
		{
			if(!ret.isEmpty())
			{
				final var finalRet = ret;
				predicate = other -> ItemStack.isSameItemSameComponents(finalRet, other);
			}
			for(int slot = 0; slot < sourceSlots && maxAmount > ret.getCount(); ++slot)
			{
				var stack = inventory.getItem(slot);
				if(stack.getCount() != 1)
				{
					continue;
				}
				var capability = stack.getCapability(Capabilities.ItemHandler.ITEM);
				if(capability != null)
				{
					var extracted = extractMatching(capability, predicate, maxAmount - ret.getCount(), simulate);
					if(ret.isEmpty())
					{
						ret = extracted;
					}
					else
					{
						ret.grow(extracted.getCount());
					}
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Attempts to extract items matching a predicate into a single stack. The first extracted item will determine what
	 * base item and components to use - all other matching stacks will be checked against this as well as the
	 * predicate.
	 * <br><br>
	 * Taken from {@link aztech.modern_industrialization.util.TransferHelper#extractMatching(Inventory, Predicate, int, boolean)} and is unchanged.
	 *
	 * @param inventory  the player inventory to use as the source
	 * @param predicate  the predicate to check against
	 * @param maxAmount  the max amount of items to extract
	 * @param containers whether item containing items in the inventory should be searched as well
	 * @return the extracted and combined stack
	 */
	public static ItemStack extractMatching(Inventory inventory, Predicate<ItemStack> predicate, int maxAmount, boolean containers)
	{
		return extractMatching(inventory, predicate, maxAmount, containers, false);
	}
	
	/**
	 * Attempts to extract items matching a predicate into a single stack. The first extracted item will determine what
	 * base item and components to use - all other matching stacks will be checked against this as well as the
	 * predicate.
	 * <br><br>
	 * Taken from {@link aztech.modern_industrialization.util.TransferHelper#extractMatching(IItemHandler, Predicate, int)} and is unchanged.
	 *
	 * @param source    the source item handler
	 * @param predicate the predicate to check against, if null then no filter will be used
	 * @param maxAmount the max amount of items to extract
	 * @param simulate  whether the extraction should be simulated or not
	 * @return the extracted and combined stack
	 */
	public static ItemStack extractMatching(IItemHandler source, Predicate<ItemStack> predicate, int maxAmount, boolean simulate)
	{
		int sourceSlots = source.getSlots();
		
		ItemStack ret = ItemStack.EMPTY;
		int slot;
		for(slot = 0; slot < sourceSlots && ret.isEmpty(); ++slot)
		{
			var stack = source.getStackInSlot(slot);
			if(predicate == null || predicate.test(stack))
			{
				ret = source.extractItem(slot, Math.min(stack.getCount(), maxAmount), simulate);
			}
		}
		if(ret.isEmpty())
		{
			return ItemStack.EMPTY;
		}
		
		for(; slot < sourceSlots && maxAmount > ret.getCount(); ++slot)
		{
			var stack = source.getStackInSlot(slot);
			if(ItemStack.isSameItemSameComponents(stack, ret))
			{
				var extracted = source.extractItem(slot, Math.min(stack.getCount(), maxAmount - ret.getCount()), simulate);
				ret.grow(extracted.getCount());
			}
		}
		
		return ret;
	}
	
	/**
	 * Attempts to extract items matching a predicate into a single stack. The first extracted item will determine what
	 * base item and components to use - all other matching stacks will be checked against this as well as the
	 * predicate.
	 * <br><br>
	 * Taken from {@link aztech.modern_industrialization.util.TransferHelper#extractMatching(IItemHandler, Predicate, int)} and is unchanged.
	 *
	 * @param source    the source item handler
	 * @param predicate the predicate to check against, if null then no filter will be used
	 * @param maxAmount the max amount of items to extract
	 * @return the extracted and combined stack
	 */
	public static ItemStack extractMatching(IItemHandler source, Predicate<ItemStack> predicate, int maxAmount)
	{
		return extractMatching(source, predicate, maxAmount, false);
	}
	
	/**
	 * Attempts to extract items. The first extracted item will determine what base item and components to use - all
	 * other subsequent stacks will be checked against this.
	 *
	 * @param source    the source item handler
	 * @param maxAmount the max amount of items to extract
	 * @param simulate  whether the extraction should be simulated or not
	 * @return the extracted and combined stack
	 */
	public static ItemStack extractFirst(IItemHandler source, int maxAmount, boolean simulate)
	{
		return extractMatching(source, null, maxAmount, simulate);
	}
	
	/**
	 * Attempts to extract items. The first extracted item will determine what base item and components to use - all
	 * other subsequent stacks will be checked against this.
	 *
	 * @param source    the source item handler
	 * @param maxAmount the max amount of items to extract
	 * @return the extracted and combined stack
	 */
	public static ItemStack extractFirst(IItemHandler source, int maxAmount)
	{
		return extractFirst(source, maxAmount, false);
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
