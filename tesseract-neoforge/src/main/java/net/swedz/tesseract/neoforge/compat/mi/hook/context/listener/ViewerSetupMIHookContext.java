package net.swedz.tesseract.neoforge.compat.mi.hook.context.listener;

import aztech.modern_industrialization.compat.viewer.abstraction.ViewerCategory;
import com.google.common.collect.Lists;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MIHookContext;

import java.util.List;

public final class ViewerSetupMIHookContext implements MIHookContext
{
	private final List<ViewerCategory<?>> categories = Lists.newArrayList();
	
	public void register(ViewerCategory<?> category)
	{
		categories.add(category);
	}
	
	public List<ViewerCategory<?>> getRegisteredCategories()
	{
		return categories;
	}
}
