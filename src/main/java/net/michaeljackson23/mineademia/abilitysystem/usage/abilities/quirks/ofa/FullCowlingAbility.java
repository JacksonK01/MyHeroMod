package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.util.LivingEntityMixinAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class FullCowlingAbility extends ToggleAbility {
    private static final int STAMINA = 5;
    private static final int COOLDOWN = 5;
    private static final int COWLING_LEVEL_MAX = 10;
    private static final int SOUND_COUNT_MAX = 300;

    //The passive stamina use changes depending on level of cowling
    private int passiveStaminaUse = 1;
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
    public boolean executeStart() {
        LivingEntity entity = getEntity();
        if(entity instanceof ServerPlayerEntity player) {
            player.sendMessage(Text.literal("Cowling " + fullCowlingLevel * 10 + "%").withColor(0x00FF00), true);
        }
        return true;
    }

    @Override
    public void executeEnd() {
        fullCowlingLevel = 0;
        LivingEntity entity = getEntity();
        if(entity instanceof ServerPlayerEntity player) {
            player.sendMessage(Text.literal("Resetting Cowling to 0%").withColor(0x00FF00), true);
        }
    }

    @Override
    public boolean onTickActive() {
        handleStamina();
        spawnCowlingParticles();
        LivingEntity entity = getEntity();
        if(entity instanceof ServerPlayerEntity player) {
            if(player.isSprinting()) {
                ((LivingEntityMixinAccessor) player).getAnimationState().startIfNotRunning(player.age);
            } else {
                ((LivingEntityMixinAccessor) player).getAnimationState().stop();
            }
        }
        return true;
    }

    @Override
    public void onTickInactive() {

    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isKeyDown) {
            fullCowlingLevel++;
            if (isToggled() && fullCowlingLevel > COWLING_LEVEL_MAX) {
                executeEnd();
                setToggle(false);
            } else if (executeStart())
                setToggle(true);

        }
    }

    private void spawnCowlingParticles() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();
        world.spawnParticles(ParticleRegister.COWLING_PARTICLES, entity.getX(), entity.getY() + 1, entity.getZ(),
                fullCowlingLevel * 2, 0.3f, 0.5f, 0.3f, 0.1);
    }

    private void handleStamina() {
        if(getUser().getStamina() >= passiveStaminaUse) {
            getUser().offsetStamina(-passiveStaminaUse);
        }
    }

    private int calculatePassiveStamina() {
        return 0;
    }
}
