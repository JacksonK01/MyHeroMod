package net.michaeljackson23.mineademia.util;

import net.michaeljackson23.mineademia.animations.AnimationDataHolder;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.entity.AnimationState;

public interface LivingEntityMixinAccessor {
    void setAnimationData(AnimationDataHolder animation);
    void setAnimationState(AnimationState animation);
    AnimationDataHolder getAnimationData();
    AnimationState getAnimationState();
}
