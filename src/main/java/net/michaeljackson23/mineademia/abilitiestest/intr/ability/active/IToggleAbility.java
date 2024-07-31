package net.michaeljackson23.mineademia.abilitiestest.intr.ability.active;

import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ITickAbility;

/**
 * Base class of all toggleable abilities, uses the {@link ITickAbility} class to tick while active or inactive
 */
public interface IToggleAbility extends IActiveAbility, ITickAbility {

    boolean isToggled();
    void setToggle(boolean toggled);

    void executeStart();
    void executeEnd();

    void onTickActive();
    void onTickInactive();

    @Override
    default void execute() {
        if (isToggled()) {
            setToggle(false);
            executeEnd();
        } else {
            setToggle(true);
            executeStart();
        }
    }

    @Override
    default void onTick() {
        if (isToggled())
            onTickActive();
        else
            onTickInactive();
    }

}
