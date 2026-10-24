package net.swedz.tesseract.neoforge.lang.placeholder.placeholder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.swedz.tesseract.api.Assert;
import net.swedz.tesseract.neoforge.lang.LangContext;
import net.swedz.tesseract.neoforge.lang.placeholder.PlaceholderFilter;
import net.swedz.tesseract.neoforge.lang.placeholder.PlaceholderProvider;
import net.swedz.tesseract.neoforge.tooltip.Parser;

import java.util.regex.Pattern;

public final class EnchantmentLangPlaceholder implements PlaceholderFilter, PlaceholderProvider
{
	private static final Pattern PATTERN = Pattern.compile("enchantment:(?<enchantment>.+)");
	
	private final Style style;
	
	public EnchantmentLangPlaceholder(Style style)
	{
		Assert.notNull(style);
		this.style = style;
	}
	
	public EnchantmentLangPlaceholder()
	{
		this(Style.EMPTY);
	}
	
	@Override
	public boolean test(String block)
	{
		return PATTERN.matcher(block).matches();
	}
	
	@Override
	public Component resolve(LangContext context, String block)
	{
		var matcher = PATTERN.matcher(block);
		if(matcher.find())
		{
			var enchantmentRawKey = matcher.group("enchantment");
			var enchantmentKey = ResourceKey.create(
					Registries.ENCHANTMENT,
					Identifier.parse(enchantmentRawKey)
			);
			var text = Parser.ENCHANTMENT.parse(context.registries(), enchantmentKey);
			if(!style.isEmpty())
			{
				return text.copy().withStyle(style);
			}
			return text;
		}
		throw new IllegalStateException("Cannot resolve block without matching against the pattern: " + block);
	}
}
