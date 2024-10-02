package net.swedz.tesseract.neoforge.compat.mi.tooltip;

import aztech.modern_industrialization.MITooltips;

public interface MIParsable
{
	<T> MIParsable arg(T arg, MITooltips.Parser<T> parser);
}
