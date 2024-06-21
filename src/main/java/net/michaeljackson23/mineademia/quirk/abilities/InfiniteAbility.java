package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;

public abstract class InfiniteAbility extends AbilityBase {
    protected InfiniteAbility(int staminaDrain, int cooldownAdd, String title, String description) {
        super(1, staminaDrain, cooldownAdd, false, true, title, description);
    }

    @Override
    protected boolean executeCondition(Quirk quirk) {
        return true;
    }
}
