package net.michaeljackson23.mineademia.quirk.abilities.explosion;

import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.entity.projectile.apshot.APShotProjectile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

public class APShot extends BasicAbility {
    int timer = 0;
    boolean hasAnimationPlayed = false;
    boolean hasFiredProjectile = false;

    public APShot() {
        super(10, 30, 10, "AP-Shot", "null");
    }

    @Override
    public void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!hasAnimationPlayed) {
            AnimationProxy.sendAnimationToClients(player, "ap_shot");
            hasAnimationPlayed = true;
        }

        if(timer >= 5) {
            if(!hasFiredProjectile) {
                player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.MHA_EXPLOSION_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                APShotProjectile apshot = new APShotProjectile(player.getWorld(), player);
                apshot.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 2f, 0);
                player.getWorld().spawnEntity(apshot);
                hasFiredProjectile = true;
            }
        }
        timer++;
    }

    @Override
    protected void deactivate(ServerPlayerEntity player, Quirk quirk) {
        super.deactivate(player, quirk);
        timer = 0;
        hasFiredProjectile = false;
        hasAnimationPlayed = false;
    }
}
