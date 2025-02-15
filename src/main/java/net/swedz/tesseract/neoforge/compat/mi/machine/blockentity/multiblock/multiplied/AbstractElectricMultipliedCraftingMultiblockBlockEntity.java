package net.swedz.tesseract.neoforge.compat.mi.machine.blockentity.multiblock.multiplied;

import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyListComponentHolder;
import aztech.modern_industrialization.machines.BEP;
import aztech.modern_industrialization.machines.components.EnergyComponent;
import aztech.modern_industrialization.machines.components.OverdriveComponent;
import aztech.modern_industrialization.machines.components.RedstoneControlComponent;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import aztech.modern_industrialization.machines.guicomponents.SlotPanel;
import aztech.modern_industrialization.machines.init.MachineTier;
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
import net.swedz.tesseract.neoforge.TesseractText;
import net.swedz.tesseract.neoforge.compat.mi.api.MachineTierHolder;
import net.swedz.tesseract.neoforge.compat.mi.helper.ModularLubricantHelper;

import java.util.List;

import static net.swedz.tesseract.neoforge.compat.mi.TesseractMITooltips.*;
import static net.swedz.tesseract.neoforge.compat.mi.tooltip.MICompatibleTextLine.*;

public abstract class AbstractElectricMultipliedCraftingMultiblockBlockEntity extends AbstractMultipliedCraftingMultiblockBlockEntity implements EnergyListComponentHolder, MachineTierHolder
{
	protected final MachineTier machineTier;
	
	protected final UpgradeComponent         upgrades;
	protected final RedstoneControlComponent redstoneControl;
	protected final OverdriveComponent       overdrive;
	
	protected final List<EnergyComponent> energyInputs = Lists.newArrayList();
	
	public AbstractElectricMultipliedCraftingMultiblockBlockEntity(BEP bep, ResourceLocation id, ShapeTemplate[] shapeTemplates,
																   MachineTier machineTier)
	{
		super(bep, id, shapeTemplates);
		
		this.machineTier = machineTier;
		
		this.upgrades = new UpgradeComponent();
		this.redstoneControl = new RedstoneControlComponent();
		this.overdrive = new OverdriveComponent();
		
		this.registerComponents(upgrades, redstoneControl, overdrive);
		
		this.registerGuiComponent(new SlotPanel.Server(this)
				.withRedstoneControl(redstoneControl)
				.withUpgrades(upgrades)
				.withOverdrive(overdrive));
	}
	
	@Override
	public List<? extends EnergyAccess> getEnergyComponents()
	{
		return energyInputs;
	}
	
	@Override
	public MachineTier getMachineTier()
	{
		return machineTier;
	}
	
	@Override
	protected void onRematch(ShapeMatcher shapeMatcher)
	{
		super.onRematch(shapeMatcher);
		
		energyInputs.clear();
		for(HatchBlockEntity hatch : shapeMatcher.getMatchedHatches())
		{
			hatch.appendEnergyInputs(energyInputs);
		}
	}
	
	@Override
	protected ItemInteractionResult useItemOn(Player player, InteractionHand hand, Direction face)
	{
		ItemInteractionResult result = super.useItemOn(player, hand, face);
		if(!result.consumesAction())
		{
			result = ModularLubricantHelper.onUse(crafter, player, hand);
		}
		if(!result.consumesAction())
		{
			result = components.mapOrDefault(UpgradeComponent.class, upgrade -> upgrade.onUse(this, player, hand), result);
		}
		if(!result.consumesAction())
		{
			result = redstoneControl.onUse(this, player, hand);
		}
		if(!result.consumesAction())
		{
			result = components.mapOrDefault(OverdriveComponent.class, (overdrive) -> overdrive.onUse(this, player, hand), result);
		}
		return result;
	}
	
	@Override
	public boolean isEnabled()
	{
		return redstoneControl.doAllowNormalOperation(this);
	}
	
	@Override
	public long consumeEu(long max, Simulation simulation)
	{
		long total = 0;
		for(EnergyComponent energyComponent : energyInputs)
		{
			total += energyComponent.consumeEu(max - total, simulation);
		}
		return total;
	}
	
	@Override
	public long getBaseRecipeEu()
	{
		return machineTier.getBaseEu();
	}
	
	@Override
	public long getBaseMaxRecipeEu()
	{
		return machineTier.getMaxEu() + upgrades.getAddMaxEUPerTick();
	}
	
	@Override
	public boolean isOverdriving()
	{
		return overdrive.shouldOverdrive();
	}
	
	@Override
	public List<Component> getTooltips()
	{
		return List.of(
				line(TesseractText.MI_MACHINE_BATCHER_RECIPE).arg(true, this.getRecipeType(), MACHINE_RECIPE_TYPE_PARSER),
				line(TesseractText.MI_MACHINE_BATCHER_SIZE_AND_COST).arg(this.getMaxMultiplier()).arg(this.getEuCostTransformer(), EU_COST_TRANSFORMER_PARSER)
		);
	}
}
