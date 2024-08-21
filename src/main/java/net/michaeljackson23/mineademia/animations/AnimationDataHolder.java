package net.michaeljackson23.mineademia.animations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.michaeljackson23.mineademia.util.EntityAnimatedPartNames;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnimationDataHolder {
    private final Animation animation;
    private final List<String> bodyPartsUsed;
    private final boolean isJointedAnimation;
    private final boolean doesContainRoot;

    //Useful in case for example, you animate the head, but also want the player's mouse to move the head.
    public AnimationDataHolder(Animation animation, String... bodyPartsUsed) {
        this.bodyPartsUsed = List.of(bodyPartsUsed);

        Set<String> bones = animation.boneAnimations().keySet();

        this.isJointedAnimation = !Collections.disjoint(bones, EntityAnimatedPartNames.getAllParts());

        this.animation = isJointedAnimation ? buildJointedAnimation(animation) : animation;
        this.doesContainRoot = this.animation.boneAnimations().containsKey(EntityAnimatedPartNames.ANIMATED_ROOT);
    }

    public AnimationDataHolder(Animation animation) {
        Set<String> bones = animation.boneAnimations().keySet();

        this.isJointedAnimation = !Collections.disjoint(bones, EntityAnimatedPartNames.getAllParts());

        this.animation = isJointedAnimation ? buildJointedAnimation(animation) : animation;

        this.bodyPartsUsed = List.copyOf(this.animation.boneAnimations().keySet());
        this.doesContainRoot = this.animation.boneAnimations().containsKey(EntityAnimatedPartNames.ANIMATED_ROOT);
    }

    private Animation buildJointedAnimation(Animation temp) {
        Map<String, List<Transformation>> newBoneAnimations = Maps.newHashMap();

        temp.boneAnimations().forEach((boneString, transformation) -> {
            newBoneAnimations.put(EntityAnimatedPartNames.getEquivalencePart(boneString), transformation);
        });

        return new Animation(temp.lengthInSeconds(), temp.looping(), newBoneAnimations);
    }

    public Animation getAnimation() {
        return animation;
    }

    public List<String> getBodyPartsUsed() {
        return bodyPartsUsed;
    }

    public boolean isJointedAnimation() {
        return isJointedAnimation;
    }

    public boolean doesContainRoot() {
        return doesContainRoot;
    }
}
