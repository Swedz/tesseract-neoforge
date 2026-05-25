package net.swedz.tesseract.neoforge.compat.mi.hook;

public interface MIHookInstance
{
	/**
	 * Gets the mod ID that should be referenced in the hook. This should only be overridden if you really know what
	 * you are doing. Things can get messy.
	 *
	 * @return the mod ID, or null to automatically inherit the mod's actual ID
	 */
	default String modId()
	{
		return null;
	}
	
	/**
	 * Checks whether this hook should initialize or not.
	 *
	 * @return true if the hook should initialize, false if it should be ignored
	 */
	default boolean shouldInitialize()
	{
		return true;
	}
}
