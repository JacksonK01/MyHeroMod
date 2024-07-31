package net.michaeljackson23.mineademia.quirk.abilities.hchh.ice;

import net.michaeljackson23.mineademia.entity.projectile.hchh.FireProjectile;
import net.michaeljackson23.mineademia.entity.projectile.hchh.IceProjectile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class IceShoot extends BasicAbility {

    public IceShoot() {
        super(10, 25, 10, "Emit Ice", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(timer % 2 == 0) {
            player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 2f, 2f);
            IceProjectile iceProjectile = new IceProjectile(player.getWorld(), player);
            iceProjectile.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 0.5f, 1);
            iceProjectile.setPosition(iceProjectile.getX(), iceProjectile.getY()-1, iceProjectile.getZ());
            player.getWorld().spawnEntity(iceProjectile);
            player.swingHand(Hand.OFF_HAND, true);
        }
    }
}
