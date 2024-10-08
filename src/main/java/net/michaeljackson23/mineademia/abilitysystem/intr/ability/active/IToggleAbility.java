package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;

/**
 * Base class of all toggleable abilities, uses the {@link ITickAbility} class to tick while active or inactive
 */
public interface IToggleAbility extends IActivationAbility {

    boolean executeStart();
    boolean executeEnd();

    @Override
    default void execute(boolean isKeyDown) {
        if (isKeyDown && isActive()) {
            setActive(false);
            if (!executeEnd()) {
                setActive(true);
                return;
            }

            resetTicks();
            setActive(false);
        } else if (isKeyDown && !isActive() && executeStart()) {
            resetTicks();
            setActive(true);
        }
    }

}
