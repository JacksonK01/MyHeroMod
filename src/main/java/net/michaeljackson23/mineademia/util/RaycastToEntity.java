package net.michaeljackson23.mineademia.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RaycastToEntity {
    @Nullable
    public static EntityHitResult raycast(LivingEntity entity, int distance) {
        Vec3d start = entity.getCameraPosVec(1.0f);
        //entity.sendMessage(Text.literal("Start X: " + (int) start.getX() + " Y: " + (int) start.getY() + " Z: " + (int) start.getZ()));

        Vec3d end = start.add(entity.getRotationVec(1.0f));
        //entity.sendMessage(Text.literal("End X: " + (int) end.getX() + " Y: " + (int) end.getY() + " Z: " + (int) end.getZ()));

        List<Entity> foundEntites = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Box box = new Box(start, end);
            List<Entity> dummyList = entity.getWorld().getOtherEntities(entity, box, (targetEntity) -> {
                return entity instanceof LivingEntity;
            });
            foundEntites.addAll(dummyList);
            start = end;
            //entity.sendMessage(Text.literal("Start X: " + start.getX() + " Y: " + start.getY() + " Z: " + start.getZ()));

            end = start.add(entity.getRotationVec(1.0f).multiply(1));
            //entity.sendMessage(Text.literal("End X: " + end.getX() + " Y: " + end.getY() + " Z: " + end.getZ()));
        }

        if (!foundEntites.isEmpty()) {
            int smallestIdx = 0;
            for(int i = 0; i < foundEntites.size(); i++) {
                double previous = Math.abs(entity.getX() - foundEntites.get(smallestIdx).getPos().getX()) +
                        Math.abs(entity.getY() - foundEntites.get(smallestIdx).getPos().getY()) +
                        Math.abs(entity.getZ() - foundEntites.get(smallestIdx).getPos().getZ());
                double next = Math.abs(entity.getX() - foundEntites.get(i).getPos().getX()) +
                        Math.abs(entity.getY() - foundEntites.get(i).getPos().getY()) +
                        Math.abs(entity.getZ() - foundEntites.get(i).getPos().getZ());
                if (previous > next) {
                    smallestIdx = i;
                }
            }
            entity.sendMessage(Text.literal("Entity found"));
            return new EntityHitResult(foundEntites.get(smallestIdx), foundEntites.get(smallestIdx).getPos());
        } else {
            entity.sendMessage(Text.literal("Entity null"));
            return null;
        }
    }
}
