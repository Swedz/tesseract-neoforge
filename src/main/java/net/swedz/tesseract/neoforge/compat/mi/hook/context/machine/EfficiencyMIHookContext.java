package net.swedz.tesseract.neoforge.compat.mi.hook.context.machine;

import aztech.modern_industrialization.machines.MachineBlockEntity;

public class EfficiencyMIHookContext extends MachineMIHookContext
{
	private final boolean hasActiveRecipe;
	
	private boolean modified;
	
	private boolean cancelled;
	
	private final int  maxEfficiencyTicks;
	private       int  efficiencyTicks;
	private       long maxRecipeEu;
	
	public EfficiencyMIHookContext(MachineBlockEntity machineBlockEntity, boolean hasActiveRecipe,
								   int maxEfficiencyTicks, int efficiencyTicks, long maxRecipeEu)
	{
		super(machineBlockEntity);
		this.hasActiveRecipe = hasActiveRecipe;
		
		this.maxEfficiencyTicks = maxEfficiencyTicks;
		this.efficiencyTicks = efficiencyTicks;
		this.maxRecipeEu = maxRecipeEu;
	}
	
	public boolean hasActiveRecipe()
	{
		return hasActiveRecipe;
	}
	
	public boolean hasBeenModified()
	{
		return modified;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled)
	{
		if(this.cancelled != cancelled)
		{
			modified = true;
		}
		this.cancelled = cancelled;
	}
	
	public int getMaxEfficiencyTicks()
	{
		return maxEfficiencyTicks;
	}
	
	public int getEfficiencyTicks()
	{
		return efficiencyTicks;
	}
	
	public void setEfficiencyTicks(int efficiencyTicks)
	{
		if(this.efficiencyTicks != efficiencyTicks)
		{
			modified = true;
		}
		this.efficiencyTicks = efficiencyTicks;
	}
	
	public long getMaxRecipeEu()
	{
		return maxRecipeEu;
	}
	
	public void setMaxRecipeEu(long maxRecipeEu)
	{
		if(this.maxRecipeEu != maxRecipeEu)
		{
			modified = true;
		}
		this.maxRecipeEu = maxRecipeEu;
	}
}
