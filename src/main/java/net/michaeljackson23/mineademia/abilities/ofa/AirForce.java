package net.michaeljackson23.mineademia.abilities.ofa;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.Empty;
import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class AirForce extends AbilityBase {

    private AirForce() {
        super(1, 10, 10, false, "Air Force", "The user flicks their fingers and creates intense wind pressure");
    }

    @Override
    public void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {
        playerData.setStamina(playerData.getStamina() - staminaDrain);
        playerData.setCooldown(cooldownAdd);
        AirForceProjectile airForceProjectile = new AirForceProjectile(player.getWorld(), player);
        airForceProjectile.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 2f, 0);
        player.getWorld().spawnEntity(airForceProjectile);

        player.swingHand(player.getActiveHand(), true);

        player.addVelocity(PlayerAngleVector.getPlayerAngleVector(player, -0.5, -0.5));
        player.velocityModified = true;
    }

    public static AbilityBase getInstance() {
        return new AirForce();
    }
}
