package net.michaeljackson23.mineademia.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Raycast {

    private Raycast() { }


    @Nullable
    public static EntityHitResult raycastEntity(@NotNull LivingEntity entity, double maxDistance) {
        maxDistance = maxDistance * maxDistance;

        Vec3d vec3d = entity.getCameraPosVec(1);
        Vec3d vec3d2 = entity.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);

        Box box = entity.getBoundingBox().stretch(vec3d2.multiply(maxDistance)).expand(1d, 1d, 1d);

        return ProjectileUtil.raycast(entity, vec3d, vec3d3, box, (target) -> !target.isSpectator() && target.canHit(), maxDistance);
    }

}
