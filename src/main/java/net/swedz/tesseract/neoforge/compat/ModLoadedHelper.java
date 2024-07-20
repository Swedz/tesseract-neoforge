package net.swedz.tesseract.neoforge.compat;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;

public final class ModLoadedHelper
{
	public static boolean isLoaded(String modId)
	{
		return ModList.get() != null ?
				ModList.get().isLoaded(modId) :
				LoadingModList.get().getMods().stream().anyMatch((m) -> m.getModId().equals(modId));
	}
}
