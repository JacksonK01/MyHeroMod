package net.michaeljackson23.mineademia.mixin.model;

import com.google.common.collect.Iterables;
import net.michaeljackson23.mineademia.animations.AnimationDataHolder;
import net.michaeljackson23.mineademia.animations.AnimationHelperBiped;
import net.michaeljackson23.mineademia.util.BipedModelMixinAccessor;
import net.michaeljackson23.mineademia.util.IBipedAnimate;
import net.michaeljackson23.mineademia.util.LivingEntityMixinAccessor;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
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

import java.util.Optional;
import java.util.function.Function;


@Mixin(BipedEntityModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> implements BipedModelMixinAccessor, IBipedAnimate {

    @Shadow @Final public ModelPart body;

    @Shadow protected abstract Iterable<ModelPart> getBodyParts();

    @Shadow protected abstract Iterable<ModelPart> getHeadParts();

    @Unique
    private static final Vector3f TEMP = new Vector3f();

    @Unique
    public ModelPart root;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;getChild(Ljava/lang/String;)Lnet/minecraft/client/model/ModelPart;"), method = "<init>(Lnet/minecraft/client/model/ModelPart;Ljava/util/function/Function;)V")
    private void init(ModelPart root, Function<Identifier, RenderLayer> renderLayerFactory, CallbackInfo ci) {
        this.root = root;
    }

    @Inject(at = @At("TAIL"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        animate(livingEntity, f, g, h, i, j);
    }

    @Unique
    public void updateAnimation(AnimationState animationState, Animation animation, float animationProgress, float speedMultiplier) {
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

    @Unique
    public Iterable<ModelPart> getAllParts() {
        Iterable<ModelPart> bodyParts = this.getBodyParts();
        Iterable<ModelPart> headParts = this.getHeadParts();
        return Iterables.concat(headParts, bodyParts);
    }

    @Unique
    public void animate(LivingEntity livingEntity, float f, float g, float h, float i, float j) {
        if (livingEntity instanceof LivingEntityMixinAccessor entity) {

            AnimationState state = entity.getAnimationState();
            AnimationDataHolder animation = entity.getAnimationData();

            if(state == null || animation == null) {
                return;
            }

            if(state.isRunning()) {
                animation.getBodyPartsUsed().forEach(part -> this.getChild(part).ifPresent(ModelPart::resetTransform));
            }

            updateAnimation(state, animation.getAnimation(), h, 1.0f);
        }
    }

    @SuppressWarnings("unchecked")
    @Unique
    private BipedEntityModel<T> getSelf() {
        return (BipedEntityModel<T>) (Object) this;
    }

}
