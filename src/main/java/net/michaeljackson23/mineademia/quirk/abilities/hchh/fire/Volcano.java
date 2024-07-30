package net.michaeljackson23.mineademia.quirk.abilities.hchh.fire;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Volcano extends BasicAbility {
    private ArrayList<LivingEntity> entities = new ArrayList<>();
    private final float PARTICLE_DENSITY = 1.0f;

    public Volcano() {
        super(120, 100, 120, "Fire Volcano", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(entities.isEmpty()) {
            AreaOfEffect.execute(player, 10, 5, player.getX(), player.getY(), player.getZ(), (entity) -> {
                int numberOfParticles = 50;
                double stepSize = 1.0 / numberOfParticles;

                double playerX = player.getX();
                double playerY = player.getY() + 1;
                double playerZ = player.getZ();
                for (double t = 0; t <= PARTICLE_DENSITY; t += stepSize) {
                    double x = playerX + t * (entity.getPos().getX() - playerX);
                    double y = playerY + t * (entity.getPos().getY() - playerY);
                    double z = playerZ + t * (entity.getPos().getZ() - playerZ);

                    player.getServerWorld().spawnParticles(ParticleTypes.FLAME, x, y, z, 4, 1f, 1f, 1f, 0.01);
                }
                entities.add(entity);
            });
        } else {
            entities.forEach((livingEntity -> {
                livingEntity.setVelocity(findDistance(player, livingEntity).multiply(0.01));
                player.getServerWorld().spawnParticles(ParticleTypes.FLAME,
                        livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        50, livingEntity.getWidth(), livingEntity.getHeight() + 5, livingEntity.getWidth(),
                        0);
            }));
        }
        player.getServerWorld().spawnParticles(ParticleTypes.FLAME,
                player.getX(), player.getY(), player.getZ(),
                5, player.getWidth(), player.getHeight(), player.getWidth(),
                0.01);
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        entities.forEach((entity) -> {
            QuirkDamage.doEmitterDamage(player, entity, 15f);
            player.getServerWorld().spawnParticles(ParticleTypes.FLAME,
                    entity.getX(), entity.getY(), entity.getZ(),
                    25, entity.getWidth(), entity.getHeight(), entity.getWidth(),
                    1);
            player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                    entity.getX(), entity.getY(), entity.getZ(),
                    3, entity.getWidth(), entity.getHeight(), entity.getWidth(),
                    1);
            player.getServerWorld().spawnParticles(ParticleTypes.LAVA,
                    entity.getX(), entity.getY(), entity.getZ(),
                    10, entity.getWidth(), entity.getHeight(), entity.getWidth(),
                    1);
            entity.setVelocity(player.getRotationVector().multiply(3));
        });
    }


    private Vec3d findDistance(ServerPlayerEntity player, LivingEntity entity) {
        Vec3d playerPos = player.getPos();
        Vec3d entityPos = entity.getPos();
        double x = playerPos.x - entityPos.x;
        double y = playerPos.y - entityPos.y;
        double z = playerPos.z - entityPos.z;

        return new Vec3d(x, y, z);
    }
}
