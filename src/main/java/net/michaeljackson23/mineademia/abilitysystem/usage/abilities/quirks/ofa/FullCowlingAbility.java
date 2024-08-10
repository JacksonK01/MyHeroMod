package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.statuseffects.StatusEffectsRegister;
import net.michaeljackson23.mineademia.util.StopSoundProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class FullCowlingAbility extends ActiveAbility implements ITickAbility, ICooldownAbility {
    //Check the calculate stamina method to see how the stamina works
    private static final int STAMINA = 0;
    private static final int COOLDOWN = 5;
    private static final int COWLING_LEVEL_MAX = 10;
    private static final int SOUND_COUNT_MAX = 300;

    private int fullCowlingLevel = 0;
    private int soundCounter = 0;


    private final Cooldown cooldown;

    public FullCowlingAbility(@NotNull IAbilityUser user) {
        super(user, "Full Cowling", "Control the output of your power", AbilityCategory.UTILITY);

        cooldown = new Cooldown(COOLDOWN);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isKeyDown && !(getEntity().isSneaking() && fullCowlingLevel == 0)) {
            processCowlingLevelLogic();
            playActivateSound();
        }
    }

    @Override
    public void onStartTick() {
        if(fullCowlingLevel > 0) {
            spawnCowlingParticles();
            applyStatusEffects();
            playRepeatSound();
            if(!hasStaminaAndConsume(calculatePassiveStamina()))
                cowlingReset();
        }
    }

    private void processCowlingLevelLogic() {
        LivingEntity livingEntity = getEntity();
        fullCowlingLevel += livingEntity.isSneaking() ? -1 : 1;
        if(livingEntity instanceof ServerPlayerEntity player) {
            player.sendMessage(Text.literal("Cowling " + fullCowlingLevel * 10 + "%").withColor(0x00FF00), true);
        }

        if(fullCowlingLevel > COWLING_LEVEL_MAX || fullCowlingLevel <= 0) {
            cowlingReset();
        }
    }

    private void cowlingReset() {
        fullCowlingLevel = 0;
        if(getEntity() instanceof ServerPlayerEntity player) {
            player.sendMessage(Text.literal("Cowling Deactivate").withColor(0x00FF00), true);
        }
        resetSound();
    }

    private void spawnCowlingParticles() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();
        world.spawnParticles(ModParticles.COWLING_PARTICLES, entity.getX(), entity.getY() + 1, entity.getZ(),
                fullCowlingLevel * 2, 0.3f, 0.5f, 0.3f, 0.1);
    }

    private int calculatePassiveStamina() {
        return fullCowlingLevel + STAMINA;
    }

    private void playActivateSound() {
        LivingEntity livingEntity = getEntity();
        if(fullCowlingLevel == 0)
            livingEntity.getWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), ModSounds.COWLING_END, SoundCategory.PLAYERS, 1f, 1f);
        else if(fullCowlingLevel == 1 && !livingEntity.isSneaking())
            livingEntity.getWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), ModSounds.COWLING_START_EVENT, SoundCategory.PLAYERS, 1f, 1f);
    }

    private void playRepeatSound() {
        LivingEntity livingEntity = getEntity();

        if (soundCounter == 0) {
            livingEntity.getWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), ModSounds.COWLING_REPEAT, SoundCategory.PLAYERS, 1f, 1f);
        }

        soundCounter++;
        if (soundCounter > SOUND_COUNT_MAX) {
            soundCounter = 0;
        }
    }

    private void resetSound() {
        LivingEntity livingEntity = getEntity();

        livingEntity.getWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), ModSounds.COWLING_END, SoundCategory.PLAYERS, 1f, 1f);

        if(livingEntity instanceof ServerPlayerEntity player)
            StopSoundProxy.execute(player, ModSounds.COWLING_REPEAT.getId());

        soundCounter = 0;
    }

    private void applyStatusEffects() {
        getEntity().addStatusEffect(new StatusEffectInstance(StatusEffectsRegister.EFFECT_COWLING, 2, fullCowlingLevel - 1, true, false));
    }
}
