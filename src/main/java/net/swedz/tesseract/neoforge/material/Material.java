package net.swedz.tesseract.neoforge.material;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.material.part.recipe.MaterialRecipeGroup;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class Material implements MaterialPropertyHolder
{
	protected final ResourceLocation id;
	protected final String           englishName;
	
	protected final MaterialPropertyMap properties = new MaterialPropertyMap();
	
	protected final Map<MaterialPart, RegisteredMaterialPart> parts = Maps.newHashMap();
	
	protected final List<MaterialRecipeGroup> recipeGroups = Lists.newArrayList();
	
	public Material(ResourceLocation id, String englishName)
	{
		this.id = id;
		this.englishName = englishName;
	}
	
	protected <M extends Material> M copy(BiFunction<ResourceLocation, String, M> creator)
	{
		M copy = creator.apply(this.id, this.englishName);
		copy.properties.putAll(this.properties);
		copy.parts.putAll(this.parts);
		copy.recipeGroups.addAll(this.recipeGroups);
		return copy;
	}
	
	public Material copy()
	{
		return this.copy(Material::new);
	}
	
	public Material as(String namespace)
	{
		return this.copy((id, name) -> new Material(ResourceLocation.fromNamespaceAndPath(namespace, id.getPath()), name));
	}
	
	public ImmutableMaterial immutable()
	{
		return this.copy(ImmutableMaterial::new);
	}
	
	public ResourceLocation id()
	{
		return id;
	}
	
	public String englishName()
	{
		return englishName;
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return properties.has(property);
	}
	
	@Override
	public <T> Material set(MaterialProperty<T> property, T value)
	{
		properties.set(property, value);
		return this;
	}
	
	@Override
	public <T> Material setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		properties.setOptional(property, value);
		return this;
	}
	
	@Override
	public <T> T get(MaterialProperty<T> property)
	{
		return properties.get(property);
	}
	
	public Map<MaterialPart, RegisteredMaterialPart> parts()
	{
		return Collections.unmodifiableMap(parts);
	}
	
	public Material add(MaterialPart part, RegisteredMaterialPart registered)
	{
		if(parts.put(part, registered) != null)
		{
			throw new IllegalArgumentException("The part '%s' has already been added to the material '%s'".formatted(part.id().toString(), this.id().toString()));
		}
		return this;
	}
	
	public Material addNative(String modId, MaterialPart part)
	{
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, part.formatId(this));
		RegisteredMaterialPart registered = part.isBlock() ? RegisteredMaterialPart.existingBlock(id) : RegisteredMaterialPart.existingItem(id);
		return this.add(part, registered);
	}
	
	public Material addNative(MaterialPart part)
	{
		return this.addNative(this.id().getNamespace(), part);
	}
	
	public Material addNative(String modId, MaterialPart... parts)
	{
		for(MaterialPart part : parts)
		{
			this.addNative(modId, part);
		}
		return this;
	}
	
	public Material addNative(MaterialPart... parts)
	{
		return this.addNative(this.id().getNamespace(), parts);
	}
	
	public boolean has(MaterialPart part)
	{
		return parts.containsKey(part);
	}
	
	public RegisteredMaterialPart get(MaterialPart part)
	{
		RegisteredMaterialPart registered = parts.get(part);
		if(registered == null)
		{
			throw new IllegalArgumentException("No '%s' part registered on material '%s'".formatted(part.id().toString(), this.id().toString()));
		}
		return registered;
	}
	
	public List<MaterialRecipeGroup> recipes()
	{
		return Collections.unmodifiableList(recipeGroups);
	}
	
	public Material recipes(Collection<MaterialRecipeGroup> recipeGroups)
	{
		this.recipeGroups.addAll(recipeGroups);
		return this;
	}
	
	public Material recipes(MaterialRecipeGroup... recipeGroups)
	{
		return this.recipes(Arrays.asList(recipeGroups));
	}
}
