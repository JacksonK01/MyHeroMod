package net.michaeljackson23.mineademia.animations;

import net.minecraft.client.render.entity.animation.Animation;

import java.util.List;

public class AnimationDataHolder {
    Animation animation;
    List<String> bodyPartsUsed;

    public AnimationDataHolder(Animation animation, String... bodyPartsUsed) {
        this.animation = animation;
        this.bodyPartsUsed = List.of(bodyPartsUsed);
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public List<String> getBodyPartsUsed() {
        return bodyPartsUsed;
    }
}
