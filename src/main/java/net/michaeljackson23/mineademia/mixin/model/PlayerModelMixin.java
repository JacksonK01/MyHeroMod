package net.michaeljackson23.mineademia.mixin.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.michaeljackson23.mineademia.animations.AnimationDataHolder;
import net.michaeljackson23.mineademia.util.BipedModelMixinAccessor;
import net.michaeljackson23.mineademia.util.EntityAnimatedPartNames;
import net.michaeljackson23.mineademia.util.IBipedAnimate;
import net.michaeljackson23.mineademia.util.LivingEntityMixinAccessor;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.Set;

import static net.michaeljackson23.mineademia.util.EntityAnimatedPartNames.*;


//TODO add support for the player's overlay on their skin.
//TODO the head gets weird for certain animations
@SuppressWarnings("unchecked")
@Mixin(PlayerEntityModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> {

    @Unique
    public ModelPart animatedRoot;
    @Unique
    public ModelPart animatedHead;
    @Unique
    public ModelPart animatedBody;
    @Unique
    public ModelPart jointed_right_arm;
    @Unique
    public ModelPart jointed_right_upper_arm;
    @Unique
    public ModelPart jointed_right_fore_arm;
    @Unique
    public ModelPart jointed_left_arm;
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

    @Unique
    private final static float palmSize = 0.001f;

    @Unique
    private final static float yPalmPos = 7.0001f;

    @Unique
    private final static float footSize = 0.001f;

    @Unique
    private final static float yFootPos = 7.0001f;

    @Unique
    private final static float elbowSize = 0.001f;

    @Unique
    private final static float kneeSize = 0.001f;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;getChild(Ljava/lang/String;)Lnet/minecraft/client/model/ModelPart;"), method = "<init>")
    private void init(ModelPart root, boolean thinArms, CallbackInfo ci) {

        this.animatedRoot = root.getChild(ANIMATED_ROOT);

        this.animatedHead = animatedRoot.getChild(ANIMATED_HEAD);

        this.animatedBody = animatedRoot.getChild(ANIMATED_BODY);

        this.jointed_right_arm = animatedRoot.getChild(JOINTED_RIGHT_ARM);

        this.jointed_right_upper_arm = jointed_right_arm.getChild(JOINTED_RIGHT_UPPER_ARM);
        this.jointed_right_fore_arm = jointed_right_arm.getChild(JOINTED_RIGHT_FORE_ARM);

        this.jointed_left_arm = animatedRoot.getChild(JOINTED_LEFT_ARM);

        this.jointed_left_upper_arm = jointed_left_arm.getChild(JOINTED_LEFT_UPPER_ARM);
        this.jointed_left_fore_arm = jointed_left_arm.getChild("jointed_left_fore_arm");

        this.jointed_right_leg = animatedRoot.getChild("jointed_right_leg");

        this.jointed_right_upper_leg = jointed_right_leg.getChild("jointed_right_upper_leg");
        this.jointed_right_lower_leg = jointed_right_leg.getChild("jointed_right_lower_leg");

        this.jointed_left_leg = animatedRoot.getChild("jointed_left_leg");

        this.jointed_left_upper_leg = jointed_left_leg.getChild("jointed_left_upper_leg");
        this.jointed_left_lower_leg = jointed_left_leg.getChild("jointed_left_lower_leg");

    }

    @Inject(at = @At("TAIL"), method = "getTexturedModelData", locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void getTexturedModelData(Dilation dilation, boolean slim, CallbackInfoReturnable<ModelData> cir, ModelData modelData, ModelPartData modelPartData) {
        ModelPartData animatedRoot = modelPartData.addChild(ANIMATED_ROOT, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = animatedRoot.addChild(ANIMATED_HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData body = animatedRoot.addChild(ANIMATED_BODY, ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        if(slim) {
            addSlimArms(animatedRoot);
        } else {
            addWideArms(animatedRoot);
        }

        ModelPartData jointed_right_leg = animatedRoot.addChild("jointed_right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, -12.0F, 0.0F));

        ModelPartData jointed_right_upper_leg = jointed_right_leg.addChild("jointed_right_upper_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(-4, 24).cuboid(-2.0F, 6.0F, -2.0F, 4.0F, kneeSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_lower_leg = jointed_right_leg.addChild("jointed_right_lower_leg", ModelPartBuilder.create().uv(0, 22).cuboid(-2.0F, 0.99F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(-4, 24).cuboid(-2.0F, 6.99F, -2.0F, 4.0F, kneeSize, 4.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-2.0F, yFootPos, -2.0F, 4.0F, footSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.0F));

        ModelPartData jointed_left_leg = animatedRoot.addChild("jointed_left_leg", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, -12.0F, 0.0F));

        ModelPartData jointed_left_upper_leg = jointed_left_leg.addChild("jointed_left_upper_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 56).cuboid(-2.0F, 6.01F, -2.0F, 4.0F, kneeSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_lower_leg = jointed_left_leg.addChild("jointed_left_lower_leg", ModelPartBuilder.create().uv(16, 54).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 56).cuboid(-2.0F, 1.0F, -2.0F, 4.0F, kneeSize, 4.0F, new Dilation(0.0F))
                .uv(16, 48).cuboid(-2.0F, yFootPos, -2.0F, 4.0F, footSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 0.0F));
    }

    @Unique
    private static void addWideArms(ModelPartData modelPartData) {
        ModelPartData jointed_right_arm = modelPartData.addChild("jointed_right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, -22.0F, 0.0F));

        ModelPartData jointed_right_upper_arm = jointed_right_arm.addChild("jointed_right_upper_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-8.0F, 4.01F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_fore_arm = jointed_right_arm.addChild("jointed_right_fore_arm", ModelPartBuilder.create().uv(40, 22).cuboid(-3.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-3.0F, 1.0F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(-3.0F, yPalmPos, -2.0F, 4.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData jointed_left_arm = modelPartData.addChild("jointed_left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, -22.0F, 0.0F));

        ModelPartData jointed_left_upper_arm = jointed_left_arm.addChild("jointed_left_upper_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 4.01F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_fore_arm = jointed_left_arm.addChild("jointed_left_fore_arm", ModelPartBuilder.create().uv(32, 54).cuboid(-1.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 1.0F, -2.0F, 4.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData cube_r1 = jointed_left_fore_arm.addChild("cube_r1", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, yPalmPos, -2.0F, 4.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }

    @Unique
    private static void addSlimArms(ModelPartData modelPartData) {
        ModelPartData jointed_right_arm = modelPartData.addChild("jointed_right_arm", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, -22.0F, 0.0F));

        ModelPartData jointed_right_upper_arm = jointed_right_arm.addChild("jointed_right_upper_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-7.0F, -2.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-7.0F, 4.01F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 0.0F, 0.0F));

        ModelPartData jointed_right_fore_arm = jointed_right_arm.addChild("jointed_right_fore_arm", ModelPartBuilder.create().uv(40, 22).cuboid(-2.0F, 1.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 24).cuboid(-2.0F, 1.0F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(-2.0F, yPalmPos, -2.0F, 3.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData jointed_left_arm = modelPartData.addChild("jointed_left_arm", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, -22.0F, 0.0F));

        ModelPartData jointed_left_upper_arm = jointed_left_arm.addChild("jointed_left_upper_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 4.01F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jointed_left_fore_arm = jointed_left_arm.addChild("jointed_left_fore_arm", ModelPartBuilder.create().uv(32, 54).cuboid(-1.0F, 1.0F, -2.0F, 3.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 56).cuboid(-1.0F, 1.0F, -2.0F, 3.0F, elbowSize, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, 0.0F));

        ModelPartData cube_r1 = jointed_left_fore_arm.addChild("cube_r1", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, yPalmPos, -2.0F, 3.0F, palmSize, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }

    @Inject(at = @At("TAIL"), method = "getBodyParts", cancellable = true)
    private void getBodyParts(CallbackInfoReturnable<Iterable<ModelPart>> cir) {
        cir.setReturnValue(Iterables.concat(cir.getReturnValue(), ImmutableList.of(this.animatedRoot)));
    }

    //HAS TO BE AT HEAD
    @Inject(at = @At("HEAD"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        copyAllTransform();
        switchModels((LivingEntityMixinAccessor) livingEntity);
    }

    @Unique
    private void copyAllTransform() {
        PlayerEntityModel<T> self = getSelf();

        if (!(self instanceof BipedModelMixinAccessor selfMixin)) {
            return;
        }

        animatedRoot.copyTransform(selfMixin.getPart());
        animatedHead.copyTransform(self.head);
        animatedBody.copyTransform(self.body);
        jointed_right_arm.copyTransform(self.rightArm);
        jointed_left_arm.copyTransform(self.leftArm);
        jointed_right_leg.copyTransform(self.rightLeg);
        jointed_left_leg.copyTransform(self.leftLeg);

        resetLowerHalfOfLimbs();
    }

    @Unique
    private void switchModels(LivingEntityMixinAccessor livingEntityMixin) {
        if(!(getSelf() instanceof BipedModelMixinAccessor selfMixin)) {
            return;
        }

        boolean bl = livingEntityMixin.getAnimationState().isRunning() && livingEntityMixin.getAnimationData().isJointedAnimation();

        selfMixin.getAllParts().forEach(part -> {
            part.visible = !bl;
        });

        animatedRoot.visible = bl;

        //This has to be like this for some reason ? ???
        if(!livingEntityMixin.getAnimationData().doesContainRoot()) {
            animatedRoot.resetTransform();
        }
    }

    @Unique
    private void resetTransformIfBonePresent(Set<String> bones, String boneName, ModelPart toSwap, ModelPart motherPart, boolean isAnimationRunning) {
        boolean bl = bones.contains(boneName) && isAnimationRunning;

        toSwap.visible = !bl;
        motherPart.visible = bl;

        if (bl) {
            motherPart.copyTransform(toSwap);
            motherPart.traverse().forEach(ModelPart::resetTransform);
        }
    }

    @Unique
    private void resetLowerHalfOfLimbs() {
        this.jointed_right_fore_arm.resetTransform();

        this.jointed_left_fore_arm.resetTransform();

        this.jointed_right_lower_leg.resetTransform();

        this.jointed_left_lower_leg.resetTransform();
    }

    @Unique
    private void resetRootTransformIfPresent(Set<String> bones, boolean isAnimationRunning) {
        if(!(getSelf() instanceof BipedModelMixinAccessor selfMixin)) {
            return;
        }

        selfMixin.getAllParts().forEach(part -> {
            part.visible = false;
        });

        animatedRoot.visible = true;

        if(isAnimationRunning)
            animatedRoot.traverse().forEach(ModelPart::resetTransform);
    }

    @Unique
    private PlayerEntityModel<T> getSelf() {
        return (PlayerEntityModel<T>) (Object) this;
    }

}
