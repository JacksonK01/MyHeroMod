package net.michaeljackson23.mineademia.quirk.abilities.explosion;

import net.michaeljackson23.mineademia.entity.projectile.apshot.APShotProjectile;
import net.michaeljackson23.mineademia.entity.projectile.stungrenade.StunGrenadeProjectile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

public class StunGrenade extends AbilityBase {

    public StunGrenade() {
        super(1, 30, 10, false, "Stun-Grenade", "null");
    }

    @Override
    public void activate(ServerPlayerEntity player, Quirk quirk) {
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.MHA_EXPLOSION_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        StunGrenadeProjectile stun = new StunGrenadeProjectile(player.getWorld(), player);
        stun.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 0.7f, 0);
        player.getWorld().spawnEntity(stun);

        player.swingHand(player.getActiveHand(), true);
    }
}
