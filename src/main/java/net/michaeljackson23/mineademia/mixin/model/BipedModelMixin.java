package net.michaeljackson23.mineademia.mixin.model;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.animations.AnimationDataHolder;
import net.michaeljackson23.mineademia.animations.AnimationHelperBiped;
import net.michaeljackson23.mineademia.util.BipedModelMixinAccessor;
import net.michaeljackson23.mineademia.util.LivingEntityMixinAccessor;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(BipedEntityModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> implements BipedModelMixinAccessor {
    @Unique
    private static final Vector3f TEMP = new Vector3f();

    @Unique
    public ModelPart root;

    @Unique
    private boolean isDoingAnimation = false;

    @Unique
    public ModelPart jointed_right_arm;
    @Unique
    public ModelPart jointed_right_upper_arm;
    @Unique
    public ModelPart jointed_right_fore_arm;
    @Unique
    public ModelPart joined_left_arm;
    @Unique
    public ModelPart jointed_left_upper_arm;
    @Unique
    public ModelPart jointed_left_fore_arm;
    @Unique
    public ModelPart jointed_right_leg;
    @Unique
    public ModelPart jointed_right_upper_leg;
    @Unique
    public ModelPart jointed_right_lower_leg;
    @Unique
    public ModelPart jointed_left_leg;
    @Unique
    public ModelPart jointed_left_upper_leg;
    @Unique
    public ModelPart jointed_left_lower_leg;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;getChild(Ljava/lang/String;)Lnet/minecraft/client/model/ModelPart;"), method = "<init>(Lnet/minecraft/client/model/ModelPart;Ljava/util/function/Function;)V")
    private void init(ModelPart root, Function<Identifier, RenderLayer> renderLayerFactory, CallbackInfo ci) {
        this.root = root;
        Mineademia.LOGGER.info("Made to init in biped");

//        this.jointed_right_arm = root.getChild("jointed_right_arm");

//        this.jointed_right_upper_arm = root.getChild("jointed_right_upper_arm");
//        this.jointed_right_fore_arm = root.getChild("jointed_right_fore_arm");
//
//        this.joined_left_arm = root.getChild("joined_left_arm");
//
//        this.jointed_left_upper_arm = root.getChild("jointed_left_upper_arm");
//        this.jointed_left_fore_arm = root.getChild("jointed_left_fore_arm");
//
//        this.jointed_right_leg = root.getChild("jointed_right_leg");
//
//        this.jointed_right_upper_leg = root.getChild("jointed_right_upper_leg");
//        this.jointed_right_lower_leg = root.getChild("jointed_right_lower_leg");
//
//        this.jointed_left_leg = root.getChild("jointed_left_leg");
//
//        this.jointed_left_upper_leg = root.getChild("jointed_left_upper_leg");
//        this.jointed_left_lower_leg = root.getChild("jointed_left_lower_leg");
    }

    @Inject(at = @At("TAIL"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        jointed_right_arm.visible = true;
        joined_left_arm.visible = true;
        jointed_right_leg.visible = true;
        jointed_left_leg.visible = true;

        if (livingEntity instanceof LivingEntityMixinAccessor entity) {

            AnimationState state = entity.getAnimationState();
            AnimationDataHolder animation = entity.getAnimationData();

            if(state == null || animation == null) {
                return;
            }

            if(state.isRunning()) {
                animation.getBodyPartsUsed().forEach((part) -> {
                    ModelPart modelPart = this.getChild(part).orElseThrow();
                    modelPart.resetTransform();
                });
            }

            updateAnimation(state, animation.getAnimation(), h, 1.0f);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPartData;addChild(Ljava/lang/String;Lnet/minecraft/client/model/ModelPartBuilder;Lnet/minecraft/client/model/ModelTransform;)Lnet/minecraft/client/model/ModelPartData;"), method = "getModelData", locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void getModelData(Dilation dilation, float pivotOffsetY, CallbackInfoReturnable<ModelData> cir, ModelData modelData, ModelPartData modelPartData) {
        Mineademia.LOGGER.info("Made to getModelData");

        ModelPartData jointed_right_arm = modelPartData.addChild("jointed_right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData jointed_right_upper_arm = jointed_right_arm.addChild("jointed_right_upper_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-8.0F, 4.01F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_fore_arm = jointed_right_arm.addChild("jointed_right_fore_arm", ModelPartBuilder.create().uv(40, 22).cuboid(-3.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-3.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(-3.0F, 7.01F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData joined_left_arm = modelPartData.addChild("joined_left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData jointed_left_upper_arm = joined_left_arm.addChild("jointed_left_upper_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 4.01F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_fore_arm = joined_left_arm.addChild("jointed_left_fore_arm", ModelPartBuilder.create().uv(32, 54).cuboid(5.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(5.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(32, 48).cuboid(5.0F, 7.01F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.0F, 3.0F, 0.0F));

        ModelPartData jointed_right_leg = modelPartData.addChild("jointed_right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData jointed_right_upper_leg = jointed_right_leg.addChild("jointed_right_upper_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(-4, 24).cuboid(-2.0F, 6.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_lower_leg = jointed_right_leg.addChild("jointed_right_lower_leg", ModelPartBuilder.create().uv(0, 22).cuboid(-2.0F, 0.99F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(-4, 24).cuboid(-2.0F, 6.99F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.0F));

        ModelPartData jointed_left_leg = modelPartData.addChild("jointed_left_leg", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        ModelPartData jointed_left_upper_leg = jointed_left_leg.addChild("jointed_left_upper_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 56).cuboid(-2.0F, 6.01F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_lower_leg = jointed_left_leg.addChild("jointed_left_lower_leg", ModelPartBuilder.create().uv(16, 54).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 56).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 48).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.0F));
    }


    @Unique
    private void updateAnimation(AnimationState animationState, Animation animation, float animationProgress, float speedMultiplier) {
        BipedEntityModel<T> model = getSelf();
        animationState.update(animationProgress, speedMultiplier);
        animationState.run(state -> AnimationHelperBiped.animate(model, animation, state.getTimeRunning(), 1.0f, TEMP));
    }

    @Unique
    public Optional<ModelPart> getChild(String name) {
        if (name.equals("root")) {
            return Optional.of(getPart());
        }
        return root.traverse().filter(part -> part.hasChild(name)).findFirst().map(part -> part.getChild(name));
    }

    @Unique
    public ModelPart getPart() {
        return root;
    }

    @SuppressWarnings("unchecked")
    @Unique
    private BipedEntityModel<T> getSelf() {
        return (BipedEntityModel<T>) (Object) this;
    }
}
