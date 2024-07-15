package net.michaeljackson23.mineademia.quirk.abilities.whirlwind;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.ManchesterSmash;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class GaleUplift extends BasicAbility {
    int yScale = 0;
    boolean init = false;

    public GaleUplift() {
        super(30, 100, 40, "Gale Uplift", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            AnimationProxy.sendAnimationToClients(player, "whirlwind_up_down");
            player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 2f, 0.55f);
            init = true;
        }
        yScale++;
        player.setVelocity(0, 0, 0);
        player.velocityModified = true;
        if (timer <= abilityDuration - 1) {
            handleAscendingPhase(player);
        } else {
            handleDescendingPhase(player);
        }
    }

    private void handleAscendingPhase(ServerPlayerEntity player) {
        spawnCloudParticles(player, 10, 0.4, 0.01);
        AreaOfEffect.execute(player, 4, yScale, player.getX(), player.getY(), player.getZ(), entityToAffect -> {
            applyAscendingEffects(player, entityToAffect);
        });
    }

    private void handleDescendingPhase(ServerPlayerEntity player) {
        spawnCloudParticles(player, 25, 0.4, 1);
        AreaOfEffect.execute(player, 4, yScale, player.getX(), player.getY(), player.getZ(), entityToAffect -> {
            applyDescendingEffects(player, entityToAffect);
        });
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 2f, 2f);
    }

    private void spawnCloudParticles(ServerPlayerEntity player, int count, double spread, double speed) {
        player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY(), player.getZ(),
                count, spread, 0, spread, speed);
    }

    private void applyAscendingEffects(ServerPlayerEntity player, LivingEntity entityToAffect) {
        entityToAffect.setVelocity(0, 0.2, 0);
        entityToAffect.velocityModified = true;
        player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                entityToAffect.getX(), entityToAffect.getY() + 1, entityToAffect.getZ(),
                10, 0.3, 0.3, 0.3, 0.01);
    }

    private void applyDescendingEffects(ServerPlayerEntity player, LivingEntity entityToAffect) {
        QuirkDamage.doEmitterDamage(player, entityToAffect, 15f);
        entityToAffect.setVelocity(player.getRotationVector().x, -1, player.getRotationVector().z);
        entityToAffect.velocityModified = true;
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                entityToAffect.getX(), entityToAffect.getY() + 1, entityToAffect.getZ(),
                1, 0, 0, 0, 0);
        player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                entityToAffect.getX(), entityToAffect.getY() + 1, entityToAffect.getZ(),
                50, 0.3, 0.3, 0.3, 0.5);
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        yScale = 0;
        init = false;
    }
}
