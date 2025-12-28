package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.machines.components.LubricantHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccess;

public final class ModularLubricantHelper
{
	/**
	 * This is literally the exact same code from {@link LubricantHelper} but it is for the {@link ModularCrafterAccess}...
	 */
	public static ItemInteractionResult onUse(ModularCrafterAccess crafter, Player player, InteractionHand hand)
	{
		if(crafter.hasActiveRecipe())
		{
			int tick = crafter.getEfficiencyTicks();
			int maxTick = crafter.getMaxEfficiencyTicks();
			int rem = maxTick - tick;
			if(rem > 0)
			{
				int maxAllowedLubricant = rem * LubricantHelper.mbPerTick;
				FluidTank interactionTank = new FluidTank(maxAllowedLubricant, stack -> stack.getFluid() == MIFluids.LUBRICANT.asFluid());
				FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(
						player.getItemInHand(hand),
						interactionTank,
						new PlayerMainInvWrapper(player.getInventory()),
						maxAllowedLubricant,
						player,
						true
				);
				
				if(result.isSuccess() && interactionTank.getFluidAmount() >= LubricantHelper.mbPerTick)
				{
					crafter.increaseEfficiencyTicks(interactionTank.getFluidAmount() / LubricantHelper.mbPerTick);
					player.setItemInHand(hand, result.getResult());
					return ItemInteractionResult.SUCCESS;
				}
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
