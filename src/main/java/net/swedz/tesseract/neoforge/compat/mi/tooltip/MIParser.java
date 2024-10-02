package net.swedz.tesseract.neoforge.compat.mi.tooltip;

import aztech.modern_industrialization.api.energy.CableTier;
import net.swedz.tesseract.neoforge.tooltip.Parser;

public interface MIParser
{
	Parser<CableTier> CABLE_TIER_SHORT = CableTier::shortEnglishName;
	Parser<CableTier> CABLE_TIER_LONG  = CableTier::longEnglishName;
}
