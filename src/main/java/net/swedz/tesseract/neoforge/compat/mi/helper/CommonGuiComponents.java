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

import java.util.List;
import java.util.function.Supplier;

import static net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine.*;

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
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, Supplier<Long> baseEuSupplier, IsActiveComponent isActive, int y, int height)
	{
		return new ModularMultiblockGui.Server(y, height, (content) ->
		{
			boolean shapeValid = machine.isShapeValid();
			boolean active = isActive.isActive;
			
			content.add((shapeValid ? MIText.MultiblockShapeValid : MIText.MultiblockShapeInvalid).text(), shapeValid ? WHITE : RED);
			if(shapeValid)
			{
				content.add(MIText.MultiblockStatusActive.text());
				
				if(crafter != null && crafter.hasActiveRecipe())
				{
					content.add(MIText.Progress.text(String.format("%.1f", crafter.getProgress() * 100) + " %"));
					
					if(crafter.getEfficiencyTicks() != 0 || crafter.getMaxEfficiencyTicks() != 0)
					{
						content.add(MIText.EfficiencyTicks.text(crafter.getEfficiencyTicks(), crafter.getMaxEfficiencyTicks()));
					}
					
					content.add(MIText.BaseEuRecipe.text(TextHelper.getEuTextTick(baseEuSupplier.get())));
					
					content.add(MIText.CurrentEuRecipe.text(TextHelper.getEuTextTick(crafter.getCurrentRecipeEu())));
				}
			}
		});
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, Supplier<Long> baseEuSupplier, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, crafter, baseEuSupplier, isActive, 0, ModularMultiblockGui.HEIGHT);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, IsActiveComponent isActive, int y, int height)
	{
		return standardMultiblockScreen(machine, crafter, crafter::getBaseRecipeEu, isActive, y, height);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, IsActiveComponent isActive, int height)
	{
		return standardMultiblockScreen(machine, crafter, crafter::getBaseRecipeEu, isActive, 0, height);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess crafter, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, crafter, isActive, ModularMultiblockGui.HEIGHT);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, int y, int height)
	{
		return standardMultiblockScreen(machine, null, null, isActive, y, height);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, int height)
	{
		return standardMultiblockScreen(machine, null, null, isActive, 0, height);
	}
	
	public static ModularMultiblockGui.Server standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, isActive, ModularMultiblockGui.HEIGHT);
	}
}
