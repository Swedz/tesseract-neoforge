package net.swedz.tesseract.neoforge.compat.mi.guicomponent.configurationpanel;

import aztech.modern_industrialization.machines.gui.GuiComponentServer;
import com.google.common.base.Preconditions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.swedz.tesseract.neoforge.Tesseract;

import java.util.List;
import java.util.stream.IntStream;

/**
 * This was stolen from {@link aztech.modern_industrialization.machines.guicomponents.ShapeSelection} to make my own generic "configuration panel" component to be used for non-shape related configuring of machines.
 */
public class ConfigurationPanel implements GuiComponentServer<ConfigurationPanel.Params, ConfigurationPanel.Data>
{
	public static final Type<Params, Data> TYPE = new Type<>(Tesseract.id("configuration_panel"), Params.STREAM_CODEC, Data.STREAM_CODEC);
	
	public interface Behavior
	{
		void handleClick(int clickedLine, int delta);
		
		int getCurrentIndex(int line);
	}
	
	public record LineInfo(List<Component> translations, boolean useArrows)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, LineInfo> STREAM_CODEC = StreamCodec.composite(
				ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list()), LineInfo::translations,
				ByteBufCodecs.BOOL, LineInfo::useArrows,
				LineInfo::new
		);
		
		public int numValues()
		{
			return translations.size();
		}
	}
	
	public final Behavior behavior;
	
	private final Component title;
	private final Component description;
	private final List<LineInfo> lines;
	
	public ConfigurationPanel(Behavior behavior, Component title, Component description, LineInfo... lines)
	{
		Preconditions.checkArgument(lines.length > 0);
		
		this.behavior = behavior;
		
		this.title = title;
		this.description = description;
		this.lines = List.of(lines);
	}
	
	@Override
	public Params getParams()
	{
		return new Params(title, description, lines);
	}
	
	@Override
	public Data extractData()
	{
		return new Data(IntStream.range(0, lines.size()).map(behavior::getCurrentIndex).boxed().toList());
	}
	
	@Override
	public Type<Params, Data> getType()
	{
		return TYPE;
	}
	
	public record Params(
			Component title,
			Component description,
			List<LineInfo> lines
	)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, Params> STREAM_CODEC = StreamCodec.composite(
				ComponentSerialization.STREAM_CODEC, Params::title,
				ComponentSerialization.STREAM_CODEC, Params::description,
				LineInfo.STREAM_CODEC.apply(ByteBufCodecs.list()), Params::lines,
				Params::new
		);
	}
	
	public record Data(List<Integer> selectedIndexes)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT.apply(ByteBufCodecs.list()), Data::selectedIndexes,
				Data::new
		);
	}
}
