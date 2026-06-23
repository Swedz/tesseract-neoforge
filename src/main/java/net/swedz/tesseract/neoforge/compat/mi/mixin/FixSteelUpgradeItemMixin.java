package net.swedz.tesseract.neoforge.compat.mi.mixin;

import aztech.modern_industrialization.items.SteelUpgradeItem;
import aztech.modern_industrialization.machines.init.MachineTier;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.swedz.tesseract.neoforge.compat.mi.api.MachineTierHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SteelUpgradeItem.class)
public class FixSteelUpgradeItemMixin
{
	@Definition(
			id = "canUpgrade",
			local = @Local(type = boolean.class)
	)
	@Expression("canUpgrade")
	@ModifyExpressionValue(
			method = "useOn",
			at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean canUpgrade(
			boolean original,
			@Local(name = "be") BlockEntity blockEntity
	)
	{
		return original ||
			   (blockEntity instanceof MachineTierHolder tierHolder && tierHolder.getMachineTier() == MachineTier.BRONZE);
	}
}
