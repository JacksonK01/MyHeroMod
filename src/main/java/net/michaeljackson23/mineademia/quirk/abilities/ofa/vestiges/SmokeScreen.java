package net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.HoldableAbility;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.joml.Vector3f;

public class SmokeScreen extends HoldableAbility {

    public SmokeScreen() {
        super( 3, 10, "Smokescreen", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        AreaOfEffect.execute(player, 3, 1, player.getX(), player.getY(), player.getZ(), (entityToAffect -> {
            entityToAffect.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 1, true, false, true));
            entityToAffect.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 2, true, false, true));
            entityToAffect.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 40, 1, true, false, true));
            QuirkDamage.doEmitterDamage(player, entityToAffect, 0.5f);
        }));
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 2f, 2f);
        player.getServerWorld().spawnParticles(
                new DustParticleEffect(new Vector3f(0.5f, 0.0f, 0.5f), 2.0f),
                player.getX(), player.getY() + 1, player.getZ(),
                15, 2, 1, 2, 1
        );
        quirk.setCooldown(quirk.getCooldown() + 1);
    }
}
