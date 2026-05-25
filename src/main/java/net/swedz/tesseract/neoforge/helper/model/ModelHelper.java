package net.swedz.tesseract.neoforge.helper.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.swedz.tesseract.api.Assert;

import java.util.LinkedHashMap;
import java.util.List;

// TODO what to do about atlases?
public final class ModelHelper
{
	public static LinkedHashMap<String, Material> gatherTextures(Identifier atlas, JsonObject json, String name)
	{
		Assert.notNull(name);
		if(json == null || !json.has(name))
		{
			return Maps.newLinkedHashMap();
		}
		LinkedHashMap<String, Material> textures = Maps.newLinkedHashMap();
		for(var entry : json.getAsJsonObject(name).entrySet())
		{
			textures.put(entry.getKey(), new Material(Identifier.parse(entry.getValue().getAsString())));
		}
		return textures;
	}
	
	public static LinkedHashMap<String, Material> gatherTextures(JsonObject json, String name)
	{
		return gatherTextures(AtlasIds.BLOCKS, json, name);
	}
	
	public static List<Material> gatherLayerTextures(Identifier atlas, JsonObject json, String name)
	{
		Assert.notNull(name);
		if(json == null || !json.has(name))
		{
			return List.of();
		}
		json = json.getAsJsonObject(name);
		ImmutableList.Builder<Material> builder = ImmutableList.builder();
		for(int index = 0; json.has("layer" + index); index++)
		{
			builder.add(new Material(Identifier.parse(json.get("layer" + index).getAsString())));
		}
		return builder.build();
	}
	
	public static List<Material> gatherLayerTextures(JsonObject json, String name)
	{
		return gatherLayerTextures(AtlasIds.BLOCKS, json, name);
	}
	
	public static List<Material> gatherTextures(TextureSlots textureSlots)
	{
		Assert.notNull(textureSlots);
		ImmutableList.Builder<Material> builder = ImmutableList.builder();
		for(int index = 0; textureSlots.getMaterial("layer" + index) != null; index++)
		{
			builder.add(textureSlots.getMaterial("layer" + index));
		}
		return builder.build();
	}
}
