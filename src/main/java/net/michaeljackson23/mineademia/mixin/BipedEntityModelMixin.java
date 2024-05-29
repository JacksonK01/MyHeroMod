package net.michaeljackson23.mineademia.mixin;


import net.michaeljackson23.mineademia.armor.ModelWithBoots;
import net.michaeljackson23.mineademia.armor.deku.gamma.model.GammaSuitModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity>
        extends AnimalModel<T> {
    @Shadow @Final public ModelPart leftLeg;

    @Shadow @Final public ModelPart rightLeg;

    @Inject(at = @At("TAIL"), method = "copyBipedStateTo")
    public void copyBipedStateTo(BipedEntityModel<T> model, CallbackInfo ci) {
        if(model instanceof ModelWithBoots customArmor) {
            customArmor.getLeftBoot().copyTransform(this.leftLeg);
            customArmor.getRightBoot().copyTransform(this.rightLeg);
        }

        if(model instanceof GammaSuitModel<T> gamma) {
            gamma.getHead().pitch = 0;
        }
    }
}
