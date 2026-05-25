package net.swedz.tesseract.neoforge.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.DyeRecipe;

import java.util.List;

/**
 * <p>A long time ago in a galaxy far, far away... (before 26.1.2) {@link DyedItemColor#applyDyes(DyedItemColor, List)}
 * did not take {@link DyedItemColor} as a paramter and instead an {@link ItemStack}. At that time, I was able to safely
 * access the {@link ItemStack} in question which is necessary for the implementation of {@link DynamicDyedItem}. As a
 * (hopefully) temporary workaround, I capture the {@link ItemStack} in question from both the
 * {@link DyedItemColor#applyDyes(ItemStack, List)} and {@link DyeRecipe#assemble(CraftingInput)} methods. This of
 * course does not cover every potential use of the {@link DyedItemColor#applyDyes(DyedItemColor, List)}, but one can
 * hope that other mods use the {@link DyedItemColor#applyDyes(ItemStack, List)} method.</p>
 */
public final class DynamicDyedItemTracker
{
	private static ItemStack DYEING_STACK;
	
	public static ItemStack get()
	{
		return DYEING_STACK;
	}
	
	public static void set(ItemStack stack)
	{
		DYEING_STACK = stack;
	}
}
