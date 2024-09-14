package net.swedz.tesseract.neoforge.compat.mi.guicomponent.modularmultiblock;

import aztech.modern_industrialization.machines.gui.GuiComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.Tesseract;

import java.util.List;
import java.util.function.Supplier;

public final class ModularMultiblockGui
{
	public static final ResourceLocation ID = Tesseract.id("modular_multiblock");
	
	public static final class Server implements GuiComponent.Server<Data>
	{
		private final int y, height;
		
		private final Supplier<List<ModularMultiblockGuiLine>> textSupplier;
		
		public Server(int y, int height, Supplier<List<ModularMultiblockGuiLine>> textSupplier)
		{
			if(height <= 4)
			{
				throw new IllegalArgumentException("Provided height outside of acceptable bounds: must be >4 but %d was provided".formatted(height));
			}
			this.y = y;
			this.height = height;
			this.textSupplier = textSupplier;
		}
		
		public Server(int height, Supplier<List<ModularMultiblockGuiLine>> textSupplier)
		{
			this(0, height, textSupplier);
		}
		
		@Override
		public Data copyData()
		{
			return new Data(y, height, textSupplier.get());
		}
		
		@Override
		public boolean needsSync(Data cachedData)
		{
			return !cachedData.equals(this.copyData());
		}
		
		@Override
		public void writeInitialData(RegistryFriendlyByteBuf buf)
		{
			this.writeCurrentData(buf);
		}
		
		@Override
		public void writeCurrentData(RegistryFriendlyByteBuf buf)
		{
			buf.writeInt(y);
			buf.writeInt(height);
			
			List<ModularMultiblockGuiLine> lines = textSupplier.get();
			buf.writeVarInt(lines.size());
			for(ModularMultiblockGuiLine line : lines)
			{
				ModularMultiblockGuiLine.write(buf, line);
			}
		}
		
		@Override
		public ResourceLocation getId()
		{
			return ID;
		}
	}
	
	public record Data(int y, int height, List<ModularMultiblockGuiLine> text)
	{
	}
	
	public static final int X      = 5;
	public static final int Y      = 16;
	public static final int WIDTH  = 166;
	public static final int HEIGHT = 80;
}
