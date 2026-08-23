package net.swedz.tesseract.neoforge.compat.mi.helper;

import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.machines.components.ActiveShapeComponent;
import aztech.modern_industrialization.machines.components.IsActiveComponent;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.guicomponents.ShapeSelection;
import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBlockEntity;
import aztech.modern_industrialization.util.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccess;
import net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGui;

import java.util.List;
import java.util.function.Supplier;

import static net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine.*;

public final class CommonGuiComponents
{
	public static ShapeSelection rangedShapeSelection(MultiblockMachineBlockEntity machine, ActiveShapeComponent activeShape, List<Component> translations, boolean useArrows)
	{
		return new ShapeSelection(
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
				new ShapeSelection.LineInfo(translations, useArrows)
		);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(
			MultiblockMachineBlockEntity machine,
			ModularCrafterAccess<?> crafter,
			Supplier<Long> baseEuSupplier,
			IsActiveComponent isActive,
			OverclockComponent overclock,
			int y,
			int height
	)
	{
		return new ModularMultiblockGui(
				y,
				height,
				(content) ->
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
					if(crafter != null && crafter.matchesMultipleRecipes())
					{
						content.add(MIText.MachineMultipleRecipes1.text(), RED);
					}
					if(overclock != null)
					{
						int ticks = overclock.getTicks();
						if(ticks > 0)
						{
							content.add(formatOverclockText(ticks));
						}
					}
				},
				() ->
				{
					if(crafter != null && crafter.matchesMultipleRecipes())
					{
						return List.of(
								MIText.MachineMultipleRecipes1.text().withStyle(ChatFormatting.RED),
								MIText.MachineMultipleRecipes2.text().withStyle(ChatFormatting.RED)
						);
					}
					return List.of();
				}
		);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, Supplier<Long> baseEuSupplier, IsActiveComponent isActive, OverclockComponent overclock)
	{
		return standardMultiblockScreen(machine, crafter, baseEuSupplier, isActive, overclock, 0, ModularMultiblockGui.HEIGHT);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, Supplier<Long> baseEuSupplier, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, crafter, baseEuSupplier, isActive, null, 0, ModularMultiblockGui.HEIGHT);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, IsActiveComponent isActive, OverclockComponent overclock, int y, int height)
	{
		return standardMultiblockScreen(machine, crafter, crafter::getBaseRecipeEu, isActive, overclock, y, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, IsActiveComponent isActive, int y, int height)
	{
		return standardMultiblockScreen(machine, crafter, crafter::getBaseRecipeEu, isActive, null, y, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, IsActiveComponent isActive, OverclockComponent overclock, int height)
	{
		return standardMultiblockScreen(machine, crafter, crafter::getBaseRecipeEu, isActive, overclock, 0, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, IsActiveComponent isActive, int height)
	{
		return standardMultiblockScreen(machine, crafter, crafter::getBaseRecipeEu, isActive, null, 0, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, IsActiveComponent isActive, OverclockComponent overclock)
	{
		return standardMultiblockScreen(machine, crafter, isActive, overclock, ModularMultiblockGui.HEIGHT);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, ModularCrafterAccess<?> crafter, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, crafter, isActive, ModularMultiblockGui.HEIGHT);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, OverclockComponent overclock, int y, int height)
	{
		return standardMultiblockScreen(machine, null, null, isActive, overclock, y, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, int y, int height)
	{
		return standardMultiblockScreen(machine, null, null, isActive, null, y, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, OverclockComponent overclock, int height)
	{
		return standardMultiblockScreen(machine, null, null, isActive, overclock, 0, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, int height)
	{
		return standardMultiblockScreen(machine, null, null, isActive, null, 0, height);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive, OverclockComponent overclock)
	{
		return standardMultiblockScreen(machine, isActive, overclock, ModularMultiblockGui.HEIGHT);
	}
	
	public static ModularMultiblockGui standardMultiblockScreen(MultiblockMachineBlockEntity machine, IsActiveComponent isActive)
	{
		return standardMultiblockScreen(machine, isActive, ModularMultiblockGui.HEIGHT);
	}
	
	private static Component formatOverclockText(int ticks)
	{
		int seconds = ticks / 20;
		int hours = seconds / 3600;
		int minutes = seconds % 3600 / 60;
		String time = String.format("%d", seconds);
		if(hours > 0)
		{
			time = String.format("%d:%02d:%02d", hours, minutes, seconds % 60);
		}
		else if(minutes > 0)
		{
			time = String.format("%d:%02d", minutes, seconds % 60);
		}
		return MIText.GunpowderTime.text(time);
	}
}
