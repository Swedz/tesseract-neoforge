package net.swedz.tesseract.neoforge.compat.mi;

import net.swedz.tesseract.neoforge.compat.ModLoadedHelper;
import net.swedz.tesseract.neoforge.conditionalmixins.SimpleConditionalMixinPlugin;

public final class MIMixinPlugin extends SimpleConditionalMixinPlugin
{
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		return ModLoadedHelper.isLoaded("modern_industrialization");
	}
}
