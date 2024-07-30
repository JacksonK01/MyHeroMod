package net.michaeljackson23.mineademia.quirk.abilities.hchh.fire;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleTypes;

public class FireShockwave extends BasicAbility {
    private double radius = 1.0;
    private double radiusIncrement = 0.5;
    private Vec3d playerPos;

    public FireShockwave() {
        super(40, 40, 40, "Fire Shockwave", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        ServerWorld world = player.getServerWorld();
        if(playerPos == null) {
            playerPos = player.getPos();
        }
        int particleCount = 100;

        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);
            double x = playerPos.x + xOffset;
            double y = playerPos.y;
            double z = playerPos.z + zOffset;

            world.spawnParticles(ParticleTypes.FLAME, x, y, z, 1, 0, 0, 0, 0);
        }
        if(playerPos != null) {
            AreaOfEffect.execute(player, radius, 1, playerPos.getX(), playerPos.getY(), player.getZ(), (entityToAffect -> {
                entityToAffect.setVelocity(findDistance(playerPos, entityToAffect.getPos()).negate().multiply(0.1));
                entityToAffect.velocityModified = true;
                world.spawnParticles(ParticleTypes.FLAME,
                        entityToAffect.getX(), entityToAffect.getY(), entityToAffect.getZ(), 10,
                        entityToAffect.getWidth(), entityToAffect.getHeight(), entityToAffect.getWidth(), 0.1);
            }));
        }
        this.radius += this.radiusIncrement;
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        radius = 1.0;
        playerPos = null;
    }

    private Vec3d findDistance(Vec3d vec1, Vec3d vec2) {
        double x = vec1.x - vec2.x;
        double y = vec1.y - vec2.y;
        double z = vec1.z - vec2.z;

        return new Vec3d(x, y, z);
    }
}
