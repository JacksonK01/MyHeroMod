package net.michaeljackson23.mineademia.animations;

import net.michaeljackson23.mineademia.util.BipedModelMixinAccessor;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnimationHelperBiped {

    public static void animate(BipedEntityModel<?> model, Animation animation, long runningTime, float scale, Vector3f tempVec) {
        float f = getRunningSeconds(animation, runningTime);
        for (Map.Entry<String, List<Transformation>> entry : animation.boneAnimations().entrySet()) {
            String key = entry.getKey();
            Optional<ModelPart> optional = ((BipedModelMixinAccessor) model).getChild(key);
            List<Transformation> list = entry.getValue();
            animate(list, optional, f, scale, tempVec);
        }
    }

    private static void animate(List<Transformation> list, Optional<ModelPart> optional, float f, float scale, Vector3f tempVec) {
        optional.ifPresent(part -> list.forEach(transformation -> {
            Keyframe[] keyframes = transformation.keyframes();
            int i = Math.max(0, MathHelper.binarySearch(0, keyframes.length, index -> f <= keyframes[index].timestamp()) - 1);
            int j = Math.min(keyframes.length - 1, i + 1);
            Keyframe keyframe = keyframes[i];
            Keyframe keyframe2 = keyframes[j];
            float h = f - keyframe.timestamp();
            float k = j != i ? MathHelper.clamp(h / (keyframe2.timestamp() - keyframe.timestamp()), 0.0f, 1.0f) : 0.0f;
            keyframe2.interpolation().apply(tempVec, k, keyframes, i, j, scale);
            transformation.target().apply((ModelPart) part, tempVec);
        }));
    }


    private static float getRunningSeconds(Animation animation, long runningTime) {
        float f = (float) runningTime / 1000.0f;
        return animation.looping() ? f % animation.lengthInSeconds() : f;
    }
}
