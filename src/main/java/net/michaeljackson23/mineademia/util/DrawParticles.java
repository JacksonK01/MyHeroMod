package net.michaeljackson23.mineademia.util;

import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public final class DrawParticles {

    private final ServerWorld world;
    private final HashSet<ServerPlayerEntity> targets;

    private DrawParticles(@NotNull ServerWorld world, @NotNull Collection<ServerPlayerEntity> targets) {
        this.world = world;
        this.targets = new HashSet<>(targets);
    }

    // SPAWN

    public <T extends ParticleEffect> int spawnParticles(@NotNull T particleEffect, float x, float y, float z, int count, float deltaX, float deltaY, float deltaZ, float speed, boolean force) {
        ParticleS2CPacket particleS2CPacket = new ParticleS2CPacket(particleEffect, force, x, y, z, deltaX, deltaY, deltaZ, speed, count);
        int playerCounter = 0;

        for (ServerPlayerEntity serverPlayerEntity : targets) {
            if (world.sendToPlayerIfNearby(serverPlayerEntity, force, x, y, z, particleS2CPacket))
                ++playerCounter;
        }

        return playerCounter;
    }
    public <T extends ParticleEffect> int spawnParticles(@NotNull T particleEffect, float x, float y, float z, int count, @NotNull Vec3d delta, float speed, boolean force) {
        return spawnParticles(particleEffect, x, y, z, count, (float) delta.x, (float) delta.y, (float) delta.z, speed, force);
    }
    public <T extends ParticleEffect> int spawnParticles(@NotNull T particleEffect, @NotNull Vec3d pos, int count, float deltaX, float deltaY, float deltaZ, float speed, boolean force) {
        return spawnParticles(particleEffect, (float) pos.x, (float) pos.y, (float) pos.z, count, deltaX, deltaY, deltaZ, speed, force);
    }
    public <T extends ParticleEffect> int spawnParticles(@NotNull T particleEffect, @NotNull Vec3d pos, int count, @NotNull Vec3d delta, float speed, boolean force) {
        return spawnParticles(particleEffect, (float) pos.x, (float) pos.y, (float) pos.z, count, (float) delta.x, (float) delta.y, (float) delta.z, speed, force);
    }

    // SPAWN SINGLE

    public <T extends ParticleEffect> int spawnParticle(@NotNull T particleEffect, float x, float y, float z, float deltaX, float deltaY, float deltaZ, boolean force) {
        return spawnParticles(particleEffect, x, y, z, 1, deltaX, deltaY, deltaZ, 0, force);
    }
    public <T extends ParticleEffect> int spawnParticle(@NotNull T particleEffect, float x, float y, float z, @NotNull Vec3d delta, boolean force) {
        return spawnParticles(particleEffect, x, y, z, 1, (float) delta.x, (float) delta.y, (float) delta.z, 0, force);
    }
    public <T extends ParticleEffect> int spawnParticle(@NotNull T particleEffect, @NotNull Vec3d pos, float deltaX, float deltaY, float deltaZ, boolean force) {
        return spawnParticles(particleEffect, (float) pos.x, (float) pos.y, (float) pos.z, 1, deltaX, deltaY, deltaZ, 0, force);
    }
    public <T extends ParticleEffect> int spawnParticle(@NotNull T particleEffect, @NotNull Vec3d pos, @NotNull Vec3d delta, boolean force) {
        return spawnParticles(particleEffect, (float) pos.x, (float) pos.y, (float) pos.z, 1, (float) delta.x, (float) delta.y, (float) delta.z, 0, force);
    }

    // LINES

    public <T extends ParticleEffect> DrawParticles lerpBetween(@NotNull T particleEffect, int count, @NotNull Vec3d delta, float speed, boolean force, float time, float lerpDelta, float initialAlpha, @NotNull Vec3d... points) {
        float alpha = initialAlpha;

        while (alpha <= 1) {
            Vec3d point = Mathf.Vector.lerp(alpha, points);
            spawnParticles(particleEffect, point, count, delta, speed, force);

            alpha += lerpDelta;
        }

        return this;
    }

    // CIRCLE SHAPE

    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count, float speed, boolean force) {
        count = Math.max(1, count);
        speed = Math.max(0, speed);

        if (normal == Vec3d.ZERO)
            normal = Mathf.Vector.UP;

        normal = normal.normalize();

        Vec3d v1 = Mathf.Vector.getOrthogonal(normal);
        Vec3d v2 = v1.crossProduct(normal);

        for (int i = 0; i < 360; i += density) {
            float radians = i - rotation;

            Vec3d addCos = v1.multiply(Math.cos(radians));
            Vec3d addSin = v2.multiply(Math.sin(radians));

            Vec3d addAll = addCos.add(addSin).multiply(radius);
            Vec3d point = center.add(addAll);

            spawnParticles(particleEffect, point, count, delta, speed, force);
            // world.spawnParticles(particleEffect, point.x, point.y, point.z, count, delta.x, delta.y, delta.z, speed);
        }
    }

    // NO ROTATION

    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count, float speed, boolean force) {
        inCircle(center, normal, radius, 0, density, particleEffect, delta, count, speed, force);
    }

    // ROTATION, YES DELTA

    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count, boolean force) {
        inCircle(center, normal, radius, rotation, density, particleEffect, delta, count, 0, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta, float speed, boolean force) {
        inCircle(center, normal, radius, rotation, density, particleEffect, delta, 1, speed, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, @NotNull Vec3d delta, boolean force) {
        inCircle(center, normal, radius, rotation, density, particleEffect, delta, 1, 0, force);
    }

    // ROTATION, NO DELTA

    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, int count, boolean force) {
        inCircle(center, normal, radius, rotation, density, particleEffect, Vec3d.ZERO, count, 0, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, float speed, boolean force) {
        inCircle(center, normal, radius, rotation, density, particleEffect, Vec3d.ZERO, 1, speed, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, float rotation, int density, @NotNull T particleEffect, boolean force) {
        inCircle(center, normal, radius, rotation, density, particleEffect, Vec3d.ZERO, 1, 0, force);
    }

    // NO ROTATION, YES DELTA

    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta, int count, boolean force) {
        inCircle(center, normal, radius, 0, density, particleEffect, delta, count, 0, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta, float speed, boolean force) {
        inCircle(center, normal, radius, 0, density, particleEffect, delta, 1, speed, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, @NotNull Vec3d delta, boolean force) {
        inCircle(center, normal, radius, 0, density, particleEffect, delta, 1, 0, force);
    }

    // NO ROTATION, NO DELTA

    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, int count, boolean force) {
        inCircle(center, normal, radius, 0, density, particleEffect, Vec3d.ZERO, count, 0, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, float speed, boolean force) {
        inCircle(center, normal, radius, 0, density, particleEffect, Vec3d.ZERO, 1, speed, force);
    }
    public <T extends ParticleEffect> void inCircle(@NotNull Vec3d center, @NotNull Vec3d normal, float radius, int density, @NotNull T particleEffect, boolean force) {
        inCircle(center, normal, radius, 0, density, particleEffect, Vec3d.ZERO, 1, 0, force);
    }

    // VORTEX SHAPE

    public <T extends ParticleEffect> void inVortex(@NotNull Vec3d center, @NotNull Vec3d normal, @NotNull RadiusMap radiusMap, float rotation, float maxHeight, int lineCount, float density, float steepness, @NotNull T particleEffect, @NotNull Vec3d delta, int count, float speed, boolean force) {
        count = Math.max(1, count);
        speed = Math.max(0, speed);

        if (normal == Vec3d.ZERO)
            normal = Mathf.Vector.UP;
        normal = normal.normalize();

        Vec3d v1 = Mathf.Vector.getOrthogonal(normal);
        Vec3d v2 = v1.crossProduct(normal);

        Vec3d currentCenter = center;
        double distance = 0;

        for (int line = 0; line < lineCount; line++) {
            while (distance < maxHeight * maxHeight) {
                float partial = (float) distance / (maxHeight * maxHeight);
                float radius = radiusMap.getRadius(partial);

                double radians = Math.toRadians(360d / lineCount * line + distance * steepness - rotation);

                Vec3d addCos = v1.multiply(Math.cos(radians));
                Vec3d addSin = v2.multiply(Math.sin(radians));

                Vec3d addAll = addCos.add(addSin).multiply(radius);
                Vec3d point = currentCenter.add(addAll);

                // world.spawnParticles(particleEffect, point.x, point.y, point.z, count, delta.x, delta.y, delta.z, speed);
                spawnParticles(particleEffect, point, count, delta, speed, force);

                currentCenter = currentCenter.add(normal.multiply(density));
                distance = currentCenter.squaredDistanceTo(center);
            }
        }
    }




    // FOR

    public static @NotNull DrawParticles forPlayers(@NotNull ServerWorld world, @NotNull Collection<ServerPlayerEntity> players) {
        return new DrawParticles(world, players.stream().filter((p) -> p.getWorld().equals(world)).toList());
    }

    public static @NotNull DrawParticles forPlayer(@NotNull ServerPlayerEntity player) {
        return forPlayers((ServerWorld) player.getWorld(), List.of(player));
    }

    @Contract("_ -> new")
    public static @NotNull DrawParticles forWorld(@NotNull ServerWorld world) {
        return forPlayers(world, world.getPlayers());
    }

}
