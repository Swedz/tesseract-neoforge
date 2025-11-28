package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import aztech.modern_industrialization.machines.gui.GuiComponentServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.swedz.tesseract.neoforge.Tesseract;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ModularMultiblockGui implements GuiComponentServer<Unit, ModularMultiblockGui.Data>
{
	public static final Type<Unit, Data> TYPE = new Type<>(Tesseract.id("modular_multiblock"), StreamCodec.unit(Unit.INSTANCE), Data.STREAM_CODEC);
	
	private final int y, height;
	
	private final Consumer<ModularMultiblockGuiContent> contentAdder;
	
	public ModularMultiblockGui(int y, int height, Consumer<ModularMultiblockGuiContent> contentAdder)
	{
		if(height <= 4)
		{
			throw new IllegalArgumentException("Provided height outside of acceptable bounds: must be >4 but %d was provided".formatted(height));
		}
		this.y = y;
		this.height = height;
		this.contentAdder = contentAdder;
	}
	
	public ModularMultiblockGui(int y, int height, Supplier<List<ModularMultiblockGuiLine>> textSupplier)
	{
		this(y, height, (c) -> c.addAll(textSupplier.get()));
	}
	
	public ModularMultiblockGui(int height, Consumer<ModularMultiblockGuiContent> contentAdder)
	{
		this(0, height, contentAdder);
	}
	
	public ModularMultiblockGui(int height, Supplier<List<ModularMultiblockGuiLine>> textSupplier)
	{
		this(0, height, textSupplier);
	}
	
	private ModularMultiblockGuiContent content()
	{
		ModularMultiblockGuiContent content = new ModularMultiblockGuiContent();
		contentAdder.accept(content);
		return content;
	}
	
	@Override
	public Unit getParams()
	{
		return Unit.INSTANCE;
	}
	
	@Override
	public Data extractData()
	{
		return new Data(y, height, this.content());
	}
	
	@Override
	public Type<Unit, Data> getType()
	{
		return TYPE;
	}
	
	public record Data(int y, int height, ModularMultiblockGuiContent content)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT, Data::y,
				ByteBufCodecs.VAR_INT, Data::height,
				ModularMultiblockGuiContent.STREAM_CODEC, Data::content,
				Data::new
		);
	}
	
	public static final int X      = 5;
	public static final int Y      = 16;
	public static final int WIDTH  = 166;
	public static final int HEIGHT = 80;
}
