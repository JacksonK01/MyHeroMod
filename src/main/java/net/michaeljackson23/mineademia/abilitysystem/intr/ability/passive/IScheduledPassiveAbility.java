package net.michaeljackson23.mineademia.abilitysystem.intr.ability.passive;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;

/**
 * Base class of all scheduled passive abilities
 */
public interface IScheduledPassiveAbility extends IPassiveAbility, ITickAbility {

    int getScheduleTime();

    int getCurrentTime();
    void setCurrentTime(int currentTime);

    @Override
    default void onStartTick() {
        int currentTime = getCurrentTime();

        if (currentTime <= 0) {
            setCurrentTime(getScheduleTime());
            execute(true);
        } else
            setCurrentTime(currentTime - 1);
    }

}
