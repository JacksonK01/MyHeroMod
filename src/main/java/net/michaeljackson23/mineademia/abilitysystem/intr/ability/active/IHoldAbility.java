package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;

/**
 * Base class of all hoold abilities, uses the {@link ITickAbility} class to tick while active or inactive
 */
public interface IHoldAbility extends IActivationAbility {

    boolean executeStart();
    void executeEnd();

    @Override
    default void execute(boolean isKeyDown) {
        if (!isKeyDown && isActive()) {
            executeEnd();

            resetTicks();
            setActive(false);
        } else if (isKeyDown && !isActive() && executeStart()) {
            resetTicks();
            setActive(true);
        }
    }


}
