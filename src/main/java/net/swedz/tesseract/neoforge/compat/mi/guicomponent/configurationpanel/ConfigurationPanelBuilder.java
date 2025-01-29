package net.swedz.tesseract.neoforge.compat.mi.guicomponent.configurationpanel;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class ConfigurationPanelBuilder
{
	private final Component title, description;
	
	private final ClickHandler handleClick;
	
	private final List<Line> lines = Lists.newArrayList();
	
	public ConfigurationPanelBuilder(Component title, Component description,
									 ClickHandler handleClick)
	{
		this.title = title;
		this.description = description;
		this.handleClick = handleClick;
	}
	
	public ConfigurationPanelBuilder(Component title, Component description)
	{
		this(title, description, null);
	}
	
	public ConfigurationPanelBuilder add(List<? extends Component> translations, boolean useArrows,
										 LineClickHandler handleClickAction, CurrentIndexSupplier currentIndexGetter)
	{
		lines.add(new Line(translations, useArrows, handleClickAction, currentIndexGetter));
		return this;
	}
	
	public ConfigurationPanel.Server build()
	{
		return new ConfigurationPanel.Server(
				new ConfigurationPanel.Behavior()
				{
					@Override
					public void handleClick(int lineIndex, int delta)
					{
						lines.get(lineIndex).handleClick(delta);
						if(handleClick != null)
						{
							handleClick.handleClick(lineIndex, delta);
						}
					}
					
					@Override
					public int getCurrentIndex(int lineIndex)
					{
						return lines.get(lineIndex).getCurrentIndex();
					}
				},
				title, description,
				lines.stream().map((line) -> new ConfigurationPanel.LineInfo(line.numValues(), line.translations(), line.useArrows())).toArray(ConfigurationPanel.LineInfo[]::new)
		);
	}
	
	private record Line(
			List<? extends Component> translations, boolean useArrows,
			LineClickHandler handleClickAction, CurrentIndexSupplier currentIndexGetter
	)
	{
		public int numValues()
		{
			return translations.size();
		}
		
		public void handleClick(int delta)
		{
			handleClickAction.handleClick(delta);
		}
		
		public int getCurrentIndex()
		{
			return currentIndexGetter.getCurrentIndex();
		}
	}
	
	public interface ClickHandler
	{
		void handleClick(int lineIndex, int delta);
	}
	
	public interface LineClickHandler
	{
		void handleClick(int delta);
	}
	
	public interface CurrentIndexSupplier
	{
		int getCurrentIndex();
	}
}
