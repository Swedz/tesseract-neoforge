package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import aztech.modern_industrialization.MIText;
import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import aztech.modern_industrialization.machines.components.OverclockComponent;
import aztech.modern_industrialization.machines.components.ShapeValidComponent;
import aztech.modern_industrialization.machines.multiblocks.MultiblockMachineBlockEntity;
import aztech.modern_industrialization.util.TextHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.swedz.tesseract.api.Assert;

import java.util.List;

import static net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock.ModularMultiblockGuiLine.*;

public final class CommonMultiblockGuiBuilder
{
	private final ShapeValidComponent shapeValid;
	
	private CrafterAccess crafter;
	
	private OverclockComponent overclock;
	
	private int y      = 0;
	private int height = ModularMultiblockGui.HEIGHT;
	
	public CommonMultiblockGuiBuilder(ShapeValidComponent shapeValid)
	{
		Assert.notNull(shapeValid, "ShapeValidComponent must be provided");
		this.shapeValid = shapeValid;
	}
	
	public CommonMultiblockGuiBuilder(MultiblockMachineBlockEntity machine)
	{
		this(machine.shapeValid);
	}
	
	public CommonMultiblockGuiBuilder crafter(CrafterAccess crafter)
	{
		this.crafter = crafter;
		return this;
	}
	
	public CommonMultiblockGuiBuilder overclock(OverclockComponent overclock)
	{
		this.overclock = overclock;
		return this;
	}
	
	public CommonMultiblockGuiBuilder y(int y)
	{
		this.y = y;
		return this;
	}
	
	public CommonMultiblockGuiBuilder height(int height)
	{
		Assert.that(height > 4, String.format("Provided height outside of acceptable bounds: must be >4 but %d was provided", height));
		this.height = height;
		return this;
	}
	
	private void contents(ModularMultiblockGuiContent content)
	{
		boolean isShapeValid = shapeValid.shapeValid;
		
		content.add((isShapeValid ? MIText.MultiblockShapeValid : MIText.MultiblockShapeInvalid).text(), isShapeValid ? WHITE : RED);
		
		if(isShapeValid)
		{
			content.add(MIText.MultiblockStatusActive.text());
		}
		
		if(crafter != null && crafter.matchesMultipleRecipes())
		{
			content.add(MIText.MachineMultipleRecipes1.text(), RED);
		}
		
		if(isShapeValid && crafter != null && crafter.hasActiveRecipe())
		{
			content.add(MIText.Progress.text(String.format("%.1f", crafter.getProgress() * 100) + " %"));
			
			if(crafter.getEfficiencyTicks() != 0 || crafter.getMaxEfficiencyTicks() != 0)
			{
				content.add(MIText.EfficiencyTicks.text(crafter.getEfficiencyTicks(), crafter.getMaxEfficiencyTicks()));
			}
			
			content.add(MIText.BaseEuRecipe.text(TextHelper.getEuTextTick(crafter.getBaseRecipeEu())));
			
			content.add(MIText.CurrentEuRecipe.text(TextHelper.getEuTextTick(crafter.getCurrentRecipeEu())));
		}
		
		if(overclock != null)
		{
			int ticks = overclock.getTicks();
			if(ticks > 0)
			{
				content.add(formatOverclockText(ticks));
			}
		}
	}
	
	private List<Component> tooltips()
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
	
	public ModularMultiblockGui build()
	{
		return new ModularMultiblockGui(
				y,
				height,
				this::contents,
				this::tooltips
		);
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
