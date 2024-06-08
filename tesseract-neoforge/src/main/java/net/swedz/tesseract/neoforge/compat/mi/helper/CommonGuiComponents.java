package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.machines.components.ActiveShapeComponent;
import aztech.modern_industrialization.machines.components.IsActiveComponent;
import aztech.modern_industrialization.machines.guicomponents.ShapeSelection;
import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBlockEntity;
import aztech.modern_industrialization.util.TextHelper;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccess;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGui;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.function.Supplier;

public final class CommonGuiComponents
{
	public static ShapeSelection.Server rangedShapeSelection(MultiblockMachineBlockEntity machine, ActiveShapeComponent activeShape, List<? extends Component> translations, boolean useArrows)
	{
		return new ShapeSelection.Server(
				new ShapeSelection.Behavior()
				{
					@Override
					public void handleClick(int line, int delta)
					{
						activeShape.incrementShape(machine, delta);
					}
					
					@Override
					public int getCurrentIndex(int line)
					{
						return activeShape.getActiveShapeIndex();
					}
				},
				new ShapeSelection.LineInfo(translations.size(), translations, useArrows)
		);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, Supplier<Long> baseEuSupplier, IsActiveComponent isActive, int height)
	{
		return new ModularMultiblockGui.Server(height, () ->
		{
			List<ModularMultiblockGuiLine> text = Lists.newArrayList();
			
			boolean shapeValid = machine.isShapeValid();
			boolean active = isActive.isActive;
			
			text.add(shapeValid ? new ModularMultiblockGuiLine(MIText.MultiblockShapeValid.text()) : new ModularMultiblockGuiLine(MIText.MultiblockShapeInvalid.text(), 0xFF0000));
			if(shapeValid)
			{
				text.add(new ModularMultiblockGuiLine(MIText.MultiblockStatusActive.text()));
				
				if(crafter != null && crafter.hasActiveRecipe())
				{
					text.add(new ModularMultiblockGuiLine(MIText.Progress.text(String.format("%.1f", crafter.getProgress() * 100) + " %")));
					
					if(crafter.getEfficiencyTicks() != 0 || crafter.getMaxEfficiencyTicks() != 0)
					{
						text.add(new ModularMultiblockGuiLine(MIText.EfficiencyTicks.text(crafter.getEfficiencyTicks(), crafter.getMaxEfficiencyTicks())));
					}
					
					text.add(new ModularMultiblockGuiLine(MIText.BaseEuRecipe.text(TextHelper.getEuTextTick(baseEuSupplier.get()))));
					
					text.add(new ModularMultiblockGuiLine(MIText.CurrentEuRecipe.text(TextHelper.getEuTextTick(crafter.getCurrentRecipeEu()))));
				}
			}
			
			return text;
		});
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, Supplier<Long> baseEuSupplier, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, crafter, baseEuSupplier, isActive, ModularMultiblockGui.H);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, IsActiveComponent isActive, int height)
	{
		return standardMultiblockScreen(machine, crafter, crafter::getBaseRecipeEu, isActive, height);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, crafter, isActive, ModularMultiblockGui.H);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, int height)
	{
		return standardMultiblockScreen(machine, null, isActive, height);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, isActive, ModularMultiblockGui.H);
	}
}
