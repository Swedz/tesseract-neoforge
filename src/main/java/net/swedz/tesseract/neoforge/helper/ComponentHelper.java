package net.swedz.tesseract.neoforge.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public final class ComponentHelper
{
	/**
	 * Strip the style from a {@link Component} and all its siblings.
	 *
	 * @param component the {@link Component}
	 * @return the stripped {@link Component}
	 */
	public static Component stripStyle(Component component)
	{
		MutableComponent mutable = component.copy();
		
		mutable.setStyle(Style.EMPTY);
		
		mutable.getSiblings().clear();
		for(Component sibling : component.getSiblings())
		{
			mutable.append(stripStyle(sibling));
		}
		
		return mutable;
	}
}
