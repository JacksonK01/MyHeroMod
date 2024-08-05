package net.michaeljackson23.mineademia.quirk.abilities.whirlwind;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.michaeljackson23.mineademia.util.RaycastToEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;

public class Ballista extends BasicAbility {
    private boolean init = false;

    public Ballista() {
        super(80, 100, 80, "Wind Ballista", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            init(player);
        }
        if(timer > 15) {
            EntityHitResult entityHitResult = RaycastToEntity.raycast(player, 15, (player2, x, y, z) -> {
                player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                        x, y, z,
                        10, 1, 1, 1, 0.05);
            });
            if(entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.setVelocity(player.getRotationVector().x, 0.2, player.getRotationVector().z);
                livingEntity.velocityModified = true;
                QuirkDamage.doEmitterDamage(player, livingEntity, 9f);
            }
            player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 2f, 2f);
        }
    }
    private void init(ServerPlayerEntity player) {
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 2f, 0.45f);
        AnimationProxy.sendAnimationToClients(player, "wind_ballista");
        init = true;
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        init = false;
    }
}
