package net.michaeljackson23.mineademia.abilities.ofa;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.abilityinit.PassiveAbility;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

public class Cowling extends AbilityBase {
    //1 = 10%
    //2 = 20%
    //etc etc
    int cowlingPower = 0;
    PassiveAbility cowling;
    int soundCounter = 0;
    //Sound is about 15 seconds, minecraft has 20 ticks per second, basic math.
    final int SOUND_COUNT_MAX = 300;

    private Cowling() {
        super(1, 0, 5, false, "Cowling", "Add Description");
    }

    @Override
    protected void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {
        if(player.isSneaking() && cowlingPower > 0) {
            cowlingPower--;
        } else if(!player.isSneaking() && cowlingPower <= 10){
            cowlingPower++;
            if(cowlingPower == 1) {
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_START_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                soundCounter = 0;
            }
        }
        if(cowlingPower <= 10) {
            player.sendMessage(Text.literal("Cowling " + cowlingPower * 10 + "%"));
        } else {
            player.sendMessage(Text.literal("Resetting Cowling to 0%"));
            cowlingPower = 0;
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_END_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
        if(cowling == null) {
            cowling = () -> {
                if(soundCounter == 0) {
                    player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_REPEAT_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                }
                soundCounter++;
                if (soundCounter > SOUND_COUNT_MAX) {
                    soundCounter = 0;
                }
                player.getServerWorld().spawnParticles(ParticleRegister.COWLING_PARTICLES, player.getX(), player.getY() + 1, player.getZ(),
                        cowlingPower, 0.3f, 0.5f, 0.3f, 0);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2, cowlingPower, true, false));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 2, cowlingPower, true, false));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 2, cowlingPower, true, false));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, cowlingPower, true, false));
                if(cowlingPower == 0) {
                    player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.COWLING_END_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                    StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(CustomSounds.COWLING_REPEAT_ID, SoundCategory.PLAYERS);
                    player.networkHandler.sendPacket(stopSoundS2CPacket);
                }
                return cowlingPower <= 0;
            };
        }
        if(!playerData.getPassives().contains(cowling)) {
            playerData.getPassives().add(cowling);
        }
    }

    public static AbilityBase getInstance() {
        return new Cowling();
    }
}
