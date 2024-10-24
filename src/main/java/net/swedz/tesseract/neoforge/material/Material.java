package net.swedz.tesseract.neoforge.material;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.material.part.MaterialPart;
import net.swedz.tesseract.neoforge.material.part.MaterialPartHolder;
import net.swedz.tesseract.neoforge.material.part.RegisteredMaterialPart;
import net.swedz.tesseract.neoforge.material.property.MaterialProperty;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyHolder;
import net.swedz.tesseract.neoforge.material.property.MaterialPropertyMap;
import net.swedz.tesseract.neoforge.material.recipe.MaterialRecipeGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.swedz.tesseract.neoforge.material.builtin.property.MaterialProperties.*;

public final class Material implements MaterialPropertyHolder.Mutable, MaterialPartHolder.Mutable
{
	private final ResourceLocation id;
	private final String           englishName;
	
	private final MaterialPropertyMap properties = new MaterialPropertyMap();
	
	private final Map<MaterialPart, RegisteredMaterialPart> parts = Maps.newHashMap();
	
	private final List<MaterialRecipeGroup> recipeGroups = Lists.newArrayList();
	
	public Material(ResourceLocation id, String englishName)
	{
		this.id = id;
		this.englishName = englishName;
	}
	
	private Material copy(MaterialFactory factory)
	{
		Material copy = factory.create(this.id, this.englishName);
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
	
	public ResourceLocation id()
	{
		return id;
	}
	
	public String englishName()
	{
		return englishName;
	}
	
	public MaterialPropertyMap properties()
	{
		return properties.copy();
	}
	
	public MaterialPropertyMap properties(MaterialPart part)
	{
		if(!this.has(part))
		{
			throw new IllegalArgumentException("Material does not have this part");
		}
		MaterialPropertyMap properties = this.properties();
		properties.putAll(part.properties());
		return properties;
	}
	
	@Override
	public <T> boolean has(MaterialProperty<T> property)
	{
		return properties.has(property);
	}
	
	@Override
	public <T> Material set(MaterialProperty<T> property, T value)
	{
		Material copy = this.copy();
		copy.properties.set(property, value);
		return copy;
	}
	
	@Override
	public <T> Material setOptional(MaterialProperty<Optional<T>> property, T value)
	{
		Material copy = this.copy();
		copy.properties.setOptional(property, value);
		return copy;
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
	
	@Override
	public Material add(MaterialPart part, RegisteredMaterialPart registered)
	{
		Material copy = this.copy();
		if(copy.parts.put(part, registered) != null)
		{
			throw new IllegalArgumentException("The part '%s' has already been added to the material '%s'".formatted(part.id().toString(), this.id().toString()));
		}
		return copy;
	}
	
	public Material addNative(String modId, MaterialPart part)
	{
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, part.formatId(this));
		RegisteredMaterialPart registered = part.isBlock() ?
				RegisteredMaterialPart.existingBlock(id) :
				RegisteredMaterialPart.existingItem(this.properties(part).get(ITEM_REFERENCE).format(modId, this, part), id);
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
	
	@Override
	public boolean has(MaterialPart part)
	{
		return parts.containsKey(part);
	}
	
	@Override
	public RegisteredMaterialPart get(MaterialPart part)
	{
		RegisteredMaterialPart registered = parts.get(part);
		if(registered == null)
		{
			throw new IllegalArgumentException("No '%s' part registered on material '%s'".formatted(part.id().toString(), this.id().toString()));
		}
		return registered;
	}
	
	public List<MaterialRecipeGroup> recipeGroups()
	{
		return Collections.unmodifiableList(recipeGroups);
	}
	
	public Material recipes(Collection<MaterialRecipeGroup> recipeGroups)
	{
		Material copy = this.copy();
		copy.recipeGroups.addAll(recipeGroups);
		return copy;
	}
	
	public Material recipes(MaterialRecipeGroup... recipeGroups)
	{
		return this.recipes(Arrays.asList(recipeGroups));
	}
}
