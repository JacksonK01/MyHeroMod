package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class TimeStopAbility extends ToggleAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME = 1;
    public static final int STAMINA_PER_TICK = 0;

    public static final int MAX_TIME = 13 * 20;
    public static final int MAX_RANGE = 50;
    public static final int MAX_RANGE_SQUARED = MAX_RANGE * MAX_RANGE;

    public static final int START_DELAY = 20;
    public static final int END_DELAY = 40;

    private final Cooldown cooldown;

    public TimeStopAbility(@NotNull IAbilityUser user) {
        super(user, "Time Stop", "ZAWARDDUUUU", Networking.C2S_ABILITY_TWO);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public boolean executeStart() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        // world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.TIMESTOP_START, SoundCategory.MASTER, 4f, 1f);
        return true;
    }

    @Override
    public boolean executeEnd() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        // world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.TIMESTOP_END, SoundCategory.MASTER, 4f, 1f);
        return true;
    }

    @Override
    public void onTickActive() {

    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    public boolean shouldTimeStop(@NotNull Entity target) {
        LivingEntity entity = getEntity();
        int ticks = getTicks();

        boolean isStopped = (isActive() && ticks >= START_DELAY) || (!isActive() && ticks <= END_DELAY);
        boolean isTargetAffected = !target.equals(entity) && target.squaredDistanceTo(entity) <= MAX_RANGE_SQUARED;

        return isStopped && isTargetAffected;
    }

}
