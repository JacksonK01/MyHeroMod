package net.michaeljackson23.mineademia.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RaycastToEntity {

    public static EntityHitResult raycast(LivingEntity entity, int distance) {
        return raycast(entity, distance, ((player, x, y, z) -> {}));
    }

    @Nullable
    public static EntityHitResult raycast(LivingEntity entity, int distance, OnStepAction onStepAction) {
        List<Entity> foundEntities = performRaycast(entity, distance, onStepAction);
        return getClosestEntity(entity, foundEntities);
    }

    private static List<Entity> performRaycast(LivingEntity entity, int distance, OnStepAction onStepAction) {
        Vec3d start = entity.getCameraPosVec(1.0f);
        Vec3d direction = entity.getRotationVec(1.0f);
        List<Entity> foundEntities = new ArrayList<>();

        for (int i = 0; i < distance; i++) {
            Vec3d end = start.add(direction);
            Box box = new Box(start, end);
            if(entity instanceof ServerPlayerEntity player) {
                onStepAction.action(player, end.x, end.y, end.z);
            }
            List<Entity> entitiesInBox = entity.getWorld().getOtherEntities(entity, box,
                    (targetEntity) -> targetEntity instanceof LivingEntity);
            foundEntities.addAll(entitiesInBox);
            start = end;  // Move the start to the end for the next segment
        }

        return foundEntities;
    }

    @Nullable
    private static EntityHitResult getClosestEntity(LivingEntity entity, List<Entity> foundEntities) {
        if (foundEntities.isEmpty()) {
            return null;
        }

        Entity closestEntity = foundEntities.get(0);
        double closestDistance = entity.squaredDistanceTo(closestEntity);

        for (Entity targetEntity : foundEntities) {
            double currentDistance = entity.squaredDistanceTo(targetEntity);
            if (currentDistance < closestDistance) {
                closestEntity = targetEntity;
                closestDistance = currentDistance;
            }
        }

        return new EntityHitResult(closestEntity, closestEntity.getPos());
    }

    public interface OnStepAction {
        void action(ServerPlayerEntity player, double x, double y, double z);
    }
}
