package net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive;

import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class TogglePassiveAbility extends DataPassiveAbility {

    private boolean active;

    public TogglePassiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description) {
        super(user, name, description);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
