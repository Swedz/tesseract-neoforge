package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied;

import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.helper.SteamHelper;
import aztech.modern_industrialization.machines.multiblocks.HatchBlockEntity;
import aztech.modern_industrialization.machines.multiblocks.ShapeMatcher;
import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import aztech.modern_industrialization.util.Simulation;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.swedz.tesseract.neoforge.compat.mi.TesseractMI;
import net.swedz.tesseract.neoforge.compat.mi.api.SteamMachineTierHolder;

import java.util.List;

public abstract class AbstractSteamMultipliedCraftingMultiblockBlockEntity extends AbstractMultipliedCraftingMultiblockBlockEntity implements SteamMachineTierHolder
{
	protected final OverclockComponent overclock;
	
	protected boolean steel;
	
	public AbstractSteamMultipliedCraftingMultiblockBlockEntity(BEP bep, ResourceLocation id, ShapeTemplate[] shapeTemplates,
																List<OverclockComponent.Catalyst> overclockCatalysts)
	{
		super(bep, id, shapeTemplates);
		
		overclock = new OverclockComponent(overclockCatalysts);
		
		this.registerComponents(overclock);
	}
	
	@Override
	public boolean isSteelTier()
	{
		return steel;
	}
	
	@Override
	protected void onRematch(ShapeMatcher shapeMatcher)
	{
		super.onRematch(shapeMatcher);
		
		steel = false;
		for(HatchBlockEntity hatch : shapeMatcher.getMatchedHatches())
		{
			if(hatch.upgradesToSteel())
			{
				steel = true;
				break;
			}
		}
	}
	
	@Override
	protected ItemInteractionResult useItemOn(Player player, InteractionHand hand, Direction face)
	{
		ItemInteractionResult result = super.useItemOn(player, hand, face);
		return !result.consumesAction() ? overclock.onUse(this, player, hand) : result;
	}
	
	@Override
	public long consumeEu(long max, Simulation simulation)
	{
		return SteamHelper.consumeSteamEu(inventory.getFluidInputs(), max, simulation);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		overclock.tick(this);
	}
	
	@Override
	public long getBaseRecipeEu()
	{
		return overclock.getRecipeEu(steel ? 4 : 2);
	}
	
	@Override
	public long getBaseMaxRecipeEu()
	{
		return this.getBaseRecipeEu();
	}
	
	@Override
	public List<Component> getTooltips()
	{
		List<Component> tooltips = Lists.newArrayList();
		tooltips.addAll(overclock.getTooltips());
		tooltips.add(TesseractMI.text().machineBatcherRecipe(false, this.getRecipeType()));
		tooltips.add(TesseractMI.text().machineBatcherSizeAndCost(this.getMaxMultiplier(), this.getEuCostTransformer()));
		return tooltips;
	}
}
