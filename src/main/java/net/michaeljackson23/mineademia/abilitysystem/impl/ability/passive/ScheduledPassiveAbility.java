package net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.PassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.passive.IScheduledPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.Mathf;
import org.jetbrains.annotations.NotNull;

public abstract class ScheduledPassiveAbility extends PassiveAbility implements IScheduledPassiveAbility {

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
        this.currentTime = Mathf.clamp(0, getScheduleTime(), currentTime);
    }

}
