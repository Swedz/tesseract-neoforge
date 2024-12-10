package net.swedz.tesseract.neoforge.compat.tyg;

import net.swedz.tesseract.neoforge.compat.ModLoadedHelper;
import net.swedz.tesseract.neoforge.conditionalmixins.SimpleConditionalMixinPlugin;

public final class TYGMixinPlugin extends SimpleConditionalMixinPlugin
{
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		return ModLoadedHelper.isLoaded("ohthetreesyoullgrow");
	}
}
