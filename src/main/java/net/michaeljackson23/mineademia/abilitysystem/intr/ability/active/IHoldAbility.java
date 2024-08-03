package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;

/**
 * Base class of all hoold abilities, uses the {@link ITickAbility} class to tick while active or inactive
 */
public interface IHoldAbility extends IActiveAbility, ITickAbility {


    boolean isHeld();
    void setHeld(boolean toggled);

    boolean executeStart();
    void executeEnd();

    boolean onTickActive();
    void onTickInactive();

    @Override
    default void execute(boolean isKeyDown) {
        if (isHeld() && !isKeyDown) {
            executeEnd();
            setHeld(false);
        } else if (isKeyDown && executeStart()) {
            setHeld(true);
        }
    }

    @Override
    default void onTick() {
        if (isHeld()) {
            if (!onTickActive()) {
                executeEnd();
                setHeld(false);
            }
        } else
            onTickInactive();
    }


}
