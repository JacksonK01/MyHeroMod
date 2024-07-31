package net.michaeljackson23.mineademia.abilitiestest.impl.ability.passive;

import net.michaeljackson23.mineademia.abilitiestest.impl.ability.PassiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.passive.IScheduledPassiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class ScheduledPassiveAbility extends PassiveAbility implements IScheduledPassiveAbility {

    private static final HashMap<IAbilityUser, Integer> scheduleMap = new HashMap<>();


    private final int scheduleTime;
    private int currentTime;

    public ScheduledPassiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, int scheduleTime) {
        super(user, name, description);

        this.scheduleTime = Math.max(1, scheduleTime);
        this.currentTime = scheduleTime;
    }

    @Override
    public int getScheduleTime() {
        return scheduleTime;
    }

    @Override
    public int getCurrentTime() {
        return this.currentTime;
    }

    @Override
    public void setCurrentTime(int currentTime) {
        this.currentTime = Math.max(0, Math.min(getScheduleTime(), currentTime));
    }

}
