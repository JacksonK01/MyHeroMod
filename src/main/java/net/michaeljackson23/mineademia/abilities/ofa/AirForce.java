package net.michaeljackson23.mineademia.abilities.ofa;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class AirForce extends AbilityBase {

    public AirForce() {
        this.title = "Air Force";
        this.description = "The user flicks their fingers and creates intense wind pressure";
        this.abilityDuration = 1;
    }

    @Override
    public void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {
        playerData.setStamina(playerData.getStamina() - staminaDrain);
        playerData.setCooldown(cooldownAdd);
        player.addVelocity(PlayerAngleVector.getPlayerAngleVector(player, -0.5, -0.5));
        player.velocityModified = true;

        AirForceProjectile airForceProjectile = new AirForceProjectile(player.getWorld(), player);
        airForceProjectile.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 2f, 0);
        player.getWorld().spawnEntity(airForceProjectile);

        player.swingHand(player.getActiveHand(), true);
    }
}
