package net.michaeljackson23.mineademia.quirk.abilities.hchh.fire;

import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.entity.projectile.hchh.FireProjectile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class FireShoot extends BasicAbility {

    public FireShoot() {
        super(10, 25, 10, "Emit Fire", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(timer % 2 == 0) {
            player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, 2f, 2f);
            FireProjectile fireProjectile = new FireProjectile(player.getWorld(), player);
            fireProjectile.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 0.5f, 1);
            player.getWorld().spawnEntity(fireProjectile);
            player.swingHand(Hand.OFF_HAND, true);
        }
    }
}
