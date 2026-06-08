package net.swedz.tesseract.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.swedz.tesseract.neoforge.gui.tinteditem.TintedItemPictureInPictureRenderState;
import net.swedz.tesseract.neoforge.gui.tinteditem.TintedItemPictureInPictureRenderer;
import net.swedz.tesseract.neoforge.tooltip.TooltipHandler;
import net.swedz.tesseract.neoforge.tooltip.component.ItemStackClientTooltipComponent;
import net.swedz.tesseract.neoforge.tooltip.component.ItemStackTooltipComponent;

@Mod(value = Tesseract.ID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = Tesseract.ID)
public final class TesseractClient
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void attachTooltips(ItemTooltipEvent event)
	{
		TooltipHandler.attach(event.getFlags(), event.getContext(), event.getItemStack(), event.getToolTip());
	}
	
	@SubscribeEvent
	private static void registerClientTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event)
	{
		event.register(ItemStackTooltipComponent.class, ItemStackClientTooltipComponent::new);
	}
	
	// TODO 1.21.11
	/*@SubscribeEvent
	private static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event)
	{
		event.register(DynamicAtlasUnbakedModel.LOADER_ID, DynamicAtlasUnbakedModel.LOADER);
	}*/
	
	@SubscribeEvent
	private static void registerPictureInPictureRenderers(RegisterPictureInPictureRenderersEvent event)
	{
		event.register(TintedItemPictureInPictureRenderState.class, TintedItemPictureInPictureRenderer::new);
	}
}
