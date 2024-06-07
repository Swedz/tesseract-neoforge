package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessConditions;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookContext;

public final class MachineProcessConditionsMIHookContext implements MIHookContext
{
	public static void register(ResourceLocation id, Codec<? extends MachineProcessCondition> serializer)
	{
		MachineProcessConditions.register(id, serializer);
	}
}
