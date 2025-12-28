package net.swedz.tesseract.neoforge.helper.datagen;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.swedz.tesseract.neoforge.api.Assert;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class FontDatagenProvider implements DataProvider
{
	private final PackOutput output;
	private final String     modId, fontName;
	
	private final Map<Character, BitmapCharacterProvider> providers = Maps.newHashMap();
	
	public FontDatagenProvider(PackOutput output, String modId, String fontName)
	{
		this.output = output;
		this.modId = modId;
		this.fontName = fontName;
	}
	
	protected abstract void addCharacters();
	
	public void addBitmap(char character, Identifier file, int height, int ascent)
	{
		Assert.noneNull(character, file);
		file = file.withPath("%s.png"::formatted);
		if(providers.put(character, new BitmapCharacterProvider(file, height, ascent)) != null)
		{
			throw new IllegalStateException("Duplicate character " + character);
		}
	}
	
	public void addBitmap(char character, Identifier file)
	{
		this.addBitmap(character, file, 7, 7);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput)
	{
		this.addCharacters();
		return !providers.isEmpty() ?
				this.save(cachedOutput, output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modId).resolve("font").resolve(fontName + ".json")) :
				CompletableFuture.allOf();
	}
	
	private CompletableFuture<?> save(CachedOutput cachedOutput, Path target)
	{
		var json = new JsonObject();
		var providersArray = new JsonArray();
		for(var entry : providers.entrySet())
		{
			var character = entry.getKey();
			var provider = entry.getValue();
			providersArray.add(provider.toJson(character));
		}
		json.add("providers", providersArray);
		return DataProvider.saveStable(cachedOutput, json, target);
	}
	
	@Override
	public String getName()
	{
		return "Font: " + fontName + " for mod: " + modId;
	}
	
	private static final class BitmapCharacterProvider
	{
		private final Identifier file;
		private final int        height, ascent;
		
		public BitmapCharacterProvider(Identifier file, int height, int ascent)
		{
			this.file = file;
			this.height = height;
			this.ascent = ascent;
		}
		
		public JsonObject toJson(char character)
		{
			var json = new JsonObject();
			json.addProperty("type", "bitmap");
			json.addProperty("file", file.toString());
			json.addProperty("height", height);
			json.addProperty("ascent", ascent);
			var chars = new JsonArray();
			chars.add(String.valueOf(character));
			json.add("chars", chars);
			return json;
		}
	}
}
