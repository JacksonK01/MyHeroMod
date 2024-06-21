package net.michaeljackson23.mineademia.quirk.abilities.whirlwind;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.*;
import net.minecraft.entity.MovementType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

//Toggle
public class WindFly extends BasicAbility {
    private boolean isToggleActive = false;
    PassiveAbility windFly = ((player, quirk) -> {
        if(!isToggleActive) {
            return true;
        }
        if(player.isSprinting()) {
            player.setVelocity(player.getRotationVector());
            player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY() + 1, player.getZ(),
                    4,
                    0.4, 0.5, 0.4,
                    0.1);
            player.getServerWorld().spawnParticles(ParticleTypes.SWEEP_ATTACK,
                    player.getX(), player.getY() + 1, player.getZ(),
                    3,
                    0.4, 0.5, 0.4,
                    0.1);
        } else {
            Vec3d velocity = player.getVelocity();
            player.travel(velocity.multiply(1, 1.5, 1));
            player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY(), player.getZ(),
                    10,
                    1, 0, 1,
                    0.1);
        }
        player.velocityModified = true;
        return false;
    });

    public WindFly() {
        super(1, 5, 6, "Fly", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        isToggleActive = !isToggleActive;
        if(isToggleActive) {
            quirk.addPassive(windFly);
        }
    }
}
