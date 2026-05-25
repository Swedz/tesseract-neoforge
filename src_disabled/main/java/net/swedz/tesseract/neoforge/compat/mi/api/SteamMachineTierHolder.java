package net.swedz.tesseract.neoforge.compat.mi.api;

import aztech.modern_industrialization.machines.init.MachineTier;

public interface SteamMachineTierHolder extends MachineTierHolder
{
	boolean isSteelTier();
	
	@Override
	default MachineTier getMachineTier()
	{
		return this.isSteelTier() ? MachineTier.STEEL : MachineTier.BRONZE;
	}
}
