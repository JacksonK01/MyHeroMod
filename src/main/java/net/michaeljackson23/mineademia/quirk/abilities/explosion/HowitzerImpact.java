package net.michaeljackson23.mineademia.quirk.abilities.explosion;

import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HowitzerImpact extends BasicAbility {
    private boolean init = false;
    private boolean phase1 = false;
    private boolean phase2 = false;

    private int phaseTimer = 0;

    public HowitzerImpact() {
        super(80, 350, 100, "Howitzer Impact", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        player.setVelocity(player.getRotationVector());
        player.velocityModified = true;

        if(!init) {
            phase1 = true;
            init = true;
        }

        if(phase1) {
            phase1(player);
            phaseTimer++;
        }

        if(phaseTimer > abilityDuration/2) {
            phase1 = false;
            phase2 = true;
        }

        if(phase2) {
            phase2(player);
        }
    }

    @Override
    protected void deactivate(ServerPlayerEntity player, Quirk quirk) {
        super.deactivate(player, quirk);
        impact(player);
        phase1 = false;
        phase2 = false;
        init = false;
        phaseTimer = 0;
    }

    private void phase1(ServerPlayerEntity player) {
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.MHA_EXPLOSION_EVENT, SoundCategory.PLAYERS, 1f, 2f);
        spawnParticlesUnderHands(player, ParticleTypes.EXPLOSION);
    }

    private void phase2(ServerPlayerEntity player) {
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 2f);
        Vec3d playerVec = player.getRotationVector();
        player.getServerWorld().spawnParticles(ParticleTypes.SMOKE, player.getX() + playerVec.x, player.getY() + 1 + playerVec.y, player.getZ() + playerVec.z, 300, 0.5, 0.5, 0.5, 0);
        spawnParticlesUnderHands(player, ParticleRegister.EXPLOSION_QUIRK_PARTICLES);
        AreaOfEffect.execute(player, 3, 0.5, player.getX(), player.getY(), player.getZ(), (entityToAffect -> {
            entityToAffect.setVelocity(playerVec);
            entityToAffect.velocityModified = true;
        }));
    }

    private void impact(ServerPlayerEntity player) {
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 0.5f);
        AreaOfEffect.execute(player, 5, 1, player.getX(), player.getY(), player.getZ(), (entityToAffect -> {
            QuirkDamage.doEmitterDamage(player, entityToAffect, 15f);
        }));
        player.getServerWorld().createExplosion(player, player.getX(), player.getY(), player.getZ(), 7f, true, World.ExplosionSourceType.TNT);
        player.setVelocity(player.getRotationVector().negate());
        player.velocityModified = true;
    }

    private void spawnParticlesUnderHands(ServerPlayerEntity player, DefaultParticleType type) {
        double pitch = ((player.getPitch() + 90) * Math.PI) / 180;
        double yaw = ((player.getYaw() + 90) * Math.PI)/ 180;
        double x = Math.cos(yaw);
        double y = Math.sin(pitch);
        double z = Math.sin(yaw);

        player.getServerWorld().spawnParticles(type,
                player.getX() + z*.5, player.getY() + 0.7, player.getZ() - x*.5,
                1, 0, 0, 0, 0);

        player.getServerWorld().spawnParticles(type,
                player.getX() - z*.5, player.getY() + 0.7, player.getZ() + x*.5,
                1, 0, 0, 0, 0);
    }
}
