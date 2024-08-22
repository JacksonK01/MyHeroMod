package net.michaeljackson23.mineademia.abilitysystem.impl.server.ability.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.DataPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class TogglePassiveAbility extends DataPassiveAbility {

    private boolean toggled;

    public TogglePassiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description) {
        super(user, name, description);
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

}
