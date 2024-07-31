package net.michaeljackson23.mineademia.quirk.abilities.engine;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class PlummetJump extends BasicAbility {

    public PlummetJump() {
        super(1, 10, 10, "Plummet Jump", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        Vec3d playerVec = player.getRotationVector();
        double y;
        if(player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir()) {
            y = -1;
            AnimationProxy.sendAnimationToClients(player, "air_kick_down");
            AreaOfEffect.execute(player, 3, 3, player.getX(), player.getY(), player.getZ(), (entityToAffect -> {
                QuirkDamage.doPhysicalDamage(player, entityToAffect, 4f);
                entityToAffect.setVelocity(playerVec.x, y, playerVec.z);
            }));
        } else {
            y = 1.5;
        }
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 2f);
        player.setVelocity(playerVec.x, y, playerVec.z);
        player.velocityModified = true;
        spawnParticles(player);
    }

    private void spawnParticles(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        world.spawnParticles(ParticleTypes.FLAME,
                player.getX(), player.getY(), player.getZ(),
                10, 0, 0.2, 0, 1);
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                player.getX(), player.getY(), player.getZ(),
                10, 0, 0.2, 0, 1);
        world.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                player.getX(), player.getY(), player.getZ(),
                5, 0, 0.2, 0, 0.01);
        world.spawnParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY(), player.getZ(),
                5, 0, 0.2, 0, 0.01);
    }

}
