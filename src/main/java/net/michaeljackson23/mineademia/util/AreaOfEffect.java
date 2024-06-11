package net.michaeljackson23.mineademia.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class AreaOfEffect {

    public static void execute(LivingEntity owner, double radiusInBlocks, double yRadiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        Box radiusBox = new Box(x - radiusInBlocks, y - yRadiusInBlocks, z - radiusInBlocks,
                x + radiusInBlocks, y + yRadiusInBlocks, z + radiusInBlocks);

        List<Entity> entitiesInRadius = owner.getWorld().getOtherEntities(owner, radiusBox);
        entitiesInRadius.forEach((entityToAffect -> {
            if (entityToAffect instanceof LivingEntity) {
                actionOnEntity.action((LivingEntity) entityToAffect);
            }
        }));

        if(owner.getWorld() instanceof ServerWorld world1) {
            renderBoxWithParticles(world1, radiusBox);
        }
    }

    public static void execute(World world, double radiusInBlocks, double yRadiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        Box radiusBox = new Box(x - radiusInBlocks, y - yRadiusInBlocks, z - radiusInBlocks,
                x + radiusInBlocks, y + yRadiusInBlocks, z + radiusInBlocks);

        List<Entity> entitiesInRadius = world.getOtherEntities(null, radiusBox);
        entitiesInRadius.forEach((entityToAffect -> {
            if (entityToAffect instanceof LivingEntity) {
                actionOnEntity.action((LivingEntity) entityToAffect);
            }
        }));
        if(world instanceof ServerWorld world1) {
            renderBoxWithParticles(world1, radiusBox);
        }
    }

    @FunctionalInterface
    public interface ActionOnEntity {
        void action(LivingEntity entityToAffect);
    }

    private static void renderBoxWithParticles(ServerWorld world, Box box) {
        double stepSize = 1; // Adjust step size for particle density

        // Draw edges of the box
        for (double x = box.minX; x <= box.maxX; x += stepSize) {
            spawnParticleLine(world, new Vec3d(x, box.minY, box.minZ), new Vec3d(x, box.minY, box.maxZ), stepSize);
            spawnParticleLine(world, new Vec3d(x, box.maxY, box.minZ), new Vec3d(x, box.maxY, box.maxZ), stepSize);
        }
        for (double z = box.minZ; z <= box.maxZ; z += stepSize) {
            spawnParticleLine(world, new Vec3d(box.minX, box.minY, z), new Vec3d(box.maxX, box.minY, z), stepSize);
            spawnParticleLine(world, new Vec3d(box.minX, box.maxY, z), new Vec3d(box.maxX, box.maxY, z), stepSize);
        }
        for (double y = box.minY; y <= box.maxY; y += stepSize) {
            spawnParticleLine(world, new Vec3d(box.minX, y, box.minZ), new Vec3d(box.minX, y, box.maxZ), stepSize);
            spawnParticleLine(world, new Vec3d(box.maxX, y, box.minZ), new Vec3d(box.maxX, y, box.maxZ), stepSize);
        }

        // Draw vertical edges
        spawnParticleLine(world, new Vec3d(box.minX, box.minY, box.minZ), new Vec3d(box.minX, box.maxY, box.minZ), stepSize);
        spawnParticleLine(world, new Vec3d(box.minX, box.minY, box.maxZ), new Vec3d(box.minX, box.maxY, box.maxZ), stepSize);
        spawnParticleLine(world, new Vec3d(box.maxX, box.minY, box.minZ), new Vec3d(box.maxX, box.maxY, box.minZ), stepSize);
        spawnParticleLine(world, new Vec3d(box.maxX, box.minY, box.maxZ), new Vec3d(box.maxX, box.maxY, box.maxZ), stepSize);
    }

    private static void spawnParticleLine(ServerWorld world, Vec3d start, Vec3d end, double stepSize) {
        Vec3d direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        for (double i = 0; i <= distance; i += stepSize) {
            Vec3d pos = start.add(direction.multiply(i));
            world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        }
    }
}
