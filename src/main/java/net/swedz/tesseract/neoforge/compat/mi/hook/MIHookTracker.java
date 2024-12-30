package net.swedz.tesseract.neoforge.compat.mi.hook;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.machines.models.MachineCasing;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.datagen.mi.client.MachineCasingModelsMIHookDatagenProvider;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class MIHookTracker
{
	private static final Map<ResourceLocation, String>                                         REI_CATEGORY_NAMES    = Maps.newHashMap();
	private static final Map<ResourceLocation, MachineModelProperties>                         MACHINE_MODELS        = Maps.newHashMap();
	private static final Map<String, List<Consumer<MachineCasingModelsMIHookDatagenProvider>>> MACHINE_CASING_MODELS = Maps.newHashMap();
	
	public static List<Map.Entry<ResourceLocation, String>> getReiCategoryNames(String modId)
	{
		return REI_CATEGORY_NAMES.entrySet().stream()
				.filter((entry) -> entry.getKey().getNamespace().equals(modId))
				.toList();
	}
	
	public static void addReiCategoryName(ResourceLocation categoryId, String englishName)
	{
		REI_CATEGORY_NAMES.put(categoryId, englishName);
	}
	
	public static MachineModelProperties getMachineModel(ResourceLocation id)
	{
		return MACHINE_MODELS.get(id);
	}
	
	public static void addMachineModel(ResourceLocation id, MachineCasing defaultCasing, String overlay, boolean front, boolean top, boolean side, boolean active)
	{
		MACHINE_MODELS.put(id, new MachineModelProperties(id.getNamespace(), defaultCasing, overlay, front, top, side, active));
	}
	
	public static List<Consumer<MachineCasingModelsMIHookDatagenProvider>> getMachineCasingModels(String modId)
	{
		return MACHINE_CASING_MODELS.computeIfAbsent(modId, (k) -> Lists.newArrayList());
	}
	
	public static void addMachineCasingModel(MIHook hook, Consumer<MachineCasingModelsMIHookDatagenProvider> action)
	{
		MACHINE_CASING_MODELS.computeIfAbsent(hook.modId(), (k) -> Lists.newArrayList()).add(action);
	}
	
	public record MachineModelProperties(
			String modId,
			MachineCasing defaultCasing,
			String overlay, boolean front, boolean top, boolean side,
			boolean active
	)
	{
		public void addToMachineJson(JsonObject json)
		{
			json.addProperty("casing", defaultCasing.key.getNamespace().equals(MI.ID) ? defaultCasing.key.getPath() : defaultCasing.key.toString());
			
			var defaultOverlays = new JsonObject();
			
			if(top)
			{
				defaultOverlays.addProperty("top", "%s:block/machines/%s/overlay_top".formatted(modId, overlay));
				if(active)
				{
					defaultOverlays.addProperty("top_active", "%s:block/machines/%s/overlay_top_active".formatted(modId, overlay));
				}
			}
			if(front)
			{
				defaultOverlays.addProperty("front", "%s:block/machines/%s/overlay_front".formatted(modId, overlay));
				if(active)
				{
					defaultOverlays.addProperty(
							"front_active",
							"%s:block/machines/%s/overlay_front_active".formatted(modId, overlay)
					);
				}
			}
			if(side)
			{
				defaultOverlays.addProperty("side", "%s:block/machines/%s/overlay_side".formatted(modId, overlay));
				if(active)
				{
					defaultOverlays.addProperty("side_active", "%s:block/machines/%s/overlay_side_active".formatted(modId, overlay));
				}
			}
			
			defaultOverlays.addProperty("output", "%s:block/overlays/output".formatted(MI.ID));
			defaultOverlays.addProperty("item_auto", "%s:block/overlays/item_auto".formatted(MI.ID));
			defaultOverlays.addProperty("fluid_auto", "%s:block/overlays/fluid_auto".formatted(MI.ID));
			
			json.add("default_overlays", defaultOverlays);
		}
	}
}
