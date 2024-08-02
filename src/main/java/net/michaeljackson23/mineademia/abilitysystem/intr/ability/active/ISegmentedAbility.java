package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;

/**
 * Base class of all segmented abilities
 */
public interface ISegmentedAbility extends IActiveAbility, ICooldownAbility {

    int getMaxCharges();

    int getCharges();
    void setCharges(int charges);

    void onExecuteCharge(int charge);

    @Override
    default void execute(boolean isKeyDown) {
        int charges = getCharges();

        if (charges > 0) {
            setCharges(charges - 1);
            onExecuteCharge(charges - 1);
        }
    }

    default void onCooldownOver() {
        setCharges(getCharges() + 1);
    }

}
