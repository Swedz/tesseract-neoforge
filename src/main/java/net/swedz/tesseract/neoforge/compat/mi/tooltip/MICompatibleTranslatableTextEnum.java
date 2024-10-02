package net.swedz.tesseract.neoforge.compat.mi.tooltip;

import aztech.modern_industrialization.MITooltips;
import net.swedz.tesseract.neoforge.tooltip.BiParser;
import net.swedz.tesseract.neoforge.tooltip.Parser;
import net.swedz.tesseract.neoforge.tooltip.TranslatableTextEnum;

public interface MICompatibleTranslatableTextEnum extends TranslatableTextEnum, MIParsable
{
	@Override
	default <T> MICompatibleTextLine arg(T arg, Parser<T> parser)
	{
		return MICompatibleTextLine.line(this).arg(arg, parser);
	}
	
	@Override
	default <A, B> MICompatibleTextLine arg(A a, B b, BiParser<A, B> parser)
	{
		return MICompatibleTextLine.line(this).arg(a, b, parser);
	}
	
	@Override
	default <T> MICompatibleTextLine arg(T arg, MITooltips.Parser<T> parser)
	{
		return MICompatibleTextLine.line(this).arg(arg, parser);
	}
	
	@Override
	default MICompatibleTextLine arg(Object arg)
	{
		return MICompatibleTextLine.line(this).arg(arg);
	}
}
