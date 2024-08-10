package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IHoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class HoldAbility extends ActivationAbility implements IHoldAbility {

    public HoldAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        super(user, name, description, categories);
    }

}
