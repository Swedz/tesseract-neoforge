package net.swedz.tesseract.neoforge.compat.mi.guicomponent.recipeefficiency;

import aztech.modern_industrialization.machines.gui.GuiComponentServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.compat.mi.component.craft.ModularCrafterAccess;

public final class ModularRecipeEfficiencyBar implements GuiComponentServer<ModularRecipeEfficiencyBar.Params, ModularRecipeEfficiencyBar.Data>
{
	public static final GuiComponentServer.Type<Params, Data> TYPE = new GuiComponentServer.Type<>(Tesseract.id("modular_recipe_efficiency"), Params.STREAM_CODEC, Data.STREAM_CODEC);
	
	private final Params params;
	private final ModularCrafterAccess crafter;
	
	public ModularRecipeEfficiencyBar(Params params, ModularCrafterAccess crafter)
	{
		this.params = params;
		this.crafter = crafter;
	}
	
	@Override
	public Params getParams()
	{
		return params;
	}
	
	@Override
	public Data extractData()
	{
		if(crafter.hasActiveRecipe())
		{
			return new Data(
					crafter.getEfficiencyTicks(),
					crafter.getMaxEfficiencyTicks(),
					crafter.getCurrentRecipeEu(),
					crafter.getBaseRecipeEu(),
					crafter.getBehavior().getMaxRecipeEu() + crafter.getBehavior().getMaxRecipeEuBonus()
			);
		}
		else
		{
			return new Data();
		}
	}
	
	@Override
	public Type<Params, Data> getType()
	{
		return TYPE;
	}
	
	public record Params(int renderX, int renderY)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, Params> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT, Params::renderX,
				ByteBufCodecs.VAR_INT, Params::renderY,
				Params::new
		);
	}
	
	public record Data(
			boolean hasActiveRecipe, int efficiencyTicks, int maxEfficiencyTicks, long currentRecipeEu,
			long baseRecipeEu, long maxRecipeEu
	)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL, Data::hasActiveRecipe,
				ByteBufCodecs.INT, Data::efficiencyTicks,
				ByteBufCodecs.INT, Data::maxEfficiencyTicks,
				ByteBufCodecs.VAR_LONG, Data::currentRecipeEu,
				ByteBufCodecs.VAR_LONG, Data::baseRecipeEu,
				ByteBufCodecs.VAR_LONG, Data::maxRecipeEu,
				Data::new
		);
		
		public Data(int efficiencyTicks, int maxEfficiencyTicks, long currentRecipeEu, long baseRecipeEu, long maxRecipeEu)
		{
			this(true, efficiencyTicks, maxEfficiencyTicks, currentRecipeEu, baseRecipeEu, maxRecipeEu);
		}
		
		public Data()
		{
			this(false, 0, 0, 0, 0, 0);
		}
	}
}
