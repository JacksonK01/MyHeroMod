package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;

/**
 * Base class of all toggleable abilities, uses the {@link ITickAbility} class to tick while active or inactive
 */
public interface IToggleAbility extends IActiveAbility, ITickAbility {

    boolean isToggled();
    void setToggle(boolean toggled);

    boolean executeStart();
    void executeEnd();

    boolean onTickActive();
    void onTickInactive();

    @Override
    default void execute(boolean isKeyDown) {
        if (isToggled() && isKeyDown) {
            executeEnd();
            setToggle(false);
        } else if (isKeyDown && executeStart()) {
            setToggle(true);
        }
    }

    @Override
    default void onTick() {
        if (isToggled()) {
            if (!onTickActive()) {
                executeEnd();
                setToggle(false);
            }
        } else
            onTickInactive();
    }
}
