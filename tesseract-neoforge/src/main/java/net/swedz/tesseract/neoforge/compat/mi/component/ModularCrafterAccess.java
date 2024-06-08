package net.swedz.tesseract.neoforge.compat.mi.component;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;

public interface ModularCrafterAccess extends CrafterAccess
{
	ModularCrafterAccessBehavior getBehavior();
	
	void decreaseEfficiencyTicks();
	
	void increaseEfficiencyTicks(int increment);
}
