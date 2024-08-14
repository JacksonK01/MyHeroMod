package net.michaeljackson23.mineademia.mixin.model;

import net.michaeljackson23.mineademia.animations.AnimationDataHolder;
import net.michaeljackson23.mineademia.animations.AnimationHelperBiped;
import net.michaeljackson23.mineademia.util.BipedModelMixinAccessor;
import net.michaeljackson23.mineademia.util.LivingEntityMixinAccessor;
import net.minecraft.client.model.ModelPart;
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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(BipedEntityModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> implements BipedModelMixinAccessor {
    @Unique
    private static final Vector3f TEMP = new Vector3f();

    @Unique
    private ModelPart root;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;getChild(Ljava/lang/String;)Lnet/minecraft/client/model/ModelPart;"), method = "<init>(Lnet/minecraft/client/model/ModelPart;Ljava/util/function/Function;)V")
    private void init(ModelPart root, Function<Identifier, RenderLayer> renderLayerFactory, CallbackInfo ci) {
//        HashMap<String, ModelPart> map = new HashMap<>();
//        map.put(EntityModelPartNames.HEAD, getSelf().head);
//        map.put(EntityModelPartNames.BODY, getSelf().body);
//        map.put(EntityModelPartNames.RIGHT_ARM, getSelf().rightArm);
//        map.put(EntityModelPartNames.LEFT_ARM, getSelf().leftArm);
//        map.put(EntityModelPartNames.RIGHT_LEG, getSelf().rightLeg);
//        map.put(EntityModelPartNames.LEFT_LEG, getSelf().leftLeg);
        this.root = root;
    }

    @Inject(at = @At("TAIL"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
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
//                    if (modelPart.traverse() == null) {
//                        System.out.println("Traverse() is null");
//                    } else {
//                        System.out.println("Traverse is not null");
//                    }
//                    System.out.println("Current Part: " + part + " IsEmpty: " + modelPart.traverse().count());
//                    livingEntity.sendMessage(Text.literal("Current Part: " + part + " IsEmpty: " + modelPart.traverse().count()));
                });
            }

            updateAnimation(state, animation.getAnimation(), h, 1.0f);
        }
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
