package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.server.network.ServerPlayerEntity;

public class AirForce extends AbilityBase {

    public AirForce() {
        super(1, 100, 5, false, "Air Force", "The user flicks their fingers and creates intense wind pressure");
    }

    @Override
    public void activate(ServerPlayerEntity player, Quirk quirk) {
        AirForceProjectile airForceProjectile = new AirForceProjectile(player.getWorld(), player);
        airForceProjectile.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 1f, 0);
        player.getWorld().spawnEntity(airForceProjectile);

        player.swingHand(player.getActiveHand(), true);

        player.setVelocity(PlayerAngleVector.getPlayerAngleVector(player, -0.5, -0.5));
        player.velocityModified = true;
    }
}
