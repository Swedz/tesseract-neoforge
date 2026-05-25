package net.swedz.tesseract.neoforge.compat.mi.mixin.accessor;

import aztech.modern_industrialization.inventory.AbstractConfigurableStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
		value = AbstractConfigurableStack.class,
		remap = false
)
public interface ConfigurableStackAccessor<T>
{
	@Accessor("lockedInstance")
	void setLockedInstance(T value);
	
	@Accessor("playerLocked")
	void setPlayerLocked(boolean value);
	
	@Accessor("machineLocked")
	void setMachineLocked(boolean value);
	
	@Accessor("playerLockable")
	void setPlayerLockable(boolean value);
	
	@Accessor("playerInsert")
	void setPlayerInsert(boolean value);
	
	@Accessor("playerExtract")
	void setPlayerExtract(boolean value);
	
	@Accessor("pipesInsert")
	void setPipesInsert(boolean value);
	
	@Accessor("pipesExtract")
	void setPipesExtract(boolean value);
}
