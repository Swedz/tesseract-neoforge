package net.swedz.tesseract.neoforge.compat.mi.mixin.hack;

import aztech.modern_industrialization.machines.multiblocks.ShapeTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(
		targets = {"aztech.modern_industrialization.compat.viewer.usage.MultiblockCategory$Recipe"},
		remap = false
)
public abstract class RegisterMultiblockShapeReiRecipeHackMixin
{
	@Accessor("materials")
	abstract List<ItemStack> getMaterials();
	
	@Mutable
	@Accessor(value = "controller")
	abstract void setController(ItemStack controller);
	
	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	private void init(String controller, ShapeTemplate shapeTemplate,
					  CallbackInfo callback)
	{
		for(String modId : MIHooks.getModIds())
		{
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, controller);
			ItemStack controllerItem = BuiltInRegistries.ITEM.get(id).getDefaultInstance();
			if(!controllerItem.isEmpty())
			{
				this.setController(controllerItem);
				this.getMaterials().remove(0);
				this.getMaterials().add(0, controllerItem);
				break;
			}
		}
	}
}
