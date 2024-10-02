package net.swedz.tesseract.neoforge.helper;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.List;

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
		MutableComponent mutable;
		if(component.getContents() instanceof TranslatableContents translatable)
		{
			long start2 = System.nanoTime();
			Object[] args = translatable.getArgs();
			Object[] strippedArgs = new Object[args.length];
			for(int i = 0; i < args.length; i++)
			{
				Object arg = args[i];
				strippedArgs[i] = arg instanceof Component c ? stripStyle(c) : arg;
			}
			mutable = Component.translatable(translatable.getKey(), strippedArgs);
		}
		else
		{
			mutable = component.copy();
			mutable.setStyle(Style.EMPTY);
		}
		
		List<Component> siblings = Lists.newArrayList(component.getSiblings());
		mutable.getSiblings().clear();
		for(Component sibling : siblings)
		{
			mutable.append(stripStyle(sibling));
		}
		
		return mutable;
	}
}
