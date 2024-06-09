package net.michaeljackson23.mineademia.mixin.client;


import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.items.ItemRegister;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    private BakedModel sharpShooterModel(BakedModel value, ItemStack itemStack, ModelTransformationMode renderMode,
                                        boolean leftHanded, MatrixStack matrices, int light, int overlay) {
        if (itemStack.isOf(ItemRegister.sharpShooter)) {
            return ((ItemRendererAccessor) this).getModels().getModelManager().getModel(new ModelIdentifier(Mineademia.MOD_ID, "sharpshooter_3d", "inventory"));
        }
        return value;
    }

}
