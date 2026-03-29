package net.swedz.tesseract.neoforge.compat.mi.hook;

import aztech.modern_industrialization.MI;
import aztech.modern_industrialization.compat.rei.machines.ReiMachineRecipes;
import aztech.modern_industrialization.datagen.model.MachineModelProperties;
import aztech.modern_industrialization.machines.models.MachineCasing;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.swedz.tesseract.neoforge.datagen.mi.client.MachineCasingModelsMIHookDatagenProvider;
import net.swedz.tesseract.neoforge.model.ModelGenerators;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@ApiStatus.Internal
public final class MIHookTracker
{
	private static final Map<Identifier, Identifier>                                                              REI_CATEGORY_IDS      = Maps.newConcurrentMap();
	private static final Map<Identifier, String>                                                                  REI_CATEGORY_NAMES    = Maps.newConcurrentMap();
	private static final Map<Identifier, MachineModelProperties>                                            MACHINE_MODELS        = Maps.newConcurrentMap();
	private static final Map<String, List<BiConsumer<MachineCasingModelsMIHookDatagenProvider, ModelGenerators>>> MACHINE_CASING_MODELS = Maps.newConcurrentMap();
	
	public static void registerRecipeCategoryForMachines(IEventBus bus)
	{
		bus.addListener(
				FMLCommonSetupEvent.class,
				(event) ->
						REI_CATEGORY_IDS.forEach(ReiMachineRecipes::registerRecipeCategoryForMachine)
		);
	}
	
	public static void addReiCategoryId(Identifier machineId, Identifier categoryId)
	{
		REI_CATEGORY_IDS.put(machineId, categoryId);
	}
	
	public static List<Map.Entry<Identifier, String>> getReiCategoryNames(String modId)
	{
		return REI_CATEGORY_NAMES.entrySet().stream()
				.filter((entry) -> entry.getKey().getNamespace().equals(modId))
				.toList();
	}
	
	public static void addReiCategoryName(Identifier categoryId, String englishName)
	{
		REI_CATEGORY_NAMES.put(categoryId, englishName);
	}
	
	public static MachineModelProperties getMachineModel(Identifier id)
	{
		return MACHINE_MODELS.get(id);
	}
	
	private static MachineModelProperties createModelProperties(Identifier id, MachineCasing defaultCasing, String overlay, boolean front, boolean top, boolean side, boolean active, String outputTexture)
	{
		outputTexture = outputTexture == null ? "%s:block/overlays/output".formatted(MI.ID) : outputTexture;
		
		var namespace = id.getNamespace();
		var builder = new MachineModelProperties.Builder(defaultCasing);
		
		if(top)
		{
			builder.addOverlay("top", Identifier.fromNamespaceAndPath(namespace, "block/machines/%s/overlay_top".formatted(overlay)));
			if(active)
			{
				builder.addOverlay("top_active", Identifier.fromNamespaceAndPath(namespace, "block/machines/%s/overlay_top_active".formatted(overlay)));
			}
		}
		if(front)
		{
			builder.addOverlay("front", Identifier.fromNamespaceAndPath(namespace, "block/machines/%s/overlay_front".formatted(overlay)));
			if(active)
			{
				builder.addOverlay("front_active", Identifier.fromNamespaceAndPath(namespace, "block/machines/%s/overlay_front_active".formatted(overlay)));
			}
		}
		if(side)
		{
			builder.addOverlay("side", Identifier.fromNamespaceAndPath(namespace, "block/machines/%s/overlay_side".formatted(overlay)));
			if(active)
			{
				builder.addOverlay("side_active", Identifier.fromNamespaceAndPath(namespace, "block/machines/%s/overlay_side_active".formatted(overlay)));
			}
		}
		
		builder.addOverlay("output", Identifier.parse(outputTexture));
		builder.addOverlay("item_auto", MI.id("block/overlays/item_auto"));
		builder.addOverlay("fluid_auto", MI.id("block/overlays/fluid_auto"));
		
		return builder.build();
	}
	
	public static void addMachineModel(Identifier id, MachineCasing defaultCasing, String overlay, boolean front, boolean top, boolean side, boolean active, String outputTexture)
	{
		MACHINE_MODELS.put(id, createModelProperties(id, defaultCasing, overlay, front, top, side, active, outputTexture));
	}
	
	public static List<BiConsumer<MachineCasingModelsMIHookDatagenProvider, ModelGenerators>> getMachineCasingModels(String modId)
	{
		return MACHINE_CASING_MODELS.computeIfAbsent(modId, (k) -> Lists.newArrayList());
	}
	
	public static void addMachineCasingModel(MIHook hook, BiConsumer<MachineCasingModelsMIHookDatagenProvider, ModelGenerators> action)
	{
		MACHINE_CASING_MODELS.computeIfAbsent(hook.modId(), (k) -> Lists.newArrayList()).add(action);
	}
	
	public record MachineModelPropertiesLEGACY(
			String modId,
			MachineCasing defaultCasing,
			String overlay,
			boolean front,
			boolean top,
			boolean side,
			boolean active,
			String outputTexture
	)
	{
		public MachineModelPropertiesLEGACY
		{
			outputTexture = outputTexture == null ? "%s:block/overlays/output".formatted(MI.ID) : outputTexture;
		}
		
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
			
			defaultOverlays.addProperty("output", outputTexture);
			defaultOverlays.addProperty("item_auto", "%s:block/overlays/item_auto".formatted(MI.ID));
			defaultOverlays.addProperty("fluid_auto", "%s:block/overlays/fluid_auto".formatted(MI.ID));
			
			json.add("default_overlays", defaultOverlays);
		}
	}
}
