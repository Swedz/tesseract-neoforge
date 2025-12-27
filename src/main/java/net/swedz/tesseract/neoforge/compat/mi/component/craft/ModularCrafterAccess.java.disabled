package net.swedz.tesseract.neoforge.compat.mi.component.craft;

import aztech.modern_industrialization.api.machine.component.CrafterAccess;
import net.swedz.tesseract.neoforge.compat.mi.api.ActiveRecipeHolder;

public interface ModularCrafterAccess<R> extends CrafterAccess, ActiveRecipeHolder<R>
{
	ModularCrafterAccessBehavior getBehavior();
	
	void decreaseEfficiencyTicks();
	
	void increaseEfficiencyTicks(int increment);
}
