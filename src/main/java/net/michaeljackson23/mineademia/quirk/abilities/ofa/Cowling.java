package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.StopSoundProxy;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.w3c.dom.css.RGBColor;

import java.awt.*;

public class Cowling extends BasicAbility {
    //1 = 10%
    //2 = 20%
    //etc etc
    int cowlingPower = 0;
    int soundCounter = 0;
    //Sound is about 15 seconds, minecraft has 20 ticks per second, basic math.
    final int SOUND_COUNT_MAX = 300;

    private final PassiveAbility cowling = ((player, quirk) -> {
        if (shouldDeactivateCowling(quirk)) {
            deactivateCowling(player);
            return true;
        }
        applyCowlingEffects(player, quirk);
        return false;
    });

    public Cowling() {
        super(1, 3, 5, "Full Cowling", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if (player.isSneaking()) {
            decreaseCowlingPower(player);
        } else {
            increaseCowlingPower(player);
        }
        quirk.addPassive(cowling);
    }

    private boolean shouldDeactivateCowling(Quirk quirk) {
        return quirk.getStamina() < staminaDrain || cowlingPower <= 0;
    }

    private void deactivateCowling(ServerPlayerEntity player) {
        resetSound(player);
        cowlingPower = 0;
    }

    private void applyCowlingEffects(ServerPlayerEntity player, Quirk quirk) {
        player.fallDistance = 0f;
        quirk.setStamina(quirk.getStamina() - ((double) cowlingPower/2));
        playRepeatSound(player);
        spawnCowlingParticles(player);
        applyStatusEffects(player);
        if (cowlingPower == 0) {
            resetSound(player);
        }
    }

    private void playRepeatSound(ServerPlayerEntity player) {
        if (soundCounter == 0) {
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_REPEAT_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
        soundCounter++;
        if (soundCounter > SOUND_COUNT_MAX) {
            soundCounter = 0;
        }
    }

    private void spawnCowlingParticles(ServerPlayerEntity player) {
        player.getServerWorld().spawnParticles(ParticleRegister.COWLING_PARTICLES, player.getX(), player.getY() + 1, player.getZ(),
                cowlingPower * 2, 0.3f, 0.5f, 0.3f, 0.1);
    }

    private void applyStatusEffects(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2, cowlingPower, true, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 2, cowlingPower, true, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 2, cowlingPower, true, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, cowlingPower, true, false));
    }

    private void decreaseCowlingPower(ServerPlayerEntity player) {
        if (cowlingPower > 0) {
            cowlingPower--;
        }
        sendMessageToPlayer(player);
    }

    private void increaseCowlingPower(ServerPlayerEntity player) {
        if (cowlingPower <= 10) {
            cowlingPower++;
            if (cowlingPower == 1) {
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_START_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                soundCounter = 0;
            }
        }
        if (cowlingPower > 10) {
            resetCowlingPower(player);
        }
        sendMessageToPlayer(player);
    }

    private void resetCowlingPower(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Resetting Cowling to 0%"));
        cowlingPower = 0;
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_END_EVENT, SoundCategory.PLAYERS, 1f, 1f);
    }

    private void sendMessageToPlayer(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Cowling " + cowlingPower * 10 + "%").withColor(0x00FF00), true);
    }

    private void resetSound(ServerPlayerEntity player) {
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_END_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        StopSoundProxy.execute(player, CustomSounds.COWLING_REPEAT_ID);
        soundCounter = 0;
    }
}
