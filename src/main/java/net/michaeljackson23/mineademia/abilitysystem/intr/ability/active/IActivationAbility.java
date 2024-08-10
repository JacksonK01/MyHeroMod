package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;

public interface IActivationAbility extends IActiveAbility, ITickAbility {

    boolean isActive();
    void setActive(boolean active);

    int getTicks();
    void setTicks(int ticks);

    void onTickActive();
    void onTickInactive();

    default void incrementTicks() {
        setTicks(getTicks() + 1);
    }
    default void resetTicks() {
        setTicks(0);
    }

    @Override
    default void onTick() {
        if (isActive())
            onTickActive();
        else
            onTickInactive();

        incrementTicks();
    }

}
