package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

@Deprecated
//TODO this ability type hasn't been implemented yet
public abstract class ToggleAbility extends AbilityBase {
    private boolean defaultCondition = false;
    protected ToggleAbility(int staminaDrain, int cooldownAdd, String title, String description) {
        super(1, staminaDrain, cooldownAdd, false, false, title, description);
    }

    @Override
    protected boolean executeCondition(Quirk quirk) {
        return timer <= abilityDuration;
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        this.onToggleActivation(player, quirk);
        quirk.addPassive(this::toggle);
    }

    protected abstract void onToggleActivation(ServerPlayerEntity player, Quirk quirk);

    /*
    @returns true when toggle is over
    */
    protected abstract boolean toggle(ServerPlayerEntity player, Quirk quirk);
}
