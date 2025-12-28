package net.swedz.tesseract.neoforge.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.SimpleUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper;
import net.swedz.tesseract.neoforge.Tesseract;
import net.swedz.tesseract.neoforge.helper.model.ModelHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>This fundamentally works similarly as a vanilla {@link BlockModel}, however it allows a custom atlas to be used
 * instead of {@link InventoryMenu#BLOCK_ATLAS}.</p>
 *
 * <p>Code taken from {@link net.neoforged.neoforge.client.model.ElementsModel} and modified to meet my needs.</p>
 */
public final class DynamicAtlasUnbakedModel extends SimpleUnbakedGeometry<DynamicAtlasUnbakedModel>
{
	public static final ResourceLocation                          LOADER_ID = Tesseract.id("dynamic_atlas");
	public static final IGeometryLoader<DynamicAtlasUnbakedModel> LOADER    = (json, context) ->
	{
		if(!json.has("atlas"))
		{
			throw new JsonParseException("A dynamic atlas model must have an \"atlas\" property.");
		}
		ResourceLocation atlas = ResourceLocation.tryParse(json.get("atlas").getAsString());
		if(atlas == null)
		{
			throw new JsonParseException("A dynamic atlas model must have a valid \"atlas\" property.");
		}
		
		if(!json.has("textures"))
		{
			throw new JsonParseException("A dynamic atlas model must have a \"textures\" property.");
		}
		Map<String, Material> textures = ModelHelper.gatherTextures(atlas, json, "textures");
		
		if(!json.has("elements"))
		{
			throw new JsonParseException("A dynamic atlas model must have an \"elements\" member.");
		}
		List<BlockElement> elements = new ArrayList<>();
		for(JsonElement element : GsonHelper.getAsJsonArray(json, "elements"))
		{
			elements.add(context.deserialize(element, BlockElement.class));
		}
		
		return new DynamicAtlasUnbakedModel(textures, elements);
	};
	
	private final Map<String, Material> textures;
	private final List<BlockElement>    elements;
	
	private DynamicAtlasUnbakedModel(Map<String, Material> textures, List<BlockElement> elements)
	{
		this.textures = textures;
		this.elements = elements;
	}
	
	@Override
	protected void addQuads(IGeometryBakingContext context, IModelBuilder<?> modelBuilder, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState)
	{
		// If there is a root transform, undo the ModelState transform, apply it, then re-apply the ModelState transform.
		// This is necessary because of things like UV locking, which should only respond to the ModelState, and as such
		// that is the only transform that should be applied during face bake.
		var rootTransform = context.getRootTransform();
		if(!rootTransform.isIdentity())
		{
			modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, rootTransform);
		}
		
		for(BlockElement element : elements)
		{
			for(Direction direction : element.faces.keySet())
			{
				var face = element.faces.get(direction);
				var texture = face.texture();
				if(texture.charAt(0) == '#')
				{
					texture = texture.substring(1);
				}
				var sprite = spriteGetter.apply(textures.getOrDefault(texture, new Material(InventoryMenu.BLOCK_ATLAS, MissingTextureAtlasSprite.getLocation())));
				var quad = BlockModel.bakeFace(element, face, sprite, direction, modelState);
				
				if(face.cullForDirection() == null)
				{
					modelBuilder.addUnculledFace(quad);
				}
				else
				{
					modelBuilder.addCulledFace(modelState.getRotation().rotateTransform(face.cullForDirection()), quad);
				}
			}
		}
	}
}
