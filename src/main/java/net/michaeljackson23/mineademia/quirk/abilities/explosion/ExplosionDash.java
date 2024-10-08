package net.michaeljackson23.mineademia.quirk.abilities.explosion;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.HoldableAbility;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

public class ExplosionDash extends HoldableAbility {
    int interval = 0;
    final int MAX_INTERVAL = 20;

    int velocityCounter = 0;
    PassiveAbility particlesPassive = (player, quirk) -> {
        if(getStaminaDrain() > quirk.getStamina()) {
            return true;
        }else if(interval <= 0) {
            interval = 0;
            return true;
        }
        interval--;
        spawnParticlesUnderHands(player, ParticleTypes.SMOKE);

        return false;
    };

    public ExplosionDash() {
        super(5, 5, "Explosion Dash", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        interval = 10;
        quirk.addPassive(particlesPassive);
        velocityCounter++;
        if(velocityCounter >= 5) {
            spawnParticlesUnderHands(player, ParticleTypes.EXPLOSION);
            player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MHA_EXPLOSION, SoundCategory.PLAYERS, 1f, 2f);
            player.setVelocity(player.getRotationVec(1.0f).multiply(2.5));
            player.velocityModified = true;
            velocityCounter = 0;
            quirk.setCooldown(quirk.getCooldown() + cooldownAdd);
            QuirkDataPacket.sendProxy(player);
            player.fallDistance = 0f;
        }
    }

    private void spawnParticlesUnderHands(ServerPlayerEntity player, DefaultParticleType type) {
        double pitch = ((player.getPitch() + 90) * Math.PI) / 180;
        double yaw = ((player.getYaw() + 90) * Math.PI) / 180;
        double x = Math.cos(yaw) ;
        double y = Math.sin(pitch);
        double z = Math.sin(yaw);

        player.getServerWorld().spawnParticles(type,
                player.getX() + z*.5, player.getY() + 0.7, player.getZ() - x*.5,
                1, 0, 0, 0, 0);

        player.getServerWorld().spawnParticles(type,
                player.getX() - z*.5, player.getY() + 0.7, player.getZ() + x*.5,
                1, 0, 0, 0, 0);
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        velocityCounter = 0;
    }
}
