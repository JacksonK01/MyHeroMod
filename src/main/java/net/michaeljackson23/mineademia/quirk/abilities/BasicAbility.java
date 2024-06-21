package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class BasicAbility extends AbilityBase {
    protected BasicAbility(int abilityDuration, int staminaDrain, int cooldownAdd, String title, String description) {
        super(abilityDuration, staminaDrain, cooldownAdd, false, false, title, description);
    }

    @Override
    protected boolean executeCondition(Quirk quirk) {
        return timer <= abilityDuration;
    }
}
