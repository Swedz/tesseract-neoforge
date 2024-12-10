package net.swedz.tesseract.neoforge.helper;

import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.neoforged.neoforgespi.locating.IModFile;

import java.util.List;
import java.util.Objects;

public final class ScanDataHelper
{
	public static List<ModFileScanData> getAllScanData()
	{
		return LoadingModList.get().getMods().stream()
				.map(IModInfo::getOwningFile)
				.filter(Objects::nonNull)
				.map(IModFileInfo::getFile)
				.distinct()
				.map(IModFile::getScanResult)
				.toList();
	}
}
