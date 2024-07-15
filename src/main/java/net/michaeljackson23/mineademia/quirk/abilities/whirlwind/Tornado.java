package net.michaeljackson23.mineademia.quirk.abilities.whirlwind;

import net.michaeljackson23.mineademia.entity.projectile.stungrenade.StunGrenadeProjectile;
import net.michaeljackson23.mineademia.entity.projectile.tornado.TornadoProjectile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class Tornado extends BasicAbility {

    public Tornado() {
        super(1, 120, 30, "Magnitude 5", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, 1f, 2f);
        TornadoProjectile tornadoProjectile = new TornadoProjectile(player.getWorld(), player);
        tornadoProjectile.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 1.0f, 0);
        player.getWorld().spawnEntity(tornadoProjectile);

        player.swingHand(player.getActiveHand(), true);
    }
}
