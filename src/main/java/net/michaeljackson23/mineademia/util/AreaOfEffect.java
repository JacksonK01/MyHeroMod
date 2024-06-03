package net.michaeljackson23.mineademia.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AreaOfEffect {
    public static void execute(LivingEntity owner, double radiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        execute(owner, radiusInBlocks, 0, x, y, z, actionOnEntity);
    }

    public static void execute(World world, double radiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        execute(world, radiusInBlocks, 0, x, y, z, actionOnEntity);
    }

    public static void execute(LivingEntity owner, double radiusInBlocks, double yRadiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        Box radiusBox = new Box(x - radiusInBlocks, y - yRadiusInBlocks, z - radiusInBlocks,
                x + radiusInBlocks, y + yRadiusInBlocks, z + radiusInBlocks);

        List<Entity> entitiesInRadius = owner.getWorld().getOtherEntities(owner, radiusBox);
        entitiesInRadius.forEach((entityToAffect -> {
            if(entityToAffect instanceof LivingEntity) {
                actionOnEntity.action((LivingEntity) entityToAffect);
            }
        }));
    }

    public static void execute(World world, double radiusInBlocks, double yRadiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        Box radiusBox = new Box(x - radiusInBlocks, y - yRadiusInBlocks, z - radiusInBlocks,
                x + radiusInBlocks, y + yRadiusInBlocks, z + radiusInBlocks);

        List<Entity> entitiesInRadius = world.getOtherEntities(null, radiusBox);
        entitiesInRadius.forEach((entityToAffect -> {
            if(entityToAffect instanceof LivingEntity) {
                actionOnEntity.action((LivingEntity) entityToAffect);
            }
        }));
    }

    @FunctionalInterface
    public interface ActionOnEntity {
        void action(LivingEntity entityToAffect);
    }
}
