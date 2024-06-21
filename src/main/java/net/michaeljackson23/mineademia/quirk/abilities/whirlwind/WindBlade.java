package net.michaeljackson23.mineademia.quirk.abilities.whirlwind;

import net.michaeljackson23.mineademia.entity.projectile.windblade.WindBladeProjectile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class WindBlade extends BasicAbility {

    public WindBlade() {
        super(1, 20, 45, "Wind Blade", "Insert title");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        WindBladeProjectile windBlade = new WindBladeProjectile(player.getWorld(), player);
        windBlade.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 1f, 0);
        player.getWorld().spawnEntity(windBlade);
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1.0f, 2.0f);
        player.swingHand(player.getActiveHand(), true);
    }
}
