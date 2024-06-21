package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;

public abstract class HoldableAbility extends AbilityBase {
    protected HoldableAbility(int staminaDrain, int cooldownAdd, String title, String description) {
        super(0, staminaDrain, cooldownAdd, true, false, title, description);
    }

    @Override
    protected boolean executeCondition(Quirk quirk) {
        if(getStaminaDrain() > quirk.getStamina()) {
            return false;
        }
        return this.isCurrentlyHeld();
    }
}
