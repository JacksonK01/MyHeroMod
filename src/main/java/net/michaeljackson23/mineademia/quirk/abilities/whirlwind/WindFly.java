package net.michaeljackson23.mineademia.quirk.abilities.whirlwind;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.*;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.minecraft.entity.MovementType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

//Toggle
public class WindFly extends BasicAbility {
    private boolean isToggleActive = false;
    //TODO use packets for velocity
    PassiveAbility windFly = ((player, quirk) -> {
        player.fallDistance = 0f;
        if(!isToggleActive) {
            return true;
        }
        if(player.isSprinting()) {
            player.setVelocity(player.getRotationVector());
            player.velocityModified = true;
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
            AreaOfEffect.execute(player, 3, 1, player.getX(), player.getY(), player.getZ(), (entityToAffect -> {
                entityToAffect.setVelocity(player.getVelocity());
                entityToAffect.velocityModified = true;
            }));
        } else {
            ServerPlayNetworking.send(player, Networking.S2C_WIND_FLY_DESCENT_VELOCITY, PacketByteBufs.empty());
            player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY(), player.getZ(),
                    10,
                    1, 0, 1,
                    0.1);
        }
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
