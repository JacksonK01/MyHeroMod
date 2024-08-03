package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class ToggleAbility extends ActiveAbility implements IToggleAbility {

    private boolean toggled;

    public ToggleAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        super(user, name, description, categories);

        this.toggled = false;
    }

    @Override
    public boolean isToggled() {
        return toggled;
    }

    @Override
    public void setToggle(boolean toggled) {
        this.toggled = toggled;
    }

}
