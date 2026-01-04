package net.swedz.tesseract.neoforge.helper.datagen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.font.SpaceProvider;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontOption;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
import net.minecraft.client.gui.font.providers.ProviderReferenceDefinition;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.swedz.tesseract.neoforge.api.Assert;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class FontDatagenProvider implements DataProvider
{
	private final PackOutput         output;
	private final ExistingFileHelper existingFileHelper;
	private final String             modId, fontName;
	
	private final List<GlyphProviderDefinition.Conditional> providers = Lists.newArrayList();
	
	private final Set<Character> bitmapCharacters = Sets.newHashSet();
	
	public FontDatagenProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modId, String fontName)
	{
		this.output = output;
		this.existingFileHelper = existingFileHelper;
		this.modId = modId;
		this.fontName = fontName;
	}
	
	protected abstract void addProviders();
	
	private static int[][] bitmapToCodepointMap(List<String> characters)
	{
		int rows = characters.size();
		int[][] codepoints = new int[rows][];
		for(int index = 0; index < rows; index++)
		{
			codepoints[index] = characters.get(index).codePoints().toArray();
		}
		return codepoints;
	}
	
	public void addBitmap(List<String> characters, ResourceLocation file, int height, int ascent, FontOption.Filter condition)
	{
		Assert.noneNull(characters, file);
		
		file = file.withPath("%s.png"::formatted);
		Assert.that(existingFileHelper.exists(file.withPrefix("textures/"), PackType.CLIENT_RESOURCES), "Texture %s does not exist in any known resource pack".formatted(file));
		
		for(var characterRow : characters)
		{
			for(var character : characterRow.toCharArray())
			{
				if(!bitmapCharacters.add(character))
				{
					throw new IllegalStateException("Duplicate character " + character);
				}
			}
		}
		
		var definition = new BitmapProvider.Definition(file, height, ascent, bitmapToCodepointMap(characters));
		this.add(definition, condition);
	}
	
	public void addBitmap(List<String> characters, ResourceLocation file, int height, int ascent)
	{
		this.addBitmap(characters, file, height, ascent, null);
	}
	
	public void addBitmap(char character, ResourceLocation file, int height, int ascent, FontOption.Filter condition)
	{
		this.addBitmap(List.of(String.valueOf(character)), file, height, ascent, condition);
	}
	
	public void addBitmap(char character, ResourceLocation file, int height, int ascent)
	{
		this.addBitmap(character, file, height, ascent, null);
	}
	
	public void addBitmap(char character, ResourceLocation file, FontOption.Filter condition)
	{
		this.addBitmap(character, file, 7, 7, condition);
	}
	
	public void addBitmap(char character, ResourceLocation file)
	{
		this.addBitmap(character, file, null);
	}
	
	public void addReference(ResourceLocation id, FontOption.Filter condition)
	{
		this.add(new ProviderReferenceDefinition(id), condition);
	}
	
	public void addReference(ResourceLocation id)
	{
		this.add(new ProviderReferenceDefinition(id), null);
	}
	
	private static Map<Integer, Float> spaceToCodepointMap(Map<Character, Float> advances)
	{
		Map<Integer, Float> codepoints = Maps.newHashMap();
		for(var entry : advances.entrySet())
		{
			var character = entry.getKey();
			var advance = entry.getValue();
			codepoints.put(String.valueOf(character).codePointAt(0), advance);
		}
		return codepoints;
	}
	
	public void addSpace(Map<Character, Float> advances, FontOption.Filter condition)
	{
		this.add(new SpaceProvider.Definition(spaceToCodepointMap(advances)), condition);
	}
	
	public void addSpace(Map<Character, Float> advances)
	{
		this.addSpace(advances, null);
	}
	
	public void add(GlyphProviderDefinition provider, FontOption.Filter condition)
	{
		providers.add(new GlyphProviderDefinition.Conditional(provider, condition == null ? FontOption.Filter.ALWAYS_PASS : condition));
	}
	
	public void add(GlyphProviderDefinition provider)
	{
		this.add(provider, null);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput)
	{
		this.addProviders();
		return !bitmapCharacters.isEmpty() ?
				this.save(cachedOutput, output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modId).resolve("font").resolve(fontName + ".json")) :
				CompletableFuture.allOf();
	}
	
	private CompletableFuture<?> save(CachedOutput cachedOutput, Path target)
	{
		var file = new FontManager.FontDefinitionFile(Collections.unmodifiableList(providers));
		var json = FontManager.FontDefinitionFile.CODEC.encodeStart(JsonOps.INSTANCE, file).getOrThrow();
		return DataProvider.saveStable(cachedOutput, json, target);
	}
	
	@Override
	public String getName()
	{
		return "Font: " + fontName + " for mod: " + modId;
	}
}
