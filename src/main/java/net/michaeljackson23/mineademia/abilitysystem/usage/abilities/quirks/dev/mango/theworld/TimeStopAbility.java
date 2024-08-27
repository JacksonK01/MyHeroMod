package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.networking.NetworkKeys;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;

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

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.QUIRK_THEWORLD_STOP_TIME_START, SoundCategory.MASTER, 4f, 1f);
        return true;
    }

    @Override
    public boolean executeEnd() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.QUIRK_THEWORLD_STOP_TIME_STOP, SoundCategory.MASTER, 4f, 1f);
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


    public static boolean shouldStopTimeClient(@NotNull World world, @NotNull IReadonlyTypesafeMap timeStopAbility, @NotNull Entity target) {
        boolean isUser = timeStopAbility.getAndCompute(NetworkKeys.UUID, (u) -> u.equals(target.getUuid()), false);

        boolean isTargetAffected = !isUser && isInsideTimeStopClient(world, timeStopAbility, target);

        return isTimeStoppedClient(world, timeStopAbility) && isTargetAffected;
    }
    public static boolean isTimeStoppedClient(@NotNull World world, @NotNull IReadonlyTypesafeMap timeStopAbility) {
        boolean active = timeStopAbility.getOrDefault(NetworkKeys.IS_ACTIVE, false);
        int ticks = timeStopAbility.getOrDefault(NetworkKeys.GET_TICKS, -1);

        return (active && ticks >= TimeStopAbility.START_DELAY) || (!active && ticks <= TimeStopAbility.END_DELAY);
    }
    public static boolean isInsideTimeStopClient(@NotNull World world, @NotNull IReadonlyTypesafeMap timeStopAbility, @NotNull Entity target) {
        return timeStopAbility.getAndCompute(NetworkKeys.ID, (id) -> target.squaredDistanceTo(world.getEntityById(id)) <= TimeStopAbility.MAX_RANGE_SQUARED, false);
    }


}
