package net.swedz.tesseract.neoforge.compat;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public final class ModLoadedHelper
{
	public static boolean isLoaded(String modId)
	{
		return modId != null && !modId.isEmpty() && ModList.get() != null ?
				ModList.get().isLoaded(modId) :
				FMLLoader.getCurrent().getLoadingModList().getModFileById(modId) != null;
	}
}
