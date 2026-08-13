package net.swedz.tesseract.neoforge.compat.guideme.tags;

import guideme.GuideBuilder;
import guideme.compiler.TagCompiler;
import guideme.compiler.tags.BoxFlowDirection;
import net.swedz.tesseract.neoforge.compat.guideme.tags.blocks.FloatingBoxTagCompiler;
import net.swedz.tesseract.neoforge.compat.guideme.tags.blocks.MarginFloatingImageCompiler;
import net.swedz.tesseract.neoforge.compat.guideme.tags.blocks.PaddedBoxTagCompiler;
import net.swedz.tesseract.neoforge.compat.guideme.tags.text.UnderlinedTextTagCompiler;

public final class TesseractGuideMETags
{
	public static void includeIn(GuideBuilder guide)
	{
		guide.extension(TagCompiler.EXTENSION_POINT, new UnderlinedTextTagCompiler());
		guide.extension(TagCompiler.EXTENSION_POINT, new FloatingBoxTagCompiler(BoxFlowDirection.ROW));
		guide.extension(TagCompiler.EXTENSION_POINT, new FloatingBoxTagCompiler(BoxFlowDirection.COLUMN));
		guide.extension(TagCompiler.EXTENSION_POINT, new MarginFloatingImageCompiler());
		guide.extension(TagCompiler.EXTENSION_POINT, new PaddedBoxTagCompiler());
	}
}
