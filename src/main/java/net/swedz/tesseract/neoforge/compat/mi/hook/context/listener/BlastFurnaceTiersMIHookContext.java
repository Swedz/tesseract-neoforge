package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHook;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

import java.util.List;

public final class BlastFurnaceTiersMIHookContext extends MIHookContext
{
	private final List<ElectricBlastFurnaceBlockEntity.Tier> tiers = Lists.newArrayList();
	
	public BlastFurnaceTiersMIHookContext(MIHook hook)
	{
		super(hook);
	}
	
	public void register(ResourceLocation coilBlockId, long maxBaseEu, String englishName)
	{
		tiers.add(new ElectricBlastFurnaceBlockEntity.Tier(coilBlockId, maxBaseEu, englishName));
	}
	
	public List<ElectricBlastFurnaceBlockEntity.Tier> getRegisteredTiers()
	{
		return tiers;
	}
}
