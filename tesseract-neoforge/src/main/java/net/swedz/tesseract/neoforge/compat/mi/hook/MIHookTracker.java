package net.swedz.tesseract.neoforge.compat.mi.hook;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class MIHookTracker
{
	private static String TRACKING_MOD_ID;
	
	public static void startTracking(String modId)
	{
		if(isTracking())
		{
			throw new IllegalStateException("Tried to start tracking while there is already an active tracker");
		}
		
		TRACKING_MOD_ID = modId;
	}
	
	public static void stopTracking()
	{
		TRACKING_MOD_ID = null;
	}
	
	public static boolean isTracking()
	{
		return TRACKING_MOD_ID != null;
	}
	
	public static String getTrackingModId()
	{
		return TRACKING_MOD_ID;
	}
}
