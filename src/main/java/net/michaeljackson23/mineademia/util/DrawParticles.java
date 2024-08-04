package net.michaeljackson23.mineademia.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public final class DrawParticles {

    private DrawParticles() { }

    public static final Vec3d UP = new Vec3d(0, 1, 0);


    // CIRCLE SHAPE

    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count, float speed) {
        count = Math.max(1, count);
        speed = Math.max(0, speed);

        normal = normal.normalize();

        Vec3d v1 = new Vec3d(normal.z, 0, -normal.x).normalize();
        Vec3d v2 = v1.crossProduct(normal);

        for (int i = 0; i < 360; i += density) {
            float radians = i - rotation;

            Vec3d addCos = v1.multiply(Math.cos(radians));
            Vec3d addSin = v2.multiply(Math.sin(radians));

            Vec3d addAll = addCos.add(addSin).multiply(radius);
            Vec3d point = center.add(addAll);

            world.spawnParticles(particleEffect, point.x, point.y, point.z, count, delta.x, delta.y, delta.z, speed);
        }
    }

    // NO ROTATION

    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count, float speed) {
        inCircle(world, center, normal, radius, 0, density, particleEffect, delta, count, speed);
    }

    // ROTATION, YES DELTA

    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count) {
        inCircle(world, center, normal, radius, rotation, density, particleEffect, delta, count, 0);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta, float speed) {
        inCircle(world, center, normal, radius, rotation, density, particleEffect, delta, 1, speed);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta) {
        inCircle(world, center, normal, radius, rotation, density, particleEffect, delta, 1, 0);
    }

    // ROTATION, NO DELTA

    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, int count) {
        inCircle(world, center, normal, radius, rotation, density, particleEffect, Vec3d.ZERO, count, 0);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, float speed) {
        inCircle(world, center, normal, radius, rotation, density, particleEffect, Vec3d.ZERO, 1, speed);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect) {
        inCircle(world, center, normal, radius, rotation, density, particleEffect, Vec3d.ZERO, 1, 0);
    }

    // NO ROTATION, YES DELTA

    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count) {
        inCircle(world, center, normal, radius, 0, density, particleEffect, delta, count, 0);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta, float speed) {
        inCircle(world, center, normal, radius, 0, density, particleEffect, delta, 1, speed);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta) {
        inCircle(world, center, normal, radius, 0, density, particleEffect, delta, 1, 0);
    }

    // NO ROTATION, NO DELTA

    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, int count) {
        inCircle(world, center, normal, radius, 0, density, particleEffect, Vec3d.ZERO, count, 0);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, float speed) {
        inCircle(world, center, normal, radius, 0, density, particleEffect, Vec3d.ZERO, 1, speed);
    }
    public static <T extends ParticleEffect> void inCircle(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect) {
        inCircle(world, center, normal, radius, 0, density, particleEffect, Vec3d.ZERO, 1, 0);
    }

    // VORTEX SHAPE

    public static <T extends ParticleEffect> void inVortex(@NotNull ServerWorld world, @NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, float maxHeight, int lineCount, float stepSize, float steepness, @NotNull T particleEffect, @NotNull Vec3d delta, int count, float speed) {
        count = Math.max(1, count);
        speed = Math.max(0, speed);

        // if (normal == Vec3d.ZERO)
        //    normal = UP;

        normal = normal.normalize();

        Vec3d v1 = new Vec3d(normal.z, 0, -normal.x).normalize();
        Vec3d v2 = v1.crossProduct(normal);

        Vec3d currentCenter = center;
        double distance = 0;

        for (int line = 0; line < lineCount; line++) {
            while (distance < maxHeight * maxHeight) {
                double radians = Math.toRadians(360d / lineCount * line + distance * steepness - rotation);

                Vec3d addCos = v1.multiply(Math.cos(radians));
                Vec3d addSin = v2.multiply(Math.sin(radians));

                Vec3d addAll = addCos.add(addSin).multiply(radius);
                Vec3d point = currentCenter.add(addAll);

                world.spawnParticles(particleEffect, point.x, point.y, point.z, count, delta.x, delta.y, delta.z, speed);

                currentCenter = currentCenter.add(normal.multiply(stepSize));
                distance = currentCenter.squaredDistanceTo(center);
            }
        }

//        for (int i = 0; i < 360; i += density) {
//            float radians = i - rotation;
//
//            Vec3d addCos = v1.multiply(Math.cos(radians));
//            Vec3d addSin = v2.multiply(Math.sin(radians));
//
//            Vec3d addAll = addCos.add(addSin).multiply(radius);
//            Vec3d point = center.add(addAll);
//
//            world.spawnParticles(particleEffect, point.x, point.y, point.z, count, delta.x, delta.y, delta.z, speed);
//        }
    }


}
