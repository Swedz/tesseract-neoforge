package net.swedz.tesseract.neoforge.compat.guideme.tags.blocks;

import guideme.document.LytRect;
import guideme.document.block.LytHBox;
import guideme.layout.LayoutContext;

public final class LytSizedHBox extends LytHBox
{
	private final int preferredWidth;
	
	public LytSizedHBox(int preferredWidth)
	{
		super();
		this.preferredWidth = preferredWidth;
	}
	
	@Override
	protected LytRect computeBoxLayout(LayoutContext context, int x, int y, int availableWidth)
	{
		return super.computeBoxLayout(context, x, y, preferredWidth == -1 ? availableWidth : Math.min(preferredWidth, availableWidth));
	}
}
