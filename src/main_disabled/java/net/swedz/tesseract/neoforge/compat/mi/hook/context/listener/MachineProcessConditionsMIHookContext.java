package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.recipe.condition.MachineProcessCondition;
import aztech.modern_industrialization.machines.recipe.condition.MachineProcessConditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

public final class MachineProcessConditionsMIHookContext extends MIHookContext
{
	public MachineProcessConditionsMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public <T extends MachineProcessCondition> void register(ResourceLocation id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec)
	{
		MachineProcessConditions.register(id, codec, streamCodec);
	}
}
